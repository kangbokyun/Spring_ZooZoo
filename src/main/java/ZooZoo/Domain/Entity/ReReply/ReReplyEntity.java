package ZooZoo.Domain.Entity.ReReply;

import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.DateEntity;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="rereply")
@Getter
@Setter
@ToString (exclude={"reReplyMemberEntity","reReplyBoardEntity", "reReplyCategoryEntity", "realReplyEntity"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReReplyEntity extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rrno;

    @Column(name="rcontents", length=1000)
    private String rrcontents;

    //멤버 엔티티 매핑
    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity reReplyMemberEntity;

    //게시판 엔티티 매핑
    @ManyToOne
    @JoinColumn(name = "bno")
    private BoardEntity reReplyBoardEntity;

    @ManyToOne
    @JoinColumn(name= "cano")
    private CategoryEntity reReplyCategoryEntity;

    @ManyToOne
    @JoinColumn(name="rno")
    private ReplyEntity realReplyEntity;
}
