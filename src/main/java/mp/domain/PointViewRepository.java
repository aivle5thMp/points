package mp.domain;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "pointViews", path = "pointViews")
public interface PointViewRepository extends JpaRepository<PointView, UUID> {
} 