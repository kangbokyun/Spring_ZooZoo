package ZooZoo.Domain.Entity.Reply;


import ZooZoo.Domain.Entity.Board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {
    @Query(nativeQuery = true, value = "select * from reply where bno = :bno and cano = :cano")
    List<ReplyEntity> findFreeReply(@Param("bno") int bno, @Param("cano") int cano);

    @Query(nativeQuery = true, value = "select * from board where mno = :mno")
    List<BoardEntity> findmyreply(@Param("mno") int mno);



}
