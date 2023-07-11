package crudproducts.crudproductsbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;

    @JsonIgnore
    private UserDto userDto;
}
