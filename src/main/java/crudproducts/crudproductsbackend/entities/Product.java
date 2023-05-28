package crudproducts.crudproductsbackend.entities;

import crudproducts.crudproductsbackend.listener.HistoryListener;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_products")
@Data
@Getter
@EntityListeners(HistoryListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<ProductImage> productImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    private String name;

    private String description;

    private BigDecimal price;

    private String createdBy;

    @Column(name = "is_deleted")
    private Boolean deleted = false;


}
