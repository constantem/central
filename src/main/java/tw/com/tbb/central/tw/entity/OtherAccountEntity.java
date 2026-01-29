package tw.com.tbb.central.tw.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//模擬他行
@Getter
@Setter
@Entity
@Table(name = "other_account")
@IdClass(OtherAccountId.class)
public class OtherAccountEntity {
    @Id
    @Column(name = "bank_code")
    private String bankCode;
    @Id
    @Column(name = "account_no")
    private String accountNo;
    private String cusidn;
    private String balance;
}
