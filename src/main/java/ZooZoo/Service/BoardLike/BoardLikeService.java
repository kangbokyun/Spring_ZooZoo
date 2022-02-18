package ZooZoo.Service.BoardLike;

import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.BoardLike.BoardLikeEntity;
import ZooZoo.Domain.Entity.BoardLike.BoardLikeRepository;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardLikeService {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardLikeRepository boardLikeRepository;
    @Autowired
    MemberRepository memberRepository;

    //자유게시판 상세페이지(view) 추천 누르기 전에 추천 되었는지 여부
    public int likeCheck(int bno, int cano, int mno){
        //해당 게시판넘버와 카테고리 넘버와 버를 넣어서 추천 개수가 0이면 true 아니면 false
        Boolean likeCheck = boardLikeRepository.findLike(bno, cano, mno);

        if(likeCheck != true){  //추천이 되어있으면 1
            return 1;
        }else {                 //추천 안 되어있으면 2
            return 2;
        }
    }

    //추천 눌렀을 때
    public int clickLike(int bno, int cano, int mno) {
        Optional<BoardEntity> boardEntity =boardRepository.findById(bno);
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);
        Boolean likeCheck = boardLikeRepository.findLike(bno, cano, mno);
        System.out.println("######################### likeCheck : "+likeCheck);
        if(likeCheck == true) {
            BoardLikeEntity boardLikeEntity = BoardLikeEntity.builder()
                    .boardBLikeEntity(boardEntity.get())
                    .categoryBLikeEntity(boardEntity.get().getCategoryEntity())
                    .memberBLikeEntity(memberEntity.get())
                    .build();
            boardLikeRepository.save(boardLikeEntity);

            return 1;
        }else{
            int blikeno = boardLikeRepository.findLikeno(bno, cano, mno);
            Optional<BoardLikeEntity> boardLikeEntity = boardLikeRepository.findById(blikeno);
            if(boardLikeEntity.get() != null){
                boardLikeRepository.delete(boardLikeEntity.get());
                return 2;
            }
            return 3;
        }
    }

    //추천 개수
    public int likeCount(int bno) {
        int count = boardLikeRepository.likeCount(bno);
        return count;
    }
}
