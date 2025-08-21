package ua.pp.watermint.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.filter.ChatEventFilterDto;
import ua.pp.watermint.backend.dto.request.ChatEventRequestDto;
import ua.pp.watermint.backend.dto.response.ChatEventResponseDto;
import ua.pp.watermint.backend.entity.ChatEvent;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.mapper.request.ChatEventRequestMapper;
import ua.pp.watermint.backend.mapper.response.ChatEventResponseMapper;
import ua.pp.watermint.backend.notifier.ChatEventNotifier;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.ChatEventRepository;
import ua.pp.watermint.backend.security.service.AuthorizationService;
import ua.pp.watermint.backend.service.ChatContentAccessRegistry;
import ua.pp.watermint.backend.service.ChatEventService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatEventServiceImpl implements ChatEventService {
    private final ChatEventRepository chatEventRepository;
    private final ChatContentRepository chatContentRepository;
    private final ChatEventResponseMapper chatEventResponseMapper;
    private final ChatEventRequestMapper chatEventRequestMapper;
    private final AuthorizationService authorizationService;
    private final ChatContentAccessRegistry accessRegistry;
    private final ChatEventNotifier notifier;

    @Override
    public ChatEventResponseDto getById(UUID id) {
        ChatEvent event = chatEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chat event with id " + id + " not found!"));
        checkChatContentAccess(event.getChatContent().getId());
        return chatEventResponseMapper.chatEventToDto(event);
    }

    @Override
    public ChatEventResponseDto create(ChatEventRequestDto dto) {
        checkChatContentAccess(dto.getChatContentId());
        ChatEventResponseDto response = chatEventResponseMapper.chatEventToDto(
                chatEventRepository.save(chatEventRequestMapper.dtoToChatEvent(dto)));
        notifier.broadcastCreate(response);
        return response;
    }

    @Override
    public Page<ChatEventResponseDto> search(ChatEventFilterDto filter, Pageable pageable) {
        if(!chatContentRepository.existsById(filter.getChatContentId()))
            throw new EntityNotFoundException("Chat content with id " + filter.getChatContentId() + " not found!");
        checkChatContentAccess(filter.getChatContentId());

        Specification<ChatEvent> spec = (root, query, cb) ->
                cb.conjunction();
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("chatContent").get("id"), filter.getChatContentId()));
        if(filter.getText() != null && !filter.getText().isEmpty())
            spec  = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("text")), "%" + filter.getText().toLowerCase() + "%"));
        if(filter.getFrom() != null)
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createTime"), filter.getFrom()));
        if(filter.getTo() != null)
            spec = spec.and((root, query, cb) ->
                    cb.lessThan(root.get("createTime"), filter.getTo()));

        return chatEventRepository.findAll(spec, pageable).map(chatEventResponseMapper::chatEventToDto);
    }

    private void checkChatContentAccess(UUID chatContentId){
        UserAccount currentUser = authorizationService.getCurrentUser();
        if(!accessRegistry.hasAccess(currentUser.getId(), chatContentId))
            throw new AccessDeniedException("User " + currentUser.getUsername() + " has no access to the chat!");
    }
}
