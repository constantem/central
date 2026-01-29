package tw.com.tbb.central.tw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.tbb.central.tw.entity.OtherAccountEntity;
import tw.com.tbb.central.tw.entity.OtherAccountId;

public interface OtherAccountRepository extends JpaRepository<OtherAccountEntity, OtherAccountId> {
}
