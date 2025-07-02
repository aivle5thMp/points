package mp.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "points", path = "points")
public interface PointsRepository
    extends JpaRepository<Point, Long> {
        Optional<Point> findByUserId(UUID userId);
    }
