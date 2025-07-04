package mp.domain;

import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookPurchased extends AbstractEvent {

    private Long id;
    private UUID userId;
    private UUID bookId;
    private Integer point;
    private String title;        // 도서 제목
    private String authorName;   // 작가 이름
    private String category;     // 카테고리
    private String imageUrl;     // 표지 이미지 URL
}
