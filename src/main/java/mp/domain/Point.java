package mp.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Point_table")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private UUID userId;

    // 포인트 변동량 (적립 시 양수, 사용 시 음수)
    private Integer point;

    // 변동 후 누적 포인트
    private Integer totalPoint;

    // 변동 사유 (SIGNUP, PAYMENT, USED)
    private String reason;

    private LocalDateTime createdAt;

    public static Point createSignupPoint(UUID userId, int initialPoint) {
        return Point.builder()
                .userId(userId)
                .point(initialPoint)
                .totalPoint(initialPoint)
                .reason("SIGNUP")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Point createPaymentPoint(UUID userId, int amount) {
        int pointToAdd = (int) (amount);
        return Point.builder()
                .userId(userId)
                .point(pointToAdd)
                .totalPoint(pointToAdd) // 이전 totalPoint는 조회해서 더해줘야 함
                .reason("PAYMENT")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Point createUsedPoint(UUID userId, int usedPoint) {
        return Point.builder()
                .userId(userId)
                .point(-usedPoint)
                .totalPoint(-usedPoint) // 이전 totalPoint는 조회해서 계산해줘야 함
                .reason("USED")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
