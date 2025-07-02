package mp.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class Purchased extends AbstractEvent {

    private UUID id;
    private String userId;
    private String item;
    private Long amount;
    private String status;
    private LocalDate createdAt;
}
