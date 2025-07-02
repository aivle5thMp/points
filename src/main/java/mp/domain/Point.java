package mp.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mp.PointsApplication;
import mp.domain.PointDecreased;
import mp.domain.PointIncreased;

@Entity
@Getter @Setter
@Table(name = "Point_table")
@Data
//<<< DDD / Aggregate Root
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private UUID userId;

    private Integer point;

    private Integer totalPoint;

    private LocalDateTime createdAt;

    public static PointsRepository repository() {
        PointsRepository pointRepository = PointsApplication.applicationContext.getBean(
            PointsRepository.class
        );
        return pointRepository;
    }

    //<<< Clean Arch / Port Method
    public static void pointIncrease(Purchased purchased) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        PointIncreased pointIncreased = new PointIncreased(point);
        pointIncreased.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(purchased.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            PointIncreased pointIncreased = new PointIncreased(point);
            pointIncreased.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void pointIncrease(Registered registered) {
        repository().findByUserId(registered.getUserId()).ifPresentOrElse(
        point -> {
            // 이미 포인트 계정이 있으면 추가 적립
            point.setPoint(point.getPoint() + registered.getInitialPoint());
            point.setTotalPoint(point.getTotalPoint() + registered.getInitialPoint());
            point.setCreatedAt(LocalDateTime.now());
            repository().save(point);

            PointIncreased event = new PointIncreased(point);
            event.publishAfterCommit();
        },
        () -> {
            // 없으면 신규 생성
            Point point = new Point();
            point.setUserId(registered.getUserId());
            point.setPoint(registered.getInitialPoint());
            point.setTotalPoint(registered.getInitialPoint());
            point.setCreatedAt(LocalDateTime.now());
            repository().save(point);

            PointIncreased event = new PointIncreased(point);
            event.publishAfterCommit();
        }
    );
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void pointDecrease(BookPurchased bookPurchased) {
        repository().findByUserId(bookPurchased.getUserId()).ifPresent(point -> {
        if (point.getPoint() < bookPurchased.getPointUsed()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        point.setPoint(point.getPoint() - bookPurchased.getPointUsed());
        point.setCreatedAt(LocalDateTime.now());
        repository().save(point);

        PointDecreased event = new PointDecreased(point);
        event.publishAfterCommit();
    });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
