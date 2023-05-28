package hr.hrproduct.dto;

import hr.hrproduct.enums.ProductStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HistoryDto {
    private Long id;
    private String responseUser;
    private LocalDateTime date;
    private ProductStatusEnum productStatusEnum;
}
