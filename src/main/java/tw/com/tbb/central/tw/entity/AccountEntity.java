package tw.com.tbb.central.tw.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @Column(name = "account_no")
    private String accountNo;
    private String balance;
}
