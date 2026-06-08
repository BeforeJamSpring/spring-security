package beforejam.springsecurity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

   private User(String username , String password, String role){
       this.username = username;
       this.password = password;
       this.role = role;
   }

   public static User of(UserDto userDto, PasswordEncoder encoder){
       User user = new User();
       user.username = userDto.username();
       user.password = encoder.encode(userDto.password());
       user.role = "NORMAL";
       return user;
   }
}
