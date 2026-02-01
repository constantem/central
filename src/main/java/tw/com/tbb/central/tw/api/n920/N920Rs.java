package tw.com.tbb.central.tw.api.n920;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import tw.com.tbb.central.tw.api.common.ApiRs;

import java.util.List;

@Getter
@Setter
public class N920Rs extends ApiRs {
    @Schema(description = "筆數")
    private String count;
    private List<Account> accounts;

    @Getter
    @Setter
    public static class Account {
        @Schema(description = "帳號")
        private String acn;
    }
}
