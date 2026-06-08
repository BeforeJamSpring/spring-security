package beforejam.springsecurity.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class controller {
    @GetMapping("/public")
    public String publicApi(){
        return "Public Area";
    }

    @GetMapping("/private")
    public String privateApi(){
        return "Private Area";
    }
}
