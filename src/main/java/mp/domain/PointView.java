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
    private UUID userId;
    
    private Integer lastPointChange;  // 마지막 포인트 변동량 (+500, -500 등)
    private Integer totalPoint;       // 현재 사용 가능한 총 포인트
    private String lastReason;        // 마지막 변동 사유 (SIGNUP, PAYMENT, USED)
    private LocalDateTime updatedAt;  // 마지막 업데이트 시간

    public void applyPointChange(Point point) {
        this.lastPointChange = point.getPoint();
        this.totalPoint = point.getTotalPoint();
        this.lastReason = point.getReason();
        this.updatedAt = LocalDateTime.now();
    }
}
