package mp.config.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaProcessor {
    String PAYMENT_INPUT = "payment-in";
    String SIGNUP_INPUT = "signup-in";
    String BOOK_INPUT = "book-in";

    @Input(PAYMENT_INPUT)
    SubscribableChannel paymentTopic();

    @Input(SIGNUP_INPUT)
    SubscribableChannel signupTopic();

    @Input(BOOK_INPUT)
    SubscribableChannel bookTopic();
}
