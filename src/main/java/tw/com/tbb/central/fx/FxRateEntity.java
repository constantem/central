package tw.com.tbb.central.fx;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FX_RATE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FxRateEntity {
    @Id
    private String currency; 
    private Double buyRate;  
    private Double sellRate; 
}