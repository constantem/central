package tw.com.tbb.central.fx;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse extends BaseResponse {
    private String tradeTime;
    private String fromAccount;
    private Double fromAmount;
    private String fromCurr;
    private String toAccount;
    private Double toAmount;
    private String toCurr;
    private Double rate;
    private Double availableBalance;
}