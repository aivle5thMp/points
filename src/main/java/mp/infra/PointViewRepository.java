package mp.infra;

import java.util.List;
import java.util.UUID;

import mp.domain.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointViewRepository extends JpaRepository<PointView, UUID> {
    List<PointView> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
