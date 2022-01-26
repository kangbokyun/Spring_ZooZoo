package ZooZoo.Domain.DTO;

import ZooZoo.Domain.Entity.MemberEntity;
import lombok.*;

@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class CompanyDTO {
    private int cmno;
    //사업자 등록번호
    private String cmcompanyno;
    //담당자 이름
    private String cmmanagername;
    //기업 이름
    private String cmname;
    //개업 일자
    private String cmdate;
    //공공 데이터 진위여부 api 확인하려면 사업자등록번호, 대표자 이름, 개업일자 필수

    private String cmaddress;
    private String cmemail;
    private String cmphone;


}
