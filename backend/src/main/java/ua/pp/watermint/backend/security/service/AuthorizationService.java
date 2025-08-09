package ua.pp.watermint.backend.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.repository.UserAccountRepository;

import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final UserAccountRepository userAccountRepository;

    public void checkAuthorized(UserAccount account) {
        if (!isAuthorized(account)) {
            throw new AccessDeniedException(String.format(
                    "Access denied: tried to access account '%s' as '%s'!",
                    account != null ? account.getUsername() : null,
                    getCurrentUser().getUsername()
            ));
        }
    }

    public void checkAnyAuthorized(UserAccount... accounts) {
        if (accounts == null || accounts.length == 0 || Arrays.stream(accounts).noneMatch(this::isAuthorized)) {
            throw new AccessDeniedException("Access denied: not authenticated as any of the required users!");
        }
    }

    public boolean isAuthorized(UserAccount account) {
        UserAccount currentUser = getCurrentUser();
        return account != null && currentUser != null && Objects.equals(account, currentUser);
    }

    public UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : null;
        return userAccountRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User account with username " + username + " not found!"));
    }
}
