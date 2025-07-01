package mp.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import mp.config.kafka.KafkaProcessor;
import mp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PointViewViewHandler {

    // <<< DDD / CQRS
    @Autowired
    private PointViewRepository pointViewRepository;

    //@StreamListener(KafkaProcessor.INPUT)
    @StreamListener("event-in")
    public void whenPointIncreased_then_UPDATE_1(
            @Payload PointIncreased pointIncreased) {
        try {
            if (!pointIncreased.validate())
                return;

            List<PointView> pointViewList = pointViewRepository.findByUserIdOrderByCreatedAtDesc(
                    pointIncreased.getUserId());
            for (PointView pointView : pointViewList) {
                pointView.setPointStatus("CHARGED");
                pointViewRepository.save(pointView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@StreamListener(KafkaProcessor.INPUT)
    @StreamListener("event-in")
    public void whenPointDecreased_then_UPDATE_2(
            @Payload PointDecreased pointDecreased) {
        try {
            if (!pointDecreased.validate())
                return;

            List<PointView> pointViewList = pointViewRepository.findByUserIdOrderByCreatedAtDesc(
                    pointDecreased.getUserId());
            for (PointView pointView : pointViewList) {
                pointView.setPointStatus("USED");
                pointViewRepository.save(pointView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
