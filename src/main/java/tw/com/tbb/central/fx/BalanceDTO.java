package tw.com.tbb.central.fx;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
    private String currency;
    private Double balance;
}