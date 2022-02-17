package ZooZoo.Domain.Entity.BoardLike;

import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.DateEntity;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="boardlike")
@ToString (exclude={"memberBLikeEntity", "boardBLikeEntity", "categoryBLikeEntity"})
public class BoardLikeEntity extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blikeno;

    //멤버 엔티티 연결
    @ManyToOne
    @JoinColumn(name="mno")
    private MemberEntity memberBLikeEntity;

    @ManyToOne
    @JoinColumn(name="bno")
    private BoardEntity boardBLikeEntity;

    @ManyToOne
    @JoinColumn(name="cano")
    private CategoryEntity categoryBLikeEntity;

}
