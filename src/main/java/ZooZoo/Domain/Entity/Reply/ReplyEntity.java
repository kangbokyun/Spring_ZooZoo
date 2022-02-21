package ZooZoo.Domain.Entity.Reply;

import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.DateEntity;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="reply")
@Getter @Setter @ToString (exclude={"boardEntity2", "memberEntity2", "categoryEntity2"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyEntity extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;

    @Column(name="rcontents", length=1000)
    private String rcontents;

    //댓글 대댓글 여부
    @Column
    private Integer rindex;

    //멤버 엔티티 매핑
    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity2;

    //게시판 엔티티 매핑
    @ManyToOne
    @JoinColumn(name = "bno")
    private BoardEntity boardEntity2;

    //카테고리 엔티티 매핑
    @ManyToOne
    @JoinColumn(name= "cano")
    private CategoryEntity categoryEntity2;



}
