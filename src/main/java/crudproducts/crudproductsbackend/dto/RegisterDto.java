package crudproducts.crudproductsbackend.dto;

import crudproducts.crudproductsbackend.entities.UserRole;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
