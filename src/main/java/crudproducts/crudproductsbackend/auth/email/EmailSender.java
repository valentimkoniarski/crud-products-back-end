package hr.hrproduct.auth.email;

import javax.mail.MessagingException;

public interface EmailSender {
    void send(String to, String email) throws MessagingException;
}
