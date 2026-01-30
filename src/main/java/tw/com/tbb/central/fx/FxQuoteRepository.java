package tw.com.tbb.central.fx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxQuoteRepository extends JpaRepository<FxQuoteEntity, String> {
}