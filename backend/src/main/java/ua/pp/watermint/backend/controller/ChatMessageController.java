package ua.pp.watermint.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.pp.watermint.backend.dto.filter.ChatMessageFilterDto;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.service.ChatMessageService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/chat-message")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/{id}")
    public ResponseEntity<ChatMessageResponseDto> getById(@PathVariable UUID id){
        return ResponseEntity.ok(chatMessageService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ChatMessageResponseDto>> search(
            @Validated ChatMessageFilterDto filter,
            Pageable pageable){
        return ResponseEntity.ok(chatMessageService.search(filter, pageable));
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponseDto> create(@Validated @RequestBody ChatMessageRequestDto dto){
        ChatMessageResponseDto response = chatMessageService.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChatMessageResponseDto> update(
            @PathVariable UUID id,
            @RequestBody ChatMessageRequestDto request){
        return ResponseEntity.ok(chatMessageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        chatMessageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
