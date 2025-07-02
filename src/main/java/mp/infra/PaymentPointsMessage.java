package mp.infra;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaymentPointsMessage {
    @JsonProperty("user_id")
    private UUID userId;
    private int point;
}
