package mp.domain;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class Purchased extends AbstractEvent {

    private UUID id;
    private UUID userId;
    private String item;
    private Integer amount;
    private String status;
    private LocalDateTime createdAt;
}
