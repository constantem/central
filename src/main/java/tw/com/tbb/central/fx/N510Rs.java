package tw.com.tbb.central.fx;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class N510Rs extends BaseResponse {
    private List<AccountDTO> accountList; // 內含原有的帳號與餘額清單
}