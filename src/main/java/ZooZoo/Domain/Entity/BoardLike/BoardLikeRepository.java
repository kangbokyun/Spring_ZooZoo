package ZooZoo.Domain.Entity.BoardLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity,Integer> {

    //해당 게시판 번호, 카테고리, 멤버의 좋아요의 수가 0이면(없으면) true 아니면(있으면) false
    @Query(nativeQuery = true, value = "SELECT IF(COUNT(*) = 0, 'true', 'false') AS NewResult FROM boardlike WHERE bno = :bno and cano = :cano and mno = :mno")
    Boolean findLike(@Param("bno") int bno, @Param("cano") int cano, @Param("mno") int mno);

    //좋아요 번호 찾기
    @Query(nativeQuery = true, value = "SELECT blikeno FROM boardlike WHERE bno = :bno and cano = :cano and mno = :mno")
    int findLikeno(@Param("bno") int bno, @Param("cano") int cano, @Param("mno") int mno);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM boardlike WHERE bno = :bno")
    int likeCount(@Param("bno") int bno);
}
