package ua.pp.watermint.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.service.UserAccountService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/user-account")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userAccountService.getById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserAccountResponseDto> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userAccountService.getByUsername(username));
    }

    @PostMapping
    public ResponseEntity<UserAccountResponseDto> create(
            @RequestBody @Validated UserAccountRequestDto dto) {
        UserAccountResponseDto response = userAccountService.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserAccountResponseDto> update(
            @PathVariable UUID id,
            @RequestBody UserAccountRequestDto dto) {
        return ResponseEntity.ok(userAccountService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}