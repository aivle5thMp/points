package mp.infra;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookPurchasedMessage {
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("point_used")
    private int pointUsed;
    
}
