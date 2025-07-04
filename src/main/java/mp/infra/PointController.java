package mp.infra;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import mp.domain.*;
import mp.util.UserHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("/points")
public class PointController {

    @Autowired
    private PointViewRepository pointViewRepository;

    @Autowired
    private PointsRepository pointsRepository;

    @Autowired
    private PointsService pointsService;

    /**
     * 현재 포인트 잔액 조회 (PointView 사용)
     */
    @GetMapping("/balance")
    public ResponseEntity<Map<String, PointBalanceDto>> getCurrentBalance(HttpServletRequest request) {
        if (!UserHeaderUtil.isAuthenticated(request)) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        
        UUID userId = UserHeaderUtil.getUserId(request);
        PointView pointView = pointViewRepository.findById(userId)
                .orElse(PointView.builder()
                        .userId(userId)
                        .lastPointChange(0)
                        .totalPoint(0)
                        .lastReason("초기화")
                        .updatedAt(LocalDateTime.now())
                        .build());
        
        Map<String, PointBalanceDto> response = new HashMap<>();
        response.put("data", PointBalanceDto.fromEntity(pointView));
        return ResponseEntity.ok(response);
    }

    /**
     * 포인트 이력 조회 (페이징)
     */
    @GetMapping("/history")
    public Page<PointHistoryDto> getPointHistory(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String reason
    ) {
        if (!UserHeaderUtil.isAuthenticated(request)) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        
        UUID userId = UserHeaderUtil.getUserId(request);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Point> points;
        if (reason != null && !reason.isEmpty()) {
            points = pointsRepository.findByUserIdAndReasonOrderByCreatedAtDesc(userId, reason, pageable);
        } else {
            points = pointsRepository.findHistoryByUserId(userId, pageable);
        }
        
        return points.map(PointHistoryDto::fromEntity);
    }

    /**
     * 현재 사용 가능한 총 포인트 조회 (실시간)
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentPoint(HttpServletRequest request) {
        if (!UserHeaderUtil.isAuthenticated(request)) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        
        UUID userId = UserHeaderUtil.getUserId(request);
        int currentTotal = pointsService.getCurrentTotalPoint(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("totalPoint", currentTotal);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 포인트 사용 가능 여부 확인
     */
    @GetMapping("/can-use")
    public ResponseEntity<Map<String, Object>> canUsePoint(
            HttpServletRequest request,
            @RequestParam int amount
    ) {
        if (!UserHeaderUtil.isAuthenticated(request)) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        
        UUID userId = UserHeaderUtil.getUserId(request);
        boolean canUse = pointsService.canUsePoint(userId, amount);
        int currentTotal = pointsService.getCurrentTotalPoint(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("canUse", canUse);
        response.put("requestedAmount", amount);
        response.put("currentTotal", currentTotal);
        response.put("shortage", canUse ? 0 : amount - currentTotal);
        
        return ResponseEntity.ok(response);
    }
}
//>>> Clean Arch / Inbound Adaptor
