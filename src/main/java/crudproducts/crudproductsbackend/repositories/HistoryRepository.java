package hr.hrproduct.repositories;

import hr.hrproduct.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("SELECT h FROM History h WHERE h.product.id = ?1")
    Iterable<History> findAllByProductId(Long productId);
}
