package mp.domain;

import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointsService {
    private final PointsRepository pointsRepository;
    
    /**
     * 사용자의 현재 포인트 잔액 조회
     */
    public PointBalanceDto getPointBalance(UUID userId) {
        Point latestPoint = pointsRepository.findFirstByUserIdOrderByCreatedAtDescIdDesc(userId)
                .orElse(Point.builder()
                        .userId(userId)
                        .point(0)
                        .totalPoint(0)
                        .reason("초기화")
                        .build());
        
        return PointBalanceDto.builder()
                .userId(userId)
                .lastPointChange(latestPoint.getPoint())
                .totalPoint(latestPoint.getTotalPoint())
                .lastReason(latestPoint.getReason())
                .updatedAt(latestPoint.getCreatedAt())
                .build();
    }

    /**
     * 회원가입 포인트 적립
     */
    @Transactional
    public Point addPointForSignup(UUID userId, int initialPoint) {
        Point point = Point.createSignupPoint(userId, initialPoint);
        pointsRepository.save(point);
        log.info("회원가입 포인트 적립 완료: userId={}, point={}", userId, point.getPoint());
        return point;
    }

    /**
     * 결제 포인트 적립 (결제 금액의 1.05% 적립)
     */
    @Transactional
    public Point addPointForPayment(UUID userId, int amount) {
        // 이전 누적 포인트 조회
        int previousTotal = getCurrentTotalPoint(userId);
        
        Point point = Point.createPaymentPoint(userId, amount);
        point.setTotalPoint(previousTotal + point.getPoint());
        pointsRepository.save(point);
        
        log.info("결제 포인트 적립 완료: userId={}, point={}, totalPoint={}", 
                userId, point.getPoint(), point.getTotalPoint());
        return point;
    }

    /**
     * 책 구매 포인트 사용
     */
    @Transactional
    public Point usePointForBook(UUID userId, int pointToUse) {
        // 최신 포인트 이력 조회
        Point latestPoint = pointsRepository.findFirstByUserIdOrderByCreatedAtDescIdDesc(userId)
                .orElseThrow(() -> new IllegalArgumentException("포인트 내역이 없는 사용자입니다: " + userId));
        
        int currentTotal = latestPoint.getTotalPoint();
        if (currentTotal < pointToUse) {
            throw new IllegalArgumentException(
                String.format("포인트가 부족합니다. 현재:%d, 필요:%d", currentTotal, pointToUse));
        }

        Point point = Point.createUsedPoint(userId, pointToUse);
        point.setTotalPoint(currentTotal + point.getPoint()); // point는 음수라 더하기
        pointsRepository.save(point);
        
        log.info("포인트 사용 완료: userId={}, usedPoint={}, totalPoint={}", 
                userId, pointToUse, point.getTotalPoint());
        return point;
    }

    /**
     * 사용자의 현재 총 포인트 조회
     */
    public int getCurrentTotalPoint(UUID userId) {
        return pointsRepository.findFirstByUserIdOrderByCreatedAtDescIdDesc(userId)
                .map(Point::getTotalPoint)
                .orElse(0);
    }

    /**
     * 포인트 사용 가능 여부 확인
     */
    public boolean canUsePoint(UUID userId, int pointToUse) {
        int currentTotal = getCurrentTotalPoint(userId);
        return currentTotal >= pointToUse;
    }
}
