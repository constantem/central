package tw.com.tbb.central.tw.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DesignatedAccountId implements Serializable {
    private String bankCode;
    private String accountNo;
}
