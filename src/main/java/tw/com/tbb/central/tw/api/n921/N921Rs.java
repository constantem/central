package tw.com.tbb.central.tw.api.n921;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import tw.com.tbb.central.tw.api.common.ApiRs;

import java.util.List;

@Getter
@Setter
public class N921Rs extends ApiRs {
    @Schema(description = "筆數", example = "2")
    private String count;
    @Schema(description = "約定帳號清單")
    private List<Account> accounts;

    @Schema(
            name = "N921Account",
            description = "約定帳號清單資訊",
            example = """
        [
          {
            "acn": "004983450012",
            "bnkcod": "004"
          },
          {
            "acn": "007120034556",
            "bnkcod": "007"
          }
        ]
        """
    )
    @Getter
    @Setter
    public static class Account {
        @Schema(description = "約定帳號", example = "004983450012")
        private String acn;
        @Schema(description = "行庫別", example = "004")
        private String bnkcod;
    }
}
