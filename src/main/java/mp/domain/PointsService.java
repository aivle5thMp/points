package mp.domain;

import java.time.LocalDateTime;
import java.util.Date;
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
    private final PointHistoryRepository pointHistoryRepository;

    
    @Transactional
    public void addPointForSignup(UUID userId) {
        int initialPoint = 1000;
        // 1. 신규 회원 포인트 적립 로직 (DB에 저장)
        log.info("회원가입 포인트 적립: userId={}, point={}", userId, initialPoint);
        Point point = pointsRepository.findByUserId(userId).orElseGet(() -> {
            Point p = new Point();
            p.setUserId(userId);
            p.setPoint(0);
            p.setTotalPoint(0);
            p.setCreatedAt(LocalDateTime.now());
            return p;
        });
        point.setPoint(initialPoint); // 현재 이벤트의 포인트 (ex. 100)
        point.setTotalPoint(point.getTotalPoint() + initialPoint); // 누적 포인트
        point.setCreatedAt(LocalDateTime.now());
        pointsRepository.save(point);

        PointHistory history = new PointHistory();
        history.setUserId(userId);
        history.setChangedPoint(initialPoint);
        history.setReason("회원가입");
        history.setCreatedAt(LocalDateTime.now());
        history.setTotalPoint(point.getTotalPoint());
        pointHistoryRepository.save(history);
    }

    @Transactional
    public void addPointForPayment(UUID userId, int point) {
        // 2. 결제 시 포인트 적립
        log.info("포인트 결제 적립: userId={}, point={}", userId, point);
        Point pointEntity = pointsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다: " + userId));
        pointEntity.setPoint(point); // 이번에 적립된 포인트(이벤트 단위)
        pointEntity.setTotalPoint(pointEntity.getTotalPoint() + point); // 누적 총합
        pointEntity.setCreatedAt(LocalDateTime.now());
        pointsRepository.save(pointEntity);

        PointHistory history = new PointHistory();
        history.setUserId(userId);
        history.setChangedPoint(point);
        history.setReason("결제 적립");
        history.setCreatedAt(LocalDateTime.now());
        history.setTotalPoint(pointEntity.getTotalPoint());
        pointHistoryRepository.save(history);
    }

    public void usePointForBook(UUID userId, int pointUsed) {
        // 3. 책 구매 시 포인트 차감
        log.info("책 구매 포인트 차감: userId={}, pointUsed={}", userId, pointUsed);
        Point point = pointsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다: " + userId));
        if (point.getTotalPoint() < pointUsed) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        point.setPoint(-pointUsed); // 이번 이벤트에서 차감된 포인트
        point.setTotalPoint(point.getTotalPoint() - pointUsed); // 누적 포인트 차감
        point.setCreatedAt(LocalDateTime.now());
        pointsRepository.save(point);

        PointHistory history = new PointHistory();
        history.setUserId(userId);
        history.setChangedPoint(-pointUsed);
        history.setReason("책 구매 차감");
        history.setCreatedAt(LocalDateTime.now());
        history.setTotalPoint(point.getTotalPoint());
        pointHistoryRepository.save(history);
    }
}
