package ua.pp.watermint.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.filter.ChatMessageFilterDto;
import ua.pp.watermint.backend.dto.request.ChatMessageRequestDto;
import ua.pp.watermint.backend.dto.response.ChatMessageResponseDto;
import ua.pp.watermint.backend.entity.ChatMessage;
import ua.pp.watermint.backend.mapper.request.ChatMessageRequestMapper;
import ua.pp.watermint.backend.mapper.response.ChatMessageResponseMapper;
import ua.pp.watermint.backend.repository.ChatContentRepository;
import ua.pp.watermint.backend.repository.ChatMessageRepository;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.service.ChatMessageService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    private final UserAccountRepository userAccountRepository;

    private final ChatContentRepository chatContentRepository;

    private final ChatMessageRequestMapper chatMessageRequestMapper;

    private final ChatMessageResponseMapper chatMessageResponseMapper;

    @Override
    public ChatMessageResponseDto getById(UUID id) {
        return chatMessageRepository.findById(id).map(chatMessageResponseMapper::chatMessageToDto)
                .orElseThrow(() -> new EntityNotFoundException("Chat message with id " + id + " not found!"));
    }

    @Override
    public List<ChatMessageResponseDto> search(ChatMessageFilterDto filter) {
        UUID contentId = filter.getChatContentId();
        if(!chatContentRepository.existsById(contentId))
            throw new EntityNotFoundException("Chat content with id " + contentId + " not found!");
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
                    cb.lessThanOrEqualTo(root.get("createTime"), filter.getTo()));

        return chatMessageRepository.findAll(spec)
                .stream()
                .map(chatMessageResponseMapper::chatMessageToDto)
                .toList();
    }

    @Override
    public ChatMessageResponseDto create(ChatMessageRequestDto chatMessage) {
        return chatMessageResponseMapper.chatMessageToDto(
                chatMessageRepository.save(chatMessageRequestMapper.dtoToChatMessage(chatMessage)));
    }

    @Override
    public ChatMessageResponseDto update(UUID id, ChatMessageRequestDto chatMessage) {
        ChatMessage message = chatMessageRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Chat message with id " + id + " not found!"));
        message.setText(chatMessage.getText());
        message.setWasUpdated(true);
        return chatMessageResponseMapper.chatMessageToDto(chatMessageRepository.saveAndFlush(message));
    }

    @Override
    public void delete(UUID id) {
        if(!chatMessageRepository.existsById(id))
            throw new EntityNotFoundException("Chat message with id " + id + " not found!");
        chatMessageRepository.deleteById(id);
    }
}
