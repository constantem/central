package tw.com.tbb.central.tw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.com.tbb.central.constants.ErrorCodeConstants;
import tw.com.tbb.central.tw.api.n070.N070Rq;
import tw.com.tbb.central.tw.api.n070.N070Rs;
import tw.com.tbb.central.tw.entity.AccountEntity;
import tw.com.tbb.central.tw.entity.OtherAccountEntity;
import tw.com.tbb.central.tw.entity.OtherAccountId;
import tw.com.tbb.central.tw.repository.AccountRepository;
import tw.com.tbb.central.tw.repository.OtherAccountRepository;

@Service
public class N070Service {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private OtherAccountRepository otherAccountRepo;

    @Value("${tbb.bank.code}")
    private String TBB_BANK_CODE;
    @Value("${password}")
    private String password;

    @Transactional
    public N070Rs transfer(N070Rq rq) {

        // 1) 轉帳金額檢核
        long amt = parseAmountOrThrow(rq.getAmount());
        if (amt <= 0) {
            return errorRs(ErrorCodeConstants.E253);//金額需 > 0
        }

        // 2) 轉出帳號存在（一定是本行 account）
        AccountEntity out = accountRepo.findById(rq.getOutacn()).orElse(null);
        if (out == null) return errorRs(ErrorCodeConstants.E250);//轉出帳號不存在

        // 3) 密碼檢核
        if (!password.equals(rq.getPinnew())) {
            return errorRs(ErrorCodeConstants.E254);//密碼錯誤
        }

        String inBank = rq.getInbnk();
        String inAcn  = rq.getInacn();
        boolean isSelfBank = TBB_BANK_CODE.equals(inBank);

        // 4) 先檢查轉入存在 / 行庫是否支援（⚠️ 動錢之前先把會失敗的都擋掉）
        AccountEntity inSelf = null;
        OtherAccountEntity inOther = null;
        if (isSelfBank) {
            inSelf = accountRepo.findById(inAcn).orElse(null);
            if (inSelf == null) return errorRs(ErrorCodeConstants.E251);//轉入帳號不存在
        } else {
            OtherAccountId id = new OtherAccountId(inBank, inAcn);
            inOther = otherAccountRepo.findById(id).orElse(null);
            if (inOther == null) return errorRs(ErrorCodeConstants.E251);//轉入帳號不存在
        }

        // 5) 檢核餘額
        long outBal = parseMoney(out.getBalance());
        if (outBal < amt) {
            return errorRs(ErrorCodeConstants.E252);//餘額不足
        }

        // 6) 扣款
        long newOutBal = outBal - amt;
        out.setBalance(toMoneyString(newOutBal));
        accountRepo.save(out);

        // 7) 入帳
        if (isSelfBank) {
            long inBal = parseMoney(inSelf.getBalance());
            long newInBal = inBal + amt;
            inSelf.setBalance(toMoneyString(newInBal));
            accountRepo.save(inSelf);
        } else {
            long inBal = parseMoney(inOther.getBalance());
            long newInBal = inBal + amt;
            inOther.setBalance(toMoneyString(newInBal));
            otherAccountRepo.save(inOther);
        }

        // 8) 回傳
        return N070Rs.builder()
                .code(ErrorCodeConstants.SUCCESS)
                .amount(toMoneyString(amt))
                .outacn(rq.getOutacn())
                .outbal(toMoneyString(newOutBal))
                .inbnk(inBank)
                .inacn(inAcn).build();
    }

    // ---------- helpers ----------

    private N070Rs errorRs(String code) {
        N070Rs rs = new N070Rs();
        rs.setCode(code);
        return rs;
    }

    private long parseAmountOrThrow(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return -1; // 統一給上層擋
        }
    }

    private long parseMoney(String s) {
        if (s == null || s.isBlank()) return 0L;
        return Long.parseLong(s);
    }

    private String toMoneyString(long v) {
        return Long.toString(v);
    }
}
