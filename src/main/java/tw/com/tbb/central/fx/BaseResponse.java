package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@SuperBuilder // 必須是 SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    @Schema(description = "回應代碼")
    @lombok.Builder.Default
    private String code = "0000"; // 預設成功代碼
}