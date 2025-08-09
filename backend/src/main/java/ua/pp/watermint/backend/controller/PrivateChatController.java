package ua.pp.watermint.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.pp.watermint.backend.dto.filter.PrivateChatFilterDto;
import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.service.PrivateChatService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/private-chat")
@RequiredArgsConstructor
public class PrivateChatController {
    private final PrivateChatService privateChatService;

    @GetMapping("/{id}")
    public ResponseEntity<PrivateChatResponseDto> getById(@PathVariable UUID id){
        return ResponseEntity.ok(privateChatService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PrivateChatResponseDto>> search(@Validated @RequestBody PrivateChatFilterDto filter){
        return ResponseEntity.ok(privateChatService.search(filter));
    }

    @PostMapping
    public ResponseEntity<PrivateChatResponseDto> create(@Validated @RequestBody PrivateChatRequestDto dto){
        PrivateChatResponseDto response = privateChatService.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        privateChatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
