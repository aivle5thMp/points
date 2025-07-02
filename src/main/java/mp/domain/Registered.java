package mp.domain;

import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class Registered extends AbstractEvent {

    private UUID userId;
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Boolean isSubscribed;
    public Integer getInitialPoint() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInitialPoint'");
    }
}
