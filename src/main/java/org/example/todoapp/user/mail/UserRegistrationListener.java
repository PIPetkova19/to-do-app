package org.example.todoapp.user.mail;

import jakarta.mail.MessagingException;
import org.example.todoapp.user.event.UserRegistrationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationListener {

    private final MailService mailService;

    public UserRegistrationListener(MailService mailService) {
        this.mailService = mailService;
    }

    @EventListener
    public void handleUserRegistration(UserRegistrationEvent event) {
        String html = "<h1>Welcome to To-Do-App!</h1><p>Keep track of your day using our app :)</p>";
        try {
            mailService.sendHtml(event.email(), "To-Do-App", html);
        } catch (MessagingException e) {
            //logger?
            System.out.println(e.getMessage());
        }
    }
}