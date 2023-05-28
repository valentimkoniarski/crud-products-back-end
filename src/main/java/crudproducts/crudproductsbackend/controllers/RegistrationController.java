package crudproducts.crudproductsbackend.controllers;

import crudproducts.crudproductsbackend.auth.registration.RegistrationRequest;
import crudproducts.crudproductsbackend.auth.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(path = "registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(final @RequestBody RegistrationRequest request) throws MessagingException {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(final @RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
