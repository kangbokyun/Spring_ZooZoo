package ZooZoo.Domain.Entity.ReReply;

import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReReplyRepository extends JpaRepository<ReReplyEntity, Integer> {

    @Query(nativeQuery = true, value = "select * from rereply where bno = :bno and cano = :cano")
    List<ReReplyEntity> findReReply(@Param("bno") int bno, @Param("cano") int cano);
}
