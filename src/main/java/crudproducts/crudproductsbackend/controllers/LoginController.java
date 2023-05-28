package hr.hrproduct.controllers;

import hr.hrproduct.auth.TokenDto;
import hr.hrproduct.auth.TokenService;
import hr.hrproduct.dto.LoginDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/login")
@AllArgsConstructor
public class LoginController {

    private AuthenticationManager authManager;
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> authentication(@RequestBody @Valid final LoginDto loginDto) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken loginData = loginDto.convert();
        final Authentication authentication = authManager.authenticate(loginData);
        final String token = tokenService.createToken(authentication);
        return ResponseEntity.ok(new TokenDto(token, "Bearer"));
    }
}
