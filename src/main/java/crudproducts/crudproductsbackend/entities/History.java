package hr.hrproduct.entities;

import hr.hrproduct.enums.ProductStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_products_history")
@Data
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private String responseUser;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private ProductStatusEnum productStatusEnum;


}
