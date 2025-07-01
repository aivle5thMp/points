package mp.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import mp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("/points")
public class PointController {

    @Autowired
    private PointViewRepository pointViewRepository;

    @GetMapping
    public List<PointHistoryDto> getPointHistory(@RequestParam UUID userId) {
        List<PointView> pointViews = pointViewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return pointViews.stream()
                .map(PointHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }
}
//>>> Clean Arch / Inbound Adaptor
