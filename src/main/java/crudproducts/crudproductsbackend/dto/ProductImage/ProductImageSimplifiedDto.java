package crudproducts.crudproductsbackend.dto.ProductImage;

import crudproducts.crudproductsbackend.dto.product.ProductDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageSimplifiedDto {
    private Long id;
    private String name;
    private String type;
    private boolean isPrincipal;
    private String url;
}
