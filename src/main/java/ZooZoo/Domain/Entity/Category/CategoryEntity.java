package ZooZoo.Domain.Entity.Category;

import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardImgEntity;
import ZooZoo.Domain.Entity.Board.LossEntity;
import ZooZoo.Domain.Entity.BoardLike.BoardLikeEntity;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@ToString(exclude = {"boardEntities", "replyEntities", "lossEntities"})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    private int cano;
    private String caname;

    //게시판 엔티티 매핑
    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL)
    private List<BoardEntity> boardEntities = new ArrayList<>();

    //리플 엔티티 매핑
    @OneToMany(mappedBy = "categoryEntity2", cascade = CascadeType.ALL)
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    /* @OneToMany(mappedBy = "categoryEntity2", cascade = CascadeType.ALL)
     private List<BoardImgEntity> boardImgEntities = new ArrayList<>();*/

    //게시판 추천
    @OneToMany(mappedBy = "categoryBLikeEntity", cascade = CascadeType.ALL)
    private List<BoardLikeEntity> boardLikeEntities = new ArrayList<>();

}