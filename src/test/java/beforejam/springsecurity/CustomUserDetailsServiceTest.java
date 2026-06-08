package beforejam.springsecurity;

import beforejam.springsecurity.config.CustomUserDetailsService;
import beforejam.springsecurity.domain.User;
import beforejam.springsecurity.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("DB에 존재하는 username으로 조회 시 Spring Security의 UserDetails 객체를 반환한다.")
    void loadUserByUsername_Success() throws Exception{
        // given
        User user = UserFixture.createUser();
        given(userRepository.findByUsername("tester")).willReturn(Optional.of(user));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername("tester");
        // then
        assertThat(userDetails.getUsername()).isEqualTo("tester");
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("NORMAL");

    }

    @Test
    @DisplayName("DB에 존재하지 않는 username으로 조회 시 UsernameNotFoundException 예외를 던진다.")
    void loadUserByUsername_Fail() throws Exception {
        // given
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknownUser"))
        .isInstanceOf(UsernameNotFoundException.class);
    }
}
