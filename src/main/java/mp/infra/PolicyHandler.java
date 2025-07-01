package mp.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import mp.config.kafka.KafkaProcessor;
import mp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
public class PolicyHandler {

    @Autowired
    PointViewRepository pointViewRepository;

    @StreamListener(value = "event-in", condition = "headers['type']=='PointIncreased'")
    public void whenPointIncreased_then_CREATE(PointIncreased event) {
        PointView pointView = PointView.builder()
                .id(UUID.randomUUID())
                .userId(event.getUserId())
                .point(event.getPoint())
                .totalPoint(event.getTotalPoint())
                .pointStatus("CHARGED")
                .createdAt(event.getCreatedAt())
                .build();
        pointViewRepository.save(pointView);
    }

    @StreamListener(value = "event-in", condition = "headers['type']=='PointDecreased'")
    public void whenPointDecreased_then_CREATE(PointDecreased event) {
        PointView pointView = PointView.builder()
                .id(UUID.randomUUID())
                .userId(event.getUserId())
                .point(event.getPoint())
                .totalPoint(event.getTotalPoint())
                .pointStatus("USED")
                .createdAt(event.getCreatedAt())
                .build();
        pointViewRepository.save(pointView);
    }
}
//>>> Clean Arch / Inbound Adaptor
