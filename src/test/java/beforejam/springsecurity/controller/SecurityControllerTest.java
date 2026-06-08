package beforejam.springsecurity.controller;

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
}
