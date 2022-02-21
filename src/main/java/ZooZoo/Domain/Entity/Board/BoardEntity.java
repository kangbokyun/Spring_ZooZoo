package ZooZoo.Domain.Entity.Board;

import ZooZoo.Domain.Entity.BoardLike.BoardLikeEntity;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.DateEntity;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="board")
@ToString (exclude={"memberEntity","categoryEntity","boardImgEntities", "replyEntities", "boardLikeEntities"})
public class BoardEntity extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;

    @Column (name="btitle")
    private String btitle;
    @Column (name="bcontents", columnDefinition = "LONGTEXT")
    private String bcontents;
    @Column (name="bview")
    private int bview;

    @Column(name="apikey")
    private String apikey; // api 식별키

    @Column(name="bstar")
    private String bstar; //병호 별점


    //회원 번호 fk
    @ManyToOne
    @JoinColumn(name="mno")
    private MemberEntity memberEntity;

    //카테고리 fk??????
    @ManyToOne
    @JoinColumn(name = "cano")
    private CategoryEntity categoryEntity;

    //게시판 이미지
    @OneToMany(mappedBy="boardEntity", cascade = CascadeType.ALL)
    private List<BoardImgEntity> boardImgEntities = new ArrayList<>();

    //댓글
    @OneToMany(mappedBy="boardEntity2", cascade =CascadeType.ALL)
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    public int getintbstar(){
        double bstar2 = Double.parseDouble(this.bstar)*10.0*2;
        int getint = (int)bstar2;

        return getint;
    }

    //게시판 추천
    @OneToMany(mappedBy="boardBLikeEntity", cascade = CascadeType.ALL)
    private List<BoardLikeEntity> boardLikeEntities = new ArrayList<>();

}