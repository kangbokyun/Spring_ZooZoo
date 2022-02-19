package ZooZoo.Service.ReReply;

import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Category.CategoryRepository;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import ZooZoo.Domain.Entity.ReReply.ReReplyEntity;
import ZooZoo.Domain.Entity.ReReply.ReReplyRepository;
import ZooZoo.Domain.Entity.Reply.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReReplyService {

    @Autowired
    ReReplyRepository reReplyRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    //대댓글 쓰기(저장)
    public boolean ReReplyWrite(int rno, int bno, int mno, int cano, String reReplyContents) {
        if(rno != 0 && bno != 0 && mno != 0 && cano != 0) {
            //자유 게시판 대댓글
            if(cano == 4) {
                ReReplyEntity reReplyEnt = ReReplyEntity.builder()
                        .reReplyBoardEntity(boardRepository.findById(bno).get())
                        .reReplyReplyEntity(replyRepository.findById(rno).get())
                        .reReplyCategoryEntity(categoryRepository.findById(cano).get())
                        .reReplyMemberEntity(memberRepository.findById(mno).get())
                        .rrcontents(reReplyContents)
                        .build();
                reReplyRepository.save(reReplyEnt);
                System.out.println(reReplyEnt);
                return true;
            //후기 게시판 대댓글
            }else if(cano == 5){
                ReReplyEntity reReplyEnt = ReReplyEntity.builder()
                        .reReplyBoardEntity(boardRepository.findById(bno).get())
                        .reReplyReplyEntity(replyRepository.findById(rno).get())
                        .reReplyCategoryEntity(categoryRepository.findById(cano).get())
                        .reReplyMemberEntity(memberRepository.findById(mno).get())
                        .rrcontents(reReplyContents)
                        .build();
                reReplyRepository.save(reReplyEnt);
                System.out.println(reReplyEnt);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    //대댓글 출력

}
