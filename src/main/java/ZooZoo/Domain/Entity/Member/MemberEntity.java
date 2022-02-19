package ZooZoo.Domain.Entity.Member;

import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.BoardLike.BoardLikeEntity;
import ZooZoo.Domain.Entity.DateEntity;
import ZooZoo.Domain.Entity.ReReply.ReReplyEntity;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "member")
@Getter @Setter @ToString (exclude={"boardEntities","replyEntities", "boardLikeEntities", "reReplyEntities"}) @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberEntity extends DateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;
    @Column(name = "mid")
    private String mid;
    @Column(name = "mpw")
    private String mpw;
    @Column(name = "mname")
    private String mname;
    @Column(name = "memail")
    private String memail;
    @Column(name = "mbirth")
    private String mbirth;
    @Column(name = "maddress")
    private String maddress;

    //게시판 엔티티와 양방향
    @OneToMany(mappedBy = "memberEntity")
    private List<BoardEntity> boardEntities = new ArrayList<>();

    //댓글 엔티티 매핑
    @OneToMany(mappedBy = "memberEntity2", cascade = CascadeType.ALL)
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    //게시판 추천
    @OneToMany(mappedBy="memberBLikeEntity", cascade = CascadeType.ALL)
    private List<BoardLikeEntity> boardLikeEntities = new ArrayList<>();

    //대댓글 엔티티 매핑
    @OneToMany(mappedBy="reReplyMemberEntity", cascade = CascadeType.ALL)
    private List<ReReplyEntity> reReplyEntities = new ArrayList<>();
}
