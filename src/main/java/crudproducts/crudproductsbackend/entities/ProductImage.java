package crudproducts.crudproductsbackend.entities;

import crudproducts.crudproductsbackend.listener.HistoryListener;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_products_images")
@Data
@EntityListeners(HistoryListener.class)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String name;

    private String type;

    @Column(name = "is_principal")
    private Boolean principal = false;

    @Column(name = "url")
    private String url;

//    @Lob
//    @Column(name = "imagedata")
//    private byte[] imageData;
}