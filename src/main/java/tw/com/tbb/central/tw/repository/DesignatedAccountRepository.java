package tw.com.tbb.central.tw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.tbb.central.tw.entity.DesignatedAccountEntity;
import tw.com.tbb.central.tw.entity.DesignatedAccountId;

import java.util.List;

public interface DesignatedAccountRepository extends JpaRepository<DesignatedAccountEntity, DesignatedAccountId> {
    List<DesignatedAccountEntity> findByCusidn(String cusidn);
}
