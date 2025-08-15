package ua.pp.watermint.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.filter.ChatMessageFilterDto;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.entity.ChatMessage;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.mapper.request.ChatMessageRequestMapper;
import ua.pp.watermint.backend.mapper.response.ChatMessageResponseMapper;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.ChatMessageRepository;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.security.service.AuthorizationService;
import ua.pp.watermint.backend.service.ChatContentAccessRegistry;
import ua.pp.watermint.backend.service.ChatMessageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserAccountRepository userAccountRepository;
    private final ChatContentRepository chatContentRepository;
    private final ChatMessageRequestMapper chatMessageRequestMapper;
    private final ChatMessageResponseMapper chatMessageResponseMapper;
    private final AuthorizationService authorizationService;
    private final ChatContentAccessRegistry accessRegistry;

    @Override
    public ChatMessageResponseDto getById(UUID id) {
        ChatMessage message = chatMessageRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Chat message with id " + id + " not found!"));
        checkChatContentAccess(message.getChatContent().getId());
        return chatMessageResponseMapper.chatMessageToDto(message);
    }

    @Override
    public Page<ChatMessageResponseDto> search(ChatMessageFilterDto filter, Pageable pageable) {
        UUID contentId = filter.getChatContentId();
        if(!chatContentRepository.existsById(contentId))
            throw new EntityNotFoundException("Chat content with id " + contentId + " not found!");
        checkChatContentAccess(contentId);
        UUID userId = filter.getUserAccountId();
        if(userId != null && !userAccountRepository.existsById(userId))
            throw new EntityNotFoundException("User account with id " + userId + " not found!");

        Specification<ChatMessage> spec = (root, query, cb) ->
                cb.conjunction();
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("chatContent").get("id"), contentId));
        if(userId != null)
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("userAccount").get("id"), userId));
        if(filter.getText() != null && !filter.getText().isEmpty())
            spec  = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("text")), "%" + filter.getText().toLowerCase() + "%"));
        if(filter.getFrom() != null)
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createTime"), filter.getFrom()));
        if(filter.getTo() != null)
            spec = spec.and((root, query, cb) ->
                    cb.lessThan(root.get("createTime"), filter.getTo()));

        return chatMessageRepository.findAll(spec, pageable).map(chatMessageResponseMapper::chatMessageToDto);
    }

    @Override
    public ChatMessageResponseDto create(ChatMessageRequestDto dto) {
        UUID contentId = dto.getChatContentId();
        if(!chatContentRepository.existsById(contentId))
            throw new EntityNotFoundException("Chat content with id " + contentId + " not found!");
        checkChatContentAccess(contentId);
        UserAccount currentUser = authorizationService.getCurrentUser();
        if(!dto.getUserAccountId().equals(currentUser.getId()))
            throw new AccessDeniedException("Access denied: tried to save a message as another account!");
        return chatMessageResponseMapper.chatMessageToDto(
                chatMessageRepository.save(chatMessageRequestMapper.dtoToChatMessage(dto)));
    }

    @Override
    public ChatMessageResponseDto update(UUID id, ChatMessageRequestDto dto) {
        ChatMessage message = chatMessageRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Chat message with id " + id + " not found!"));
        checkChatContentAccess(message.getChatContent().getId());
        UserAccount currentUser = authorizationService.getCurrentUser();
        if(!message.getUserAccount().equals(currentUser))
            throw new AccessDeniedException("Access denied: tried to update a message as another account!");
        message.setText(dto.getText());
        message.setWasUpdated(true);
        return chatMessageResponseMapper.chatMessageToDto(chatMessageRepository.saveAndFlush(message));
    }

    @Override
    public void delete(UUID id) {
        ChatMessage message = chatMessageRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Chat message with id " + id + " not found!"));
        checkChatContentAccess(message.getChatContent().getId());
        UserAccount currentUser = authorizationService.getCurrentUser();
        if(!message.getUserAccount().equals(currentUser))
            throw new AccessDeniedException("Access denied: tried to delete a message as another account!");
        chatMessageRepository.deleteById(id);
    }

    private void checkChatContentAccess(UUID chatContentId){
        UserAccount currentUser = authorizationService.getCurrentUser();
        if(!accessRegistry.hasAccess(currentUser.getId(), chatContentId))
            throw new AccessDeniedException("User " + currentUser.getUsername() + " has no access to the chat!");
    }
}
