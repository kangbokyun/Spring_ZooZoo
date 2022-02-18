package ZooZoo.Service.Member;

import ZooZoo.Domain.DTO.Member.MemberDTO;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<BoardEntity> getmyboard = new ArrayList<>();

        for (int i = 0; i < boardEntities.size(); i++) {
            if (boardEntities.get(i).getMemberEntity().getMno() == mno &&
                    (boardEntities.get(i).getCategoryEntity().getCano() == 4 ||
                            boardEntities.get(i).getCategoryEntity().getCano() == 5)) {
                getmyboard.add(boardEntities.get(i));
            }
        }

        return getmyboard;

    }

    // 내가 쓴 게시물 가져오기
    public List<BoardEntity> getmybreply(int mno) {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<BoardEntity> getmyboard = new ArrayList<>();

        for (int i = 0; i < boardEntities.size(); i++) {
            if (boardEntities.get(i).getMemberEntity().getMno() == mno &&
                    (boardEntities.get(i).getCategoryEntity().getCano() == 1 ||
                            boardEntities.get(i).getCategoryEntity().getCano() == 2 ||
                            boardEntities.get(i).getCategoryEntity().getCano() == 3 ||
                            boardEntities.get(i).getCategoryEntity().getCano() == 5)) {
                getmyboard.add(boardEntities.get(i));
            }
        }
        return getmyboard;
    }
    // 내가 쓴 게시물 가져오기
    public List<ReplyEntity> getmyreply(int mno) {
        List<ReplyEntity> replyEntities = replyRepository.findAll();
        List<ReplyEntity> getmyboard = new ArrayList<>();

        for (int i = 0; i < replyEntities.size(); i++) {
            if (replyEntities.get(i).getMemberEntity2().getMno() == mno &&
                    (replyEntities.get(i).getCategoryEntity2().getCano() == 4)) {
                getmyboard.add(replyEntities.get(i));
            }
        }
        return getmyboard;
    }

//    // 내가 쓴 게시물 가져오기
//    @Transactional
//    public List<BoardEntity> getmyboard(int mno) {
//
//        List<BoardEntity> boardEntities = new ArrayList<>();
//        List<BoardEntity> boardEntities1 = new ArrayList<>();
//        List<BoardEntity> finalboard = new ArrayList<>();
//
//
//        try {
//            boardEntities = boardRepository.findmyboard(4, mno);
//            boardEntities1 = boardRepository.findmyboard(5, mno);
//        } catch (Exception e) {
//        }
//
//        return boardEntities;
//    }

//    // 내가 쓴 댓글 가져오기
//    public List<BoardEntity> getmyreply(int mno) {
//        List<BoardEntity> replyEntities1 = boardRepository.findmyboard(1,mno);
//        List<BoardEntity> replyEntities2 = boardRepository.findmyboard(2,mno);
//        List<BoardEntity> replyEntities3 = boardRepository.findmyboard(3,mno);
//        List<BoardEntity> replyEntities4 = replyRepository.findmyreply(mno);
//        List<BoardEntity> replyEntities5 = boardRepository.findmyboard(5,mno);
//
//        List<BoardEntity> getmyreply = new ArrayList<>();
//
//        for (int i = 0; i < replyEntities1.size(); i++) {
//            getmyreply.add(replyEntities1.get(i));
//        }
//        for (int i = 0; i < replyEntities2.size(); i++) {
//            getmyreply.add(replyEntities2.get(i));
//        }
//        for (int i = 0; i < replyEntities3.size(); i++) {
//            getmyreply.add(replyEntities3.get(i));
//        }
//        for (int i = 0; i < replyEntities4.size(); i++) {
//            getmyreply.add(replyEntities4.get(i));
//        }
//        for (int i = 0; i < replyEntities5.size(); i++) {
//            getmyreply.add(replyEntities5.get(i));
//        }
//
//        return getmyreply;
//    }


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