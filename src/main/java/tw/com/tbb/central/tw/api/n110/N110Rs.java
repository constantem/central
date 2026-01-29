package tw.com.tbb.central.tw.api.n110;

import lombok.Getter;
import lombok.Setter;
import tw.com.tbb.central.tw.api.common.ApiRs;

import java.util.List;

@Getter
@Setter
public class N110Rs extends ApiRs {
    private String count;
    private List<Account> accounts;

    @Getter
    @Setter
    public static class Account {
        private String acn;
        private String bal;
    }
}
