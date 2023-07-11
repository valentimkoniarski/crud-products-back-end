package crudproducts.crudproductsbackend.dto.product;


import crudproducts.crudproductsbackend.dto.CategoryDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSimplifiedDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String createdBy;
    private CategoryDto categoryDto;

}
