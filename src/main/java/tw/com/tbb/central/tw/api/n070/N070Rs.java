package tw.com.tbb.central.tw.api.n070;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import tw.com.tbb.central.tw.api.common.ApiRs;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class N070Rs extends ApiRs {
    @Schema(description = "轉帳金額")
    private String amount;//轉帳金額
    @Schema(description = "轉出帳號")
    private String outacn;//轉出帳號
    @Schema(description = "轉出帳號帳上餘額")
    private String outbal;//轉出帳號帳上餘額
    @Schema(description = "轉入行庫別")
    private String inbnk;//轉入行庫別
    @Schema(description = "轉入帳號")
    private String inacn;//轉入帳號
}
