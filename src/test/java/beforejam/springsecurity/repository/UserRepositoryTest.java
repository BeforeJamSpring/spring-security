package beforejam.springsecurity.repository;

import beforejam.springsecurity.UserFixture;
import beforejam.springsecurity.domain.User;
import beforejam.springsecurity.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 정보를 DB에 저장하고 username으로 조회할 수 있다")
    void saveAndFindByUsername() throws Exception {
        // given
        User user = UserFixture.createUser();
        userRepository.save(user);
        // when

        Optional<User> found = userRepository.findByUsername(user.getUsername());
        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("tester");
        assertThat(found.get().getRole()).isEqualTo("NORMAL");
    }
}
