package tw.com.tbb.central.tw.api.n110;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import tw.com.tbb.central.tw.api.common.ApiRs;

import java.util.List;

@Getter
@Setter
public class N110Rs extends ApiRs {
    @Schema(description = "筆數", example = "1")
    private String count;
    @Schema(description = "帳號清單")
    private List<Account> accounts;

    @Schema(name = "N110Account", description = "帳號清單資訊")
    @Getter
    @Setter
    public static class Account {
        @Schema(description = "帳號", example = "050100000001")
        private String acn;
        @Schema(description = "餘額", example = "10000")
        private String bal;
    }
}
