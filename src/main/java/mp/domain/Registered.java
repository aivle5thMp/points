package mp.domain;

import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class Registered extends AbstractEvent {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Boolean isSubscribed;
}
