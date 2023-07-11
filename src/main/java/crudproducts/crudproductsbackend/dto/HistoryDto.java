package crudproducts.crudproductsbackend.dto;

import crudproducts.crudproductsbackend.enums.ProductStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDto {
    private Long id;
    private String responseUser;
    private LocalDateTime date;
    private ProductStatusEnum productStatusEnum;
}
