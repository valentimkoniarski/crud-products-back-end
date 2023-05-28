package hr.hrproduct.repositories;

import hr.hrproduct.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("SELECT pi FROM ProductImage pi WHERE pi.id = :idProductImage AND pi.user.id = :userId")
    ProductImage findByIdAndUserId(@Param("idProductImage") Long idProductImage, @Param("userId") Long userId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.user.id = :userId")
    List<ProductImage> findAllByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.principal = true AND pi.product.id = ?1")
    ProductImage findByPrincipalTrueAndProductId(Long productId);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query("UPDATE ProductImage pi SET pi.principal = CASE WHEN pi.id = :id THEN true ELSE false END WHERE pi.product.id = :productId AND pi.user.id = :userId")
    void setPrincipal(Long productId, Long id, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.id = :idProductImage AND pi.user.id = :userId")
    void deleteByIdAndUserId(@Param("idProductImage") Long idProductImage, @Param("userId") Long userId);


}
