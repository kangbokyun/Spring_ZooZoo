package ZooZoo.Service.Reply;

import ZooZoo.Domain.DTO.Member.MemberDTO;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Category.CategoryRepository;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import ZooZoo.Domain.Entity.Reply.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyService {

    @Autowired
    HttpServletRequest request;

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public int writeReply(int bno, int cano, String frcontents) {

        HttpSession session = request.getSession();
        MemberDTO memberDTO = (MemberDTO)session.getAttribute("loginDTO");
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberDTO.getMno());
        if(memberDTO == null || memberDTO.equals("")) return 1;
        if(frcontents == null || frcontents.equals("")) return 2;
        Optional<BoardEntity> boardEntity = boardRepository.findById(bno);
        ReplyEntity replyEntity = ReplyEntity.builder()
                .boardEntity2(boardEntity.get())
                .rcontents(frcontents)
                .memberEntity2(memberEntity.get())
                .categoryEntity2(boardEntity.get().getCategoryEntity())
                .rindex(null)
                .build();
        replyRepository.save(replyEntity);
        boardEntity.get().getReplyEntities().add(replyEntity);
        return 3;
    }
    //댓글 리스트 출력
    public List<ReplyEntity> getAllReplys(int bno, int cano) {
        List<ReplyEntity>replyEntity = new ArrayList<>();
        try{
            replyEntity = replyRepository.findFreeReply(bno, cano);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return replyEntity;
    }

    //댓글 삭제
    public boolean deleteReply(int rno) {
        Optional<ReplyEntity> replyEntity = replyRepository.findById(rno);
        if(replyEntity.get() != null){
            replyRepository.delete(replyEntity.get());
            return true;
        }else{
            return false;
        }
    }

    // 댓글 수정
    @Transactional
    public boolean updateReply(int rno, String newfreecontents) {
        // 댓글 가져오기
        ReplyEntity replyEntity = replyRepository.findById(rno).get();
        System.out.println("bno : " + rno);
        System.out.println("내용 : " + newfreecontents);
        // 내용 수정
        replyEntity.setRcontents(newfreecontents);
        return true;
    }

    // 댓글 수정
    @Transactional
    public boolean replyupdate(int bno, String newcontents) {
        // 댓글 가져오기
        ReplyEntity replyEntity = replyRepository.findById(bno).get();
        System.out.println("bno : " + bno);
        System.out.println("내용 : " + newcontents);
        System.out.println(replyRepository.findById(bno).get());
        // 내용 수정
        replyEntity.setRcontents(newcontents);
        return true;
    }

    //대댓글
    //대댓글 쓰기(저장)
    public boolean ReReplyWrite(int rno, int bno, int mno, int cano, String reReplyContents, Integer rindex) {
        if(rno != 0 && bno != 0 && mno != 0 && cano != 0) {
            //자유 게시판 대댓글
            if(cano == 4) {
                Optional<BoardEntity> boardEntityOptional = boardRepository.findById(bno);
                ReplyEntity reReplyEnt = ReplyEntity.builder()
                        .boardEntity2(boardRepository.findById(bno).get())
                        .categoryEntity2(categoryRepository.findById(cano).get())
                        .memberEntity2(memberRepository.findById(mno).get())
                        .rcontents(reReplyContents)
                        .rindex(rno)
                        .build();
                replyRepository.save(reReplyEnt);
                boardEntityOptional.get().getReplyEntities().add(reReplyEnt);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@boardEntityOptional.get()  : "+ boardEntityOptional.get());
                return true;
                //후기 게시판 대댓글
            }else if(cano == 5){
                Optional<BoardEntity> boardEntityOptional = boardRepository.findById(bno);
                ReplyEntity reReplyEnt = ReplyEntity.builder()
                        .boardEntity2(boardRepository.findById(bno).get())
                        .categoryEntity2(categoryRepository.findById(cano).get())
                        .memberEntity2(memberRepository.findById(mno).get())
                        .rcontents(reReplyContents)
                        .rindex(rno)
                        .build();
                replyRepository.save(reReplyEnt);
                boardEntityOptional.get().getReplyEntities().add(reReplyEnt);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@boardEntityOptional.get()  : "+ boardEntityOptional.get());
                System.out.println(reReplyEnt);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

}
