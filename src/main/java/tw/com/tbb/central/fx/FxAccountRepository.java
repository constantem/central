package tw.com.tbb.central.fx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 負責處理 FX_ACCOUNT 與 FX_ACCOUNT_BALANCES 兩張表的資料存取
 * JpaRepository<實體類別, 主鍵型態>
 */
@Repository
public interface FxAccountRepository extends JpaRepository<FxAccountEntity, String> {

}