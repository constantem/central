package tw.com.tbb.central.fx;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime; // 必須要有這一行

@Entity
@Table(name = "FX_QUOTE_LOG")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FxQuoteEntity {
    @Id
    private String quoteId;
    private String cusidn;
    private String fromCurr;
    private String toCurr;
    private Double amount;
    private Double convertedAmount;
    private Double rate;
    private LocalDateTime expiryTime;
}