package ua.pp.watermint.backend.mapper.request;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.entity.PrivateChat;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.util.TestDatabaseInitializer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({PrivateChatRequestMapper.class, TestDatabaseInitializer.class})
public class PrivateChatRequestMapperIT {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PrivateChatRequestMapper privateChatRequestMapper;

    @Test
    void dtoToChatMessage_withValidDto_returnsEntity(){
        List<UserAccount> users = userAccountRepository.findAll();
        PrivateChatRequestDto request = PrivateChatRequestDto.builder()
                .userAccount1Id(users.getFirst().getId())
                .userAccount2Id(users.getLast().getId())
                .build();

        PrivateChat entity = privateChatRequestMapper.dtoToPrivateChat(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getUserAccount1()).isEqualTo(users.getFirst());
        assertThat(entity.getUserAccount2()).isEqualTo(users.getLast());
        assertThat(entity.getChatContent()).isNotNull();
    }

    @Test
    void dtoToChatMessage_withNonExistentUserAccountId_throwsEntityNotFoundException(){
        UUID userAccountId = userAccountRepository.findAll().getFirst().getId();
        assertThrows(EntityNotFoundException.class, () ->
                privateChatRequestMapper.dtoToPrivateChat(
                        PrivateChatRequestDto.builder()
                                .userAccount1Id(userAccountId)
                                .userAccount2Id(UUID.randomUUID())
                                .build()
                )
        );
    }
}
