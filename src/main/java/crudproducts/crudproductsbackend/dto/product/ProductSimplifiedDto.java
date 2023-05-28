package hr.hrproduct.dto.product;


import hr.hrproduct.dto.CategoryDto;
import hr.hrproduct.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductSimplifiedDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String createdBy;
    private CategoryDto categoryDto;

}
