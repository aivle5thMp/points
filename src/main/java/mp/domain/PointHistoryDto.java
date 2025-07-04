package mp.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointHistoryDto {
    private Long id;
    private UUID userId;
    private Integer point;
    private Integer totalPoint;
    private String reason;
    private LocalDateTime createdAt;

    public static PointHistoryDto fromEntity(Point point) {
        return PointHistoryDto.builder()
                .id(point.getId())
                .userId(point.getUserId())
                .point(point.getPoint())
                .totalPoint(point.getTotalPoint())
                .reason(point.getReason())
                .createdAt(point.getCreatedAt())
                .build();
    }
} 