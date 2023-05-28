package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.auth.TokenService;
import crudproducts.crudproductsbackend.auth.TokenUtils;
import crudproducts.crudproductsbackend.auth.registration.token.ConfirmationToken;
import crudproducts.crudproductsbackend.auth.registration.token.ConfirmationTokenRepository;
import crudproducts.crudproductsbackend.auth.registration.token.ConfirmationTokenService;
import crudproducts.crudproductsbackend.dto.RegisterDto;
import crudproducts.crudproductsbackend.entities.User;
import crudproducts.crudproductsbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user with email %s not found";
    private final UserRepository userRepository;

    private final TokenService tokenService;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND)));
    }

    public User register(RegisterDto user) {
        String hash = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        User user1 = new User();
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setUserRole(user.getUserRole());
        return userRepository.save(user1);
    }

    public User getUserByToken(HttpServletRequest request) {
        Long idUser = getIdUserByToken(request);
        User user = userRepository.getOne(idUser);
        return user;
    }

    public Long getIdUserByToken(HttpServletRequest request) {
        String token = TokenUtils.getToken(request);
        return tokenService.getIdUser(token);
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail())
                .isPresent();


        if (userExists) {
            // TODO Refatorar isso, colocar exeptions
            var confirmation = confirmationTokenRepository.findByUserId(
                    userRepository.findByEmail(user.getEmail()).get().getId()).get();

            if (confirmation.getConfirmedAt() != null) {
                throw new IllegalStateException("email already taken");
            }
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(2),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }


}