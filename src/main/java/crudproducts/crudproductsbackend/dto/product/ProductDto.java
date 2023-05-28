package crudproducts.crudproductsbackend.dto.product;


import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.dto.ProductImage.ProductImageDto;
import crudproducts.crudproductsbackend.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String createdBy;
    private UserDto userDto;
    private List<ProductImageDto> productImageDto;
    private CategoryDto categoryDto;

}
