package mp.infra;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSignupMessage {
    @JsonProperty("id")
    private UUID userId;
    @JsonProperty("initial_point")
    private int initialPoint;
}
