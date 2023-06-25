package crudproducts.crudproductsbackend.repositories;

import crudproducts.crudproductsbackend.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("SELECT pi FROM ProductImage pi WHERE pi.id = :idProductImage AND pi.user.id = :userId")
    ProductImage findByIdAndUserId(@Param("idProductImage") final Long idProductImage, @Param("userId") final Long userId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.user.id = :userId")
    List<ProductImage> findAllByProductIdAndUserId(@Param("productId") final Long productId, @Param("userId") final Long userId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.principal = true AND pi.product.id = ?1")
    ProductImage findPrincipalTrueByProductId(final Long productId);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query("UPDATE ProductImage pi SET pi.principal = CASE WHEN pi.id = :id THEN true ELSE false END WHERE pi.product.id = :productId AND pi.user.id = :userId")
    void setPrincipal(final Long productId, Long id, final Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.id = :idProductImage AND pi.user.id = :userId")
    void deleteByIdAndUserId(@Param("idProductImage") final Long idProductImage, @Param("userId") final  Long userId);


}
