package ZooZoo.Service.Member;

import ZooZoo.Domain.DTO.Board.BoardDTO;
import ZooZoo.Domain.DTO.Board.ReplyDTO;
import ZooZoo.Domain.DTO.Member.MemberDTO;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.Category.CategoryRepository;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import ZooZoo.Domain.Entity.Reply.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

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

    // 아이디 중복확인
    public boolean Checkid(String mid) {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            if (memberEntity != null && memberEntity.getMid().equals(mid)) {
                return true;
            }
        }
        return false;
    }

    @Autowired
    HttpServletRequest request;

    // 로그인
    public boolean Login(String mid, String mpw) {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            if (memberEntity.getMid().equals(mid) && memberEntity.getMpw().equals(mpw)) {
                MemberDTO memberDTO = MemberDTO.builder().mid(memberEntity.getMid()).mpw(memberEntity.getMpw()).mno(memberEntity.getMno()).build();
                HttpSession session = request.getSession();
                session.setAttribute("loginDTO", memberDTO);
                return true;
            }
        }
        return false;
    }

    // 회원 아이디 찾기
    public String FindId(String memail, String mname) {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for (MemberEntity temp : memberEntities) {
            if (temp.getMemail() != null && temp.getMname() != null && temp.getMemail().equals(memail) && temp.getMname().equals(mname)) {
                return temp.getMid();
            }
        }
        return null;
    }

    // 회원 비밀번호 찾기
    public String findpw(String mid, String memail) {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntities) {
            if (memberEntity.getMid().equals(mid) && memberEntity.getMemail().equals(memail)) {
                return memberEntity.getMpw();
            }
        }
        return null;
    }

    // 회원번호 -> 회원정보 반환
    @Transactional
    public MemberDTO getmemberDto(int mno) {
        // 회원정보 저장 리스트
        Optional<MemberEntity> entityOptional = memberRepository.findById(mno);

        return MemberDTO.builder()
                .mid(entityOptional.get().getMid())
                .mpw(entityOptional.get().getMpw())
                .memail(entityOptional.get().getMemail())
                .mbirth(entityOptional.get().getMbirth())
                .mname(entityOptional.get().getMname())
                .maddress(entityOptional.get().getMaddress())
                .createdDate(entityOptional.get().getCreatedDate())
                .build();
    }

    // 회원탈퇴
    public boolean mdelete(int mno, String passwordconfirm) {
        // 엔티티 가져오기
        Optional<MemberEntity> entityOptional = memberRepository.findById(mno);

        // 비밀번호 일치 여부 확인
        if (entityOptional.get().getMpw().equals(passwordconfirm)) {
            memberRepository.delete(entityOptional.get());
            return true;
        } else {
            return false;
        }
    }

    // 회원수정
    @Transactional
    public boolean mupdate(int mno, String newmname, String newmemail, String newmbirth, String newmaddress) {
        MemberEntity memberEntity = memberRepository.findById(mno).get();
        memberEntity.setMname(newmname);
        memberEntity.setMemail(newmemail);
        memberEntity.setMbirth(newmbirth);
        memberEntity.setMaddress(newmaddress);
        return true;
    }

    // 비밀번호수정
    @Transactional
    public boolean mpwupdate(int mno, String tdmpw, String newmpw) {
        MemberEntity memberEntity = memberRepository.findById(mno).get();
        if (memberEntity.getMpw().equals(tdmpw)) {
            memberEntity.setMpw(newmpw);
            return true;
        } else {
            return false;
        }
    }

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ReplyRepository replyRepository;

    // 작성 게시물 세기
    public int countboard(int mno) {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        int count = 0;
        for (int i = 0; i < boardEntities.size(); i++) {
            if (boardEntities.get(i).getMemberEntity().getMno() == mno && boardEntities.get(i).getCategoryEntity().getCano() == 4) {
                count++;
            }
        }
        return count;
    }

    // 내가 쓴 게시물 가져오기
    public List<BoardEntity> getmyboard(int mno) {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<BoardEntity> getboard = new ArrayList<>();
        List<BoardEntity> totgetboard = new ArrayList<>();
        for (int i = 0; i < boardEntities.size(); i++) {
            if (boardEntities.get(i).getMemberEntity().getMno() == mno) {
                getboard.add(boardEntities.get(i));
            }
        }
        getboard.sort(Comparator.comparing(BoardEntity::getCreatedDate).reversed());
        return getboard;
    }

    @Autowired
    CategoryRepository categoryRepository;

    // 댓글 가져오기
    public List<ReplyDTO> getmyreply(int mno) {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<ReplyEntity> replyEntities = replyRepository.findAll();

        List<ReplyDTO> totalreply = new ArrayList<>();

        System.out.println("확인 : " + boardEntities.get(1).getCreatedDate());

        // 유기, 분양, 병원
        for (int i = 0; i < boardEntities.size(); i++) {
            if (boardEntities.get(i).getMemberEntity().getMno() == mno &&
                    (boardEntities.get(i).getCategoryEntity().getCano() == 1 ||
                            boardEntities.get(i).getCategoryEntity().getCano() == 2 ||
                            boardEntities.get(i).getCategoryEntity().getCano() == 3)) {
                ReplyDTO replyDTO = ReplyDTO.builder()
                        .rcontents(boardEntities.get(i).getBcontents())
                        .rcreatedDate(boardEntities.get(i).getCreatedDate())
                        .build();
                totalreply.add(replyDTO);
            }
        }

        // 자유, 후기
        for (int i = 0; i < replyEntities.size(); i++) {
            if (replyEntities.get(i).getMemberEntity2().getMno() == mno &&
                    (replyEntities.get(i).getCategoryEntity2().getCano() == 4 ||
                            replyEntities.get(i).getCategoryEntity2().getCano() == 5)) {
                ReplyDTO replyDTO = ReplyDTO.builder()
                        .rcontents(replyEntities.get(i).getRcontents())
                        .rcreatedDate(replyEntities.get(i).getCreatedDate())
                        .build();
                totalreply.add(replyDTO);
            }
        }

        totalreply.sort(Comparator.comparing(ReplyDTO::getRcreatedDate).reversed());

        return totalreply;
    }

    // 댓글 세기
    public int countreply(int mno) {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<ReplyEntity> replyEntities = replyRepository.findAll();

        int count = 0; // 전체 댓글 수

        // board DB (유기, 분양, 병원 댓글)
        for (int i = 0; i < boardEntities.size(); i++) {
            if (boardEntities.get(i).getMemberEntity().getMno() == mno && boardEntities.get(i).getCategoryEntity().getCano() != 4) {
                count++;
            }
        }
        // reply DB (자유 댓글)
        for (int i = 0; i < replyEntities.size(); i++) {
            if (replyEntities.get(i).getMemberEntity2().getMno() == mno) {
                count++;
            }
        }
        return count;
    }


}