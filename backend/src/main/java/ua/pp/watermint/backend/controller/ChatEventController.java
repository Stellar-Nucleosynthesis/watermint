package ua.pp.watermint.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.pp.watermint.backend.dto.filter.ChatEventFilterDto;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.service.ChatEventService;

import java.util.UUID;

@RestController
@RequestMapping("/chat-event")
@RequiredArgsConstructor
public class ChatEventController {
    private final ChatEventService chatEventService;

    @GetMapping("/{id}")
    public ResponseEntity<ChatEventResponseDto> getById(@PathVariable UUID id){
        return ResponseEntity.ok(chatEventService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ChatEventResponseDto>> search(
            @Validated ChatEventFilterDto filter,
            Pageable pageable) {
        return ResponseEntity.ok(chatEventService.search(filter, pageable));
    }
}
