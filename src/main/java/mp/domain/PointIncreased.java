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
public class PointIncreased extends AbstractEvent {

    private UUID id;
    private UUID userId;
    private Integer point;
    private Integer totalPoint;
    private LocalDateTime createdAt;

    public PointIncreased(Point aggregate) {
        super(aggregate);
    }
}
//>>> DDD / Domain Event
