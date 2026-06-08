# 🛡️ Spring Security JWT TDD Project

## 📌 프로젝트 소개
이 프로젝트는 **Spring Security**와 **JWT(JSON Web Token)**를 이용한 인증/인가 서버의 핵심 로직을 **TDD(Test-Driven Development)** 방식으로 구현한 학습용 저장소입니다.

단순히 동작하는 코드를 짜는 것을 넘어, RED(실패) - GREEN(성공) 사이클을 엄격하게 지키며 스프링 시큐리티의 내부 필터 체인 동작 원리와 의존성을 단계별로 파악하는 것을 목적으로 합니다.

## 🛠️ 기술 스택
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security
* **Database:** H2 Database (In-Memory), Spring Data JPA
* **Auth:** JJWT (JSON Web Token)
* **Test:** JUnit 5, Mockito, MockMvc

---

## 🚀 구현 로드맵 및 커밋 히스토리

본 프로젝트는 다음과 같은 커밋 단위로 기능이 덧붙여졌습니다.

### Step 0: 프로젝트 환경 세팅
* `chore: Spring Boot 초기 프로젝트 생성 및 H2, yml 환경 설정`
* 필수 의존성 추가 및 `application.yml`을 통한 H2 인메모리 DB 연동 설정.

### Step 1: 기본 접근 제어 및 Stateless 설정
* `feat: Public/Private 엔드포인트 분리 및 Security Stateless 기본 설정`
* `fix: 인증되지 않은 접근 시 403 대신 401 반환하도록 AuthenticationEntryPoint 설정`
* JWT 도입을 대비한 세션 비활성화(`STATELESS`).
* `/api/public` (접근 허용)과 `/api/private` (인증 요구) 엔드포인트 분리.
* 시큐리티 기본 동작(Form Login)을 끄고, 예외 처리 필터를 통해 미인증 시 401 에러 반환.

### Step 2: Domain & Repository 구현
* `feat: User 엔티티 및 UserRepository 구현과 테스트`
* `DataJpaTest`를 활용한 데이터베이스 저장 및 조회 테스트.
* 데이터베이스 예약어 충돌 방지를 위한 `@Table(name="users")` 매핑.

### Step 3: 인증 로직 연동 (UserDetailsService)
* `test & feat: DB 연동을 위한 CustomUserDetailsService 구현 및 테스트`
* 실제 DB의 유저 정보를 Spring Security가 이해할 수 있는 `UserDetails` 객체로 변환하는 핵심 서비스 구현.
* `Mockito`를 활용한 단위 테스트로 예외 처리 로직(UsernameNotFound) 검증.

### Step 4: JWT 기반 인가 필터 적용 (최종)
* **Step 4-1:** `test & feat: JWT 의존성 추가 및 JwtProvider 발급/검증 로직 구현`
* **Step 4-2:** `feat & test: JWT 인증 필터 구현 및 Security Filter Chain에 등록`
* 서명 및 만료 시간을 포함한 JWT 토큰 생성 및 파싱 유틸리티 구현.
* `JwtAuthenticationFilter`를 `UsernamePasswordAuthenticationFilter` **앞(Before)** 에 배치하여, 컨트롤러에 도달하기 전 Authorization 헤더의 토큰을 낚아채 SecurityContext에 인증 객체를 주입.

---

## ⚙️ 핵심 아키텍처 포인트

1. **Stateless Session:** 서버는 상태(Session)를 유지하지 않으며, 모든 요청은 클라이언트가 제공하는 JWT 토큰의 유효성만으로 평가됩니다.
2. **Filter Chain 순서:** 스프링 시큐리티의 필터는 순서가 중요합니다. 기존 폼 로그인 검사기가 작동하기 전에 커스텀 JWT 필터가 먼저 작동하도록 체인 순서를 조작했습니다.
3. **명시적 예외 처리:** 화면이 없는 REST API 특성에 맞게, 인증 실패 시 기본 제공되는 투박한 로그인 웹페이지 대신 **401 Unauthorized** 상태 코드를 직접 응답하도록 설계했습니다.
