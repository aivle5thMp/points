package mp.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "points", path = "points")
public interface PointsRepository extends JpaRepository<Point, Long> {
    
    /**
     * 사용자의 최신 포인트 이력 조회 (가장 최근 생성된) - 안전한 단일 결과 보장
     */
    Optional<Point> findFirstByUserIdOrderByCreatedAtDescIdDesc(UUID userId);

    /**
     * 사용자의 포인트 이력 조회 (페이징, 최신순)
     */
    @Query("SELECT p FROM Point p WHERE p.userId = :userId ORDER BY p.createdAt DESC, p.id DESC")
    Page<Point> findHistoryByUserId(@Param("userId") UUID userId, Pageable pageable);

    /**
     * 사용자의 특정 사유별 포인트 이력 조회 (페이징, 최신순)
     */
    Page<Point> findByUserIdAndReasonOrderByCreatedAtDesc(UUID userId, String reason, Pageable pageable);
}
