package tw.com.tbb.central.tw.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//自行/他行 約定帳號
@Getter
@Setter
@Entity
@Table(name = "designated_account")
@IdClass(DesignatedAccountId.class)
public class DesignatedAccountEntity {
    @Id
    @Column(name = "bank_code")
    private String bankCode;
    @Id
    @Column(name = "account_no")
    private String accountNo;
    private String cusidn;
}
