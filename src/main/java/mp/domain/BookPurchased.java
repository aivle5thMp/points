package mp.domain;

import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class BookPurchased extends AbstractEvent {

    private Long id;
    private String userId;
    private String bookId;
}
