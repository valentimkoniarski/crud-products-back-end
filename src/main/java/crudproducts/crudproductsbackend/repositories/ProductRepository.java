package crudproducts.crudproductsbackend.repositories;

import crudproducts.crudproductsbackend.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    Product findByIdAndUserId(Long id, Long userId);

    @Query("SELECT p FROM Product p WHERE p.user.id = :userId AND p.deleted = false")
    Page<Product> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.user.id = :userId AND p.deleted = true order by p.id desc")
    Page<Product> findAllByUserIdDeleted(@Param("userId") Long userId, Pageable pageable);

    @Query("UPDATE Product p SET p.deleted = true WHERE p.id = :id AND p.user.id = :userId")
    @Modifying
    void deleteProductByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT CASE WHEN EXISTS (SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.user.id = :userId) THEN true ELSE false END FROM Category c WHERE c.id = :categoryId")
    boolean existsByCategoryIdAndUserId(@Param("categoryId") Long categoryId, @Param("userId") Long userId);


}
