package tw.com.tbb.central.tw.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "error_code")
public class ErrorCodeEntity {
    @Id
    private String code;
    private String message;
}
