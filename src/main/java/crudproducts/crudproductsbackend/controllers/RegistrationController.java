package crudproducts.crudproductsbackend.controllers;

import crudproducts.crudproductsbackend.auth.registration.RegistrationRequest;
import crudproducts.crudproductsbackend.auth.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;

@RestController
@RequestMapping(path = "registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Value("${url.registration.redirect}")
    private String URL_REGISTRATION_REDIRECT;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public String register(final @RequestBody RegistrationRequest request) throws MessagingException {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public RedirectView confirm(final @RequestParam("token") String token) {
        registrationService.confirmToken(token);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(URL_REGISTRATION_REDIRECT);

        return redirectView;
    }

}
