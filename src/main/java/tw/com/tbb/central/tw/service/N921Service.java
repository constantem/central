package tw.com.tbb.central.tw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.com.tbb.central.constants.ErrorCodeConstants;
import tw.com.tbb.central.exception.BizException;
import tw.com.tbb.central.tw.api.n921.N921Rq;
import tw.com.tbb.central.tw.api.n921.N921Rs;
import tw.com.tbb.central.tw.entity.DesignatedAccountEntity;
import tw.com.tbb.central.tw.repository.DesignatedAccountRepository;

import java.util.List;

@Service
public class N921Service {
    @Autowired
    private DesignatedAccountRepository designatedAccountRepo;

    public N921Rs queryDesignatedAccounts(N921Rq rq) {

        List<DesignatedAccountEntity> list =
                designatedAccountRepo.findByCusidn(rq.getCusidn());

        // 🔥 5️⃣ 空清單 = 業務錯誤
        if (list.isEmpty()) {
            throw new BizException(ErrorCodeConstants.E257); // 查無約定帳號
        }

        N921Rs rs = new N921Rs();
        rs.setCount(String.valueOf(list.size()));

        rs.setAccounts(list.stream().map(e -> {
            N921Rs.Account acc = new N921Rs.Account();
            acc.setAcn(e.getAccountNo());
            acc.setBnkcod(e.getBankCode());
            return acc;
        }).toList());

        return rs;
    }
}
