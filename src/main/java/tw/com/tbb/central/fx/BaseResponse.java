package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.*;

@Data
@SuperBuilder // 必須是 SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    @lombok.Builder.Default
    private String code = "0000"; // 預設成功代碼
}