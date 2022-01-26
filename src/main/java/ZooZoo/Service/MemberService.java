package ZooZoo.Service;

import ZooZoo.Domain.DTO.MemberDTO;
import ZooZoo.Domain.Entity.MemberEntity;
import ZooZoo.Domain.Entity.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    // 회원가입
    public boolean SignUp(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.builder()
                .mid(memberDTO.getMid())
                .mpw(memberDTO.getMpw())
                .mname(memberDTO.getMname())
                .memail(memberDTO.getMemail())
                .mbirth(memberDTO.getMbirth())
                .maddress(memberDTO.getMaddress())
                .build();
        memberRepository.save(memberEntity);
        return true;
    }

    public String FindId(String memail, String mpw) {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for(MemberEntity temp : memberEntities){
            if(temp.getMemail().equals(memail) && temp.getMpw().equals(mpw)){
                return temp.getMid();
            }
        }
        return null;
    }
}
