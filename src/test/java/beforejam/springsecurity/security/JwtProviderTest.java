package beforejam.springsecurity.security;

import beforejam.springsecurity.config.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtProviderTest {
    private final String testKey = "v-e-r-y-s-e-c-r-e-t-k-e-y-f-o-r-t-d-d-s-p-r-i-n-g-b-o-o-t";
    private final JwtProvider provider = new JwtProvider(testKey);

    @Test
    @DisplayName("username과 role을 전달하면 JWT 토큰이 생성되고, 토킁에서 다시 Authentication 객체를 추출할 수 있다.")
    void createAndResolveToken() throws Exception {
        // given
        String username = "tester";
        String role = "NORMAL";

        // when
        String token = provider.createToken(username, role);

        // then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);

        Authentication authentication = provider.getAuthentication(token);

        assertThat(authentication.getName()).isEqualTo(username);
        assertThat(authentication.getAuthorities().iterator().next().getAuthority())
                .isEqualTo(role);
    }

}
