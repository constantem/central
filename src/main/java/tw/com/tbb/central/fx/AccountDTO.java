package tw.com.tbb.central.fx;
import lombok.*;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @Schema(description = "外幣帳戶", example = "010111912224")
    private String accountNumber;
    @Schema(description = "外幣餘額List")
    private List<BalanceDTO> balances;
}