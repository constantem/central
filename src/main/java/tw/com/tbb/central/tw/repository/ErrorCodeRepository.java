package tw.com.tbb.central.tw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.tbb.central.tw.entity.ErrorCodeEntity;

public interface ErrorCodeRepository extends JpaRepository<ErrorCodeEntity, String> {
}
