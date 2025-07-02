package mp.infra;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mp.domain.PointsService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointsKafkaConsumer {

    private final PointsService pointsService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @KafkaListener(topics = "user.signup.v1", groupId = "points-service")
    public void handleUserSignup(String message) {
        try {
            UserSignupMessage signupMsg = objectMapper.readValue(message, UserSignupMessage.class);
            log.info("가입 이벤트 수신: {}", signupMsg);
            pointsService.addPointForSignup(
                signupMsg.getUserId());
        } catch (Exception e) {
            log.error("user.signup.v1 메시지 파싱 또는 포인트 적립 실패", e);
        }
    }

    @KafkaListener(topics = "payment.points.v1", groupId = "points-service")
    public void handlePaymentPoints(String message) {
        try {
            PaymentPointsMessage paymentMsg = objectMapper.readValue(message, PaymentPointsMessage.class);
            log.info("포인트 결제 이벤트 수신: {}", paymentMsg);
            pointsService.addPointForPayment(paymentMsg.getUserId(), paymentMsg.getPoint());
        } catch (Exception e) {
            log.error("payment.points.v1 메시지 파싱 또는 포인트 적립 실패", e);
        }
    }

    @KafkaListener(topics = "book.purchased.v1", groupId = "points-service")
    public void handleBookPurchased(String message) {
        try {
            BookPurchasedMessage bookMsg = objectMapper.readValue(message, BookPurchasedMessage.class);
            log.info("책 구매 이벤트 수신: {}", bookMsg);
            pointsService.usePointForBook(bookMsg.getUserId(), bookMsg.getPointUsed());
        } catch (Exception e) {
            log.error("book.purchased.v1 메시지 파싱 또는 포인트 차감 실패", e);
        }
    }
}