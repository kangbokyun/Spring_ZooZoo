package ZooZoo.Domain.Entity.Board;

import ZooZoo.Domain.Entity.BoardLike.BoardLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity,Integer> {
    //게시판 빼오기
    @Query(nativeQuery = true, value = "select * from board where cano = :categoryNumber")
    Page<BoardEntity> findAllBoard(Pageable pageable, @Param("categoryNumber") int categoryNumber);

    @Query(nativeQuery = true, value = "select * from board where cano = :categoryNumber")
    List<BoardEntity> findFreeBoardByCategory(@Param("categoryNumber")int categoryNumber);

    @Query(nativeQuery = true, value = "select * from board where cano = :categoryNumber and mno = :mno")
    Page<BoardEntity> findAllMno(Pageable pageable, @Param("categoryNumber") int categoryNumber, @Param("mno") int mno);

    @Query(nativeQuery = true, value = "select * from board where cano = :categoryNumber and btitle like %:search%")
    Page<BoardEntity> findAllTitles(Pageable pageable,
                                    @Param("categoryNumber") int categoryNumber,
                                    @Param("search") String search);



    //전체 댓글
    @Query(nativeQuery = true, value = "select * from board where apikey = :apikey and cano = :cano order by bno DESC")
    List<BoardEntity> findAllReply(@Param("apikey") String apikey, @Param("cano") int cano);




}
