package beforejam.springsecurity.controller;

import beforejam.springsecurity.config.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("인증 없이 /api/public 에 접근하면 200 OK를 반환한다")
    void publicApi_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/public"))
                .andExpect(status().isOk())
                .andExpect(content().string("Public Area"));
    }

    @Test
    @DisplayName("인증 없이 /api/private 에 접근하면 403 Unauthorized를 반환한다.")
    void privateApi_WithoutAuth_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/private"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @DisplayName("유효한 JWT 토큰을 Header에 담아 /api/private 에 접근하면 200OK를 반환한다.")
    void privateApi_WithValidToken_ShouldReturn200() throws Exception {
        // given
        String token = jwtProvider.createToken("tester", "NORMAL");

        // when
        // then
        mockMvc.perform(get("/api/private")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Private Area"));

    }
}
