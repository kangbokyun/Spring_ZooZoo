package ZooZoo.Domain.Entity.Board;

import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="lossapi")
@ToString (exclude={"memberEntity","categoryEntity"})
public class LossEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rvno")
    private int rvno;

    @Column(name="rvapikey")
    private String rvapikey;

    @ManyToOne
    @JoinColumn(name="mno")
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name="cano")
    private CategoryEntity categoryEntity;


}
