package mp.infra;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserSignupMessage {
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("initial_point")
    private int initialPoint;

    
}
