package mp.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//<<< EDA / CQRS
@Entity
@Table(name = "PointView_table")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointView {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;
    
    private Double point;
    private Double totalPoint;
    private String pointStatus;
    private LocalDateTime createdAt;
}
