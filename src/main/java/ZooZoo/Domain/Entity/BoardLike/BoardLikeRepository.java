package ZooZoo.Domain.Entity.BoardLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity,Integer> {

    //해당 게시판 번호, 카테고리, 멤버의 좋아요가 존재하는지 유무
    @Query(nativeQuery = true, value = "SELECT IF(COUNT(*) = 0, 'true', 'false') AS NewResult FROM boardlike WHERE bno = :bno and cano = :cano and mno = :mno")
    Boolean findLike(@Param("bno") int bno, @Param("cano") int cano, @Param("mno") int mno);

    //좋아요 번호 찾기
    @Query(nativeQuery = true, value = "SELECT blikeno FROM boardlike WHERE bno = :bno and cano = :cano and mno = :mno")
    int findLikeno(@Param("bno") int bno, @Param("cano") int cano, @Param("mno") int mno);
}
