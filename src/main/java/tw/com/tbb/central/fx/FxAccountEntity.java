package tw.com.tbb.central.fx;


import jakarta.persistence.*;
import lombok.*;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "FX_ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FxAccountEntity {
    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "FX_ACCOUNT_BALANCES", 
        joinColumns = @JoinColumn(name = "account_number")
    )
    @MapKeyColumn(name = "currency")
    @Column(name = "balance")
    private Map<String, Double> balances;
}