package tw.com.tbb.central.tw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.com.tbb.central.constants.ErrorCodeConstants;
import tw.com.tbb.central.exception.BizRollbackException;
import tw.com.tbb.central.tw.api.n110.N110Rq;
import tw.com.tbb.central.tw.api.n110.N110Rs;
import tw.com.tbb.central.tw.entity.AccountEntity;
import tw.com.tbb.central.tw.repository.AccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class N110Service {
    @Autowired
    private AccountRepository accountRepo;

    public N110Rs queryBalance(N110Rq rq) {
        N110Rs rs = new N110Rs();

        // 1. 根據 ACN (帳號) 查詢。
        List<AccountEntity> entities;

        if (rq.getAcn() != null && !rq.getAcn().isEmpty()) {
            entities = accountRepo.findById(rq.getAcn())
                    .map(List::of)
                    .orElse(Collections.emptyList());

            // (3) 帳號查不到
            if (entities.isEmpty()) {
                throw new BizRollbackException(ErrorCodeConstants.E255); // 帳號不存在
            }
        } else {
            // 如果沒有帶 ACN，通常會根據 CUSIDN 查該用戶所有帳號
            entities = accountRepo.findAll();

            if (entities.isEmpty()) {
                throw new BizRollbackException(ErrorCodeConstants.E255);  // 帳號不存在
            }
        }

        // 2. 組回應
        List<N110Rs.Account> accountList = entities.stream().map(entity -> {
            N110Rs.Account acc = new N110Rs.Account();
            acc.setAcn(entity.getAccountNo());
            acc.setBal(entity.getBalance());
            return acc;
        }).collect(Collectors.toList());

        // 3. 設定結果
        rs.setAccounts(accountList);
        rs.setCount(String.valueOf(accountList.size()));
        rs.setCode(ErrorCodeConstants.SUCCESS);
        return rs;
    }
}
