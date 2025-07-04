package mp.infra;

import java.util.UUID;
import java.time.LocalDateTime;

import mp.domain.*;
import mp.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

//<<< Clean Arch / Inbound Adaptor
@Service
@Slf4j
public class PolicyHandler {

    @Autowired
    PointsService pointsService;

    @Autowired
    PointViewRepository pointViewRepository;

    private void updatePointView(Point point) {
        PointView pointView = pointViewRepository.findById(point.getUserId())
                .orElse(PointView.builder()
                        .userId(point.getUserId())
                        .lastPointChange(0)
                        .totalPoint(0)
                        .lastReason("초기화")
                        .updatedAt(LocalDateTime.now())
                        .build());
        
        pointView.applyPointChange(point);
        pointViewRepository.save(pointView);
        
        log.info("포인트 View 업데이트 완료 - 사용자: {}, 변동: {}, 총액: {}, 사유: {}", 
                point.getUserId(), point.getPoint(), point.getTotalPoint(), point.getReason());
    }

    // 회원가입 완료 이벤트 수신
    @StreamListener(KafkaProcessor.SIGNUP_INPUT)
    public void whenRegistered_then_CREATE(@Payload Registered registered) {
        log.info("회원가입 이벤트 수신: {}", registered);
        try {
            Point point = pointsService.addPointForSignup(registered.getId(), registered.getInitialPoint());
            updatePointView(point);
        } catch (Exception e) {
            log.error("회원가입 포인트 적립 실패", e);
        }
    }

    // 결제 완료 이벤트 수신 (포인트 적립)
    @StreamListener(KafkaProcessor.PAYMENT_INPUT)
    public void whenPurchased_then_CREATE(@Payload Purchased purchased) {
        log.info("결제 이벤트 수신: {}", purchased);
        try {
            // 구독 결제인 경우 포인트 적립하지 않음
            if (purchased.getItem() != null && purchased.getItem().toLowerCase().contains("subscription")) {
                log.info("구독 결제로 인한 포인트 적립 제외 - 사용자: {}", purchased.getUserId());
                return;
            }
            
            Point point = pointsService.addPointForPayment(purchased.getUserId(), purchased.getAmount());
            updatePointView(point);
        } catch (Exception e) {
            log.error("결제 포인트 적립 실패", e);
        }
    }

    // 책 구매 이벤트 수신 (포인트 사용)
    @StreamListener(KafkaProcessor.BOOK_INPUT)
    public void whenBookPurchased_then_CREATE(@Payload BookPurchased bookPurchased) {
        log.info("책 구매 이벤트 수신: {}", bookPurchased);
        try {
            // 포인트 사용이 0인 경우 (구독자) 처리하지 않음
            if (bookPurchased.getPoint() == null || bookPurchased.getPoint() == 0) {
                log.info("구독자 구매 - 포인트 차감 없음: userId={}", bookPurchased.getUserId());
                return;
            }
            
            Point point = pointsService.usePointForBook(bookPurchased.getUserId(), bookPurchased.getPoint());
            updatePointView(point);
        } catch (Exception e) {
            log.error("책 구매 포인트 차감 실패", e);
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
