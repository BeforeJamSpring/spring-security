package beforejam.springsecurity.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${SECRETKEY}")
    private String secretKey;

    @Bean
    public JwtProvider jwtProvider(){
        return new JwtProvider(secretKey);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                // CSRF 비활성화(JWT를 사용할 것이므로 불필요)
                .csrf(AbstractHttpConfigurer::disable)

                // H2 콘솔 접근을 위해 X-Frame-Options 비활성화
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                // 세션 관리 상태를 Stateless로 설정 (JWT 사용을 위함)
                .sessionManagement(seesion -> seesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Form Login 및 Http Basic 인증 비활성화 (커스텀 필터 사용 예정)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 예외처리 설정
                .exceptionHandling(e -> e.authenticationEntryPoint(((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED , "Unauthorized"))))

                // 권한 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public" , "/h2-consle/**").permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider()), UsernamePasswordAuthenticationFilter.class);;

        return httpSecurity.build();
    }
}
