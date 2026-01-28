package tw.com.tbb.central.tw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.tbb.central.tw.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity,String> {
}
