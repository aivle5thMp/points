package mp.infra;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mp.domain.PointView;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryDto  {
    private Integer point;
    private Integer totalPoint;
    private String pointStatus;
    private LocalDateTime createdAt;
    
    public static PointHistoryDto fromEntity(PointView entity) {
        return PointHistoryDto.builder()
                .point(entity.getPoint())
                .totalPoint(entity.getTotalPoint())
                .pointStatus(entity.getPointStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
