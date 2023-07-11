package crudproducts.crudproductsbackend.dto.product;


import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.dto.UserDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String createdBy;
    private UserDto userDto;
    private CategoryDto categoryDto;

}
