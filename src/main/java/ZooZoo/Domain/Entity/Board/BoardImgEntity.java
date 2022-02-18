package ZooZoo.Domain.Entity.Board;

import ZooZoo.Domain.DTO.Board.BoardImgDTO;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.DateEntity;
import lombok.*;

import javax.persistence.*;
import java.util.stream.DoubleStream;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="boardimg")
@ToString
public class BoardImgEntity extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bimgno")
    private int bimgno;

    @Column(name="bimg")
    private String bimg;

    @ManyToOne
    @JoinColumn(name="bno")
    private BoardEntity boardEntity;

    @ManyToOne
    @JoinColumn(name="cano")
    private CategoryEntity categoryEntity2;

}