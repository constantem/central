package tw.com.tbb.central.fx;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueryRequest {
    @JsonProperty("CUSIDN")
    private String cusidn;
}