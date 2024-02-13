package by.arvisit.modsenlibapp.libraryservice.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthService {
    
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
