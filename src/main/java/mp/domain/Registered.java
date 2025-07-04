package mp.domain;

import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class Registered extends AbstractEvent {

    private UUID id;
    
    public Integer getInitialPoint() {
        return 1000; // 회원가입 시 지급되는 포인트
    }
}
