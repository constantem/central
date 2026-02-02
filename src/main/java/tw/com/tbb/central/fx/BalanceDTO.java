package tw.com.tbb.central.fx;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
    @Schema(description = "餘額幣別", example = "USD")
    private String currency;
    @Schema(description = "餘額", example = "1000.0")
    private Double balance;
}