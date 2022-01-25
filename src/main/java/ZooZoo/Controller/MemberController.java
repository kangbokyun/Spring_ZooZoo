package ZooZoo.Controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    // 시작 - 메인화면
    @GetMapping("/")
    public String goToMain() {return "main";}
    // 로그인화면으로
    @GetMapping("/Member/Login")
    public String goToLogin() {return "Member/Login";}
    // 회원가입으로
    @GetMapping("/Member/SignUp")
    public String goToSignUp() {return "Member/SignUp";}
}
