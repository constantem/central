package tw.com.tbb.central.tw.api.n920;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import tw.com.tbb.central.tw.api.common.ApiRs;

import java.util.List;

@Getter
@Setter
public class N920Rs extends ApiRs {
    @Schema(description = "筆數", example = "2")
    private String count;
    @Schema(description = "帳號清單")
    private List<Account> accounts;

    @Schema(
            name = "N920Account",
            description = "帳號清單資訊",
            example = """
        [
          { "acn": "050100000001" },
          { "acn": "050100000002" }
        ]
        """
    )
    @Getter
    @Setter
    public static class Account {
        @Schema(description = "帳號", example = "050100000001")
        private String acn;
    }
}
