package tw.com.tbb.central.tw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.com.tbb.central.constants.ErrorCodeConstants;
import tw.com.tbb.central.exception.BizException;
import tw.com.tbb.central.tw.api.n920.N920Rq;
import tw.com.tbb.central.tw.api.n920.N920Rs;
import tw.com.tbb.central.tw.entity.AccountEntity;
import tw.com.tbb.central.tw.repository.AccountRepository;

import java.util.List;

@Service
public class N920Service {
    @Autowired
    private AccountRepository accountRepo;

    public N920Rs queryAccounts(N920Rq rq) {

        List<AccountEntity> list = accountRepo.findByCusidnOrderByAccountNoAsc(rq.getCusidn());

        // 🔥 5️⃣ 空清單 = 業務錯誤
        if (list.isEmpty()) {
            throw new BizException(ErrorCodeConstants.E256); // 查無帳號
        }

        N920Rs rs = new N920Rs();
        rs.setCode("0000");
        rs.setCount(String.valueOf(list.size()));
        rs.setAccounts(list.stream().map(this::toAccount).toList());

        return rs;
    }

    private N920Rs.Account toAccount(AccountEntity e) {
        N920Rs.Account a = new N920Rs.Account();
        a.setAcn(e.getAccountNo());
        return a;
    }
}
