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
        log.info("{\"event\":\"PAYMENT_RECEIVED\", \"type\":\"PAYMENT\", \"userId\":\"{}\", \"amount\":{}, \"item\":\"{}\"}", 
            purchased.getUserId(), purchased.getAmount(), purchased.getItem());
            
        try {
            // 구독 결제인 경우 포인트 적립하지 않음
            if (purchased.getItem() != null && purchased.getItem().toLowerCase().contains("subscription")) {
                log.info("{\"event\":\"PAYMENT_SKIPPED\", \"type\":\"SUBSCRIPTION\", \"userId\":\"{}\"}", 
                    purchased.getUserId());
                return;
            }
            
            Point point = pointsService.addPointForPayment(purchased.getUserId(), purchased.getAmount());
            log.info("{\"event\":\"POINTS_ADDED\", \"type\":\"PAYMENT\", \"userId\":\"{}\", \"amount\":{}, \"totalPoints\":{}}", 
                purchased.getUserId(), purchased.getAmount(), point.getTotalPoint());
                
        } catch (Exception e) {
            log.error("{\"event\":\"POINTS_ADD_FAILED\", \"type\":\"PAYMENT\", \"userId\":\"{}\", \"amount\":{}, \"error\":\"{}\"}", 
                purchased.getUserId(), purchased.getAmount(), e.getMessage());
        }
    }

    // 책 구매 이벤트 수신 (포인트 사용)
    @StreamListener(KafkaProcessor.BOOK_INPUT)
    public void whenBookPurchased_then_CREATE(@Payload BookPurchased bookPurchased) {
        log.info("{\"event\":\"BOOK_PURCHASE_RECEIVED\", \"userId\":\"{}\", \"bookId\":\"{}\", \"point\":{}, \"title\":\"{}\"}", 
            bookPurchased.getUserId(), bookPurchased.getBookId(), 
            bookPurchased.getPoint(), bookPurchased.getTitle());
            
        try {
            // 포인트 사용이 0인 경우 (구독자) 처리하지 않음
            if (bookPurchased.getPoint() == null || bookPurchased.getPoint() == 0) {
                log.info("{\"event\":\"POINTS_USAGE_SKIPPED\", \"type\":\"SUBSCRIPTION\", \"userId\":\"{}\", \"bookId\":\"{}\"}", 
                    bookPurchased.getUserId(), bookPurchased.getBookId());
                return;
            }
            
            Point point = pointsService.usePointForBook(bookPurchased.getUserId(), bookPurchased.getPoint());
            log.info("{\"event\":\"POINTS_USED\", \"userId\":\"{}\", \"bookId\":\"{}\", \"usedPoints\":{}, \"remainingPoints\":{}, \"title\":\"{}\"}", 
                bookPurchased.getUserId(), bookPurchased.getBookId(), 
                bookPurchased.getPoint(), point.getTotalPoint(), bookPurchased.getTitle());
                
        } catch (Exception e) {
            log.error("{\"event\":\"POINTS_USAGE_FAILED\", \"userId\":\"{}\", \"bookId\":\"{}\", \"point\":{}, \"error\":\"{}\"}", 
                bookPurchased.getUserId(), bookPurchased.getBookId(), 
                bookPurchased.getPoint(), e.getMessage());
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
