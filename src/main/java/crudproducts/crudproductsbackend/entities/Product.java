package crudproducts.crudproductsbackend.entities;

import crudproducts.crudproductsbackend.listener.HistoryListener;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_products")
@Getter
@Setter
@EntityListeners(HistoryListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull(message = "User cannot be null")
    private User user;

    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    @Size(max = 15, min = 5)
    @NotNull(message = "Name cannot be null")
    private String name;

    @Size(max = 80, min = 5)
    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;

    private String createdBy;

    @Column(name = "is_deleted")
    private Boolean deleted = false;
}
