package mp.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import mp.PointsApplication;
import mp.domain.PointDecreased;
import mp.domain.PointIncreased;

@Entity
@Table(name = "Point_table")
@Data
//<<< DDD / Aggregate Root
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userId;

    private Double point;

    private Double totalPoint;

    private Date createdAt;

    public static PointRepository repository() {
        PointRepository pointRepository = PointsApplication.applicationContext.getBean(
            PointRepository.class
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
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        PointIncreased pointIncreased = new PointIncreased(point);
        pointIncreased.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(registered.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            PointIncreased pointIncreased = new PointIncreased(point);
            pointIncreased.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void pointDecrease(BookPurchased bookPurchased) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        PointDecreased pointDecreased = new PointDecreased(point);
        pointDecreased.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(bookPurchased.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            PointDecreased pointDecreased = new PointDecreased(point);
            pointDecreased.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
