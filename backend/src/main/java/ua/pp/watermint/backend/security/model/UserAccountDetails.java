package ua.pp.watermint.backend.security.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.pp.watermint.backend.entity.UserAccount;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserAccountDetails implements UserDetails {
    private final UserAccount account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    public boolean isVerified() {
        return account.getVerified();
    }
}
