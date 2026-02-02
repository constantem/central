package tw.com.tbb.central.fx;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "N510外幣帳戶餘額回應")
public class N510Rs extends BaseResponse {
    @Schema(description = "外幣帳戶List")
    private List<AccountDTO> accountList; // 內含原有的帳號與餘額清單
}