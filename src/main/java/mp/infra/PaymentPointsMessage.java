package mp.infra;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentPointsMessage {
    @JsonProperty("userId")
    private UUID userId;
    private int point;
    
    private String createdAt;
}
