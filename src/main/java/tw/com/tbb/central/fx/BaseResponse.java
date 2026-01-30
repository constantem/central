package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@SuperBuilder // 使用 SuperBuilder 讓子類繼承時也能使用 Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    @lombok.Builder.Default
    private String code = "0000"; // 預設成功代碼
}