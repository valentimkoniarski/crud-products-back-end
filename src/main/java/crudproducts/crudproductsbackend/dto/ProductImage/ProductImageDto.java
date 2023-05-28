package crudproducts.crudproductsbackend.dto.ProductImage;

import crudproducts.crudproductsbackend.dto.product.ProductDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageDto {

    private Long id;
    private String name;
    private String type;

//    private byte[] data;
    private ProductDto productDto;
    private boolean isPrincipal;
    private String url;
}
