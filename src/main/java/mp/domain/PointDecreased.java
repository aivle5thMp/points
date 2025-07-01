package mp.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

//<<< DDD / Domain Event
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDecreased extends AbstractEvent {

    private UUID id;
    private UUID userId;
    private Double point;
    private Double totalPoint;
    private LocalDateTime createdAt;

    public PointDecreased(Point aggregate) {
        super(aggregate);
    }
}
//>>> DDD / Domain Event
