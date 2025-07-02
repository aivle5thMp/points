package mp.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "point_history")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID userId;

    // 적립/차감된 포인트(변동분, 예: +1000, -3000)
    private Integer changedPoint;

    // 적립/차감 사유 (예: "가입", "결제", "책구매" 등)
    private String reason;

    // 이벤트 발생 시각
    private LocalDateTime createdAt;

    // 변화 후 최종 누적 포인트(조회용)
    private Integer totalPoint;

    // getter/setter 생략 or lombok 사용
    // @Getter @Setter
}
