package beforejam.springsecurity;

import beforejam.springsecurity.domain.User;
import beforejam.springsecurity.domain.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFixture {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();


    public static UserDto createUserDto(){
        return new UserDto("tester" , "1234");
    }

    public static User createUser(){
        return User.of(createUserDto() , PASSWORD_ENCODER);
    }
}
