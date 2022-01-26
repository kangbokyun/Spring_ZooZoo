package ZooZoo.Controller;

import ZooZoo.Domain.DTO.MemberDTO;
import ZooZoo.Service.MemberService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;

    // 시작 - 메인화면
    /*@GetMapping("/")
    public String goToMain() {return "Main";}*/
    // 로그인화면으로
    @GetMapping("/Member/Login")
    public String goToLogin() {return "Member/Login";}
    // 일반 회원가입으로
    @GetMapping("/Member/SignUp")
    public String goToSignUp() {return "Member/SignUp";}
    // 기업 회원가입으로
    @GetMapping("/Member/CompanySignUp")
    public String goToCompanySignUp() {return "Member/CompanySignUp";}
    // 아이디 찾기로
    @GetMapping("/Member/FindId")
    public String goToFindId() {return "Member/FindId";}
    // 비밀번호 찾기로
    @GetMapping("/Member/FindPw")
    public String goToFindPw() {return "Member/FindPw";}

    // 회원가입
    @PostMapping("/Member/SignUpController") @ResponseBody
    public String SignUp(MemberDTO memberDTO) {
        System.out.println(memberDTO.getMid());
        boolean result = memberService.SignUp(memberDTO);
        if(result) {
            return "1";
        } else {
            return "2";
        }
    }
}
