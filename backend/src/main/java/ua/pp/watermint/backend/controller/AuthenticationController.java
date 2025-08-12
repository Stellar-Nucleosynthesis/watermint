package ua.pp.watermint.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.pp.watermint.backend.dto.request.AuthRequestDto;
import ua.pp.watermint.backend.dto.response.AuthResponseDto;
import ua.pp.watermint.backend.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Validated AuthRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
