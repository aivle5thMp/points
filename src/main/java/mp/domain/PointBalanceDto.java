package mp.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointBalanceDto {
    private UUID userId;
    private Integer lastPointChange;  // 마지막 포인트 변동량 (+500, -500 등)
    private Integer totalPoint;       // 현재 사용 가능한 총 포인트
    private String lastReason;        // 마지막 변동 사유 (SIGNUP, PAYMENT, USED)
    private LocalDateTime updatedAt;  // 마지막 업데이트 시간

    public static PointBalanceDto fromEntity(PointView pointView) {
        return PointBalanceDto.builder()
                .userId(pointView.getUserId())
                .lastPointChange(pointView.getLastPointChange())
                .totalPoint(pointView.getTotalPoint())
                .lastReason(pointView.getLastReason())
                .updatedAt(pointView.getUpdatedAt())
                .build();
    }
} 