package crudproducts.crudproductsbackend.repositories;

import crudproducts.crudproductsbackend.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    Iterable<Category> findAllByUserId(Long idUserByToken);

}
