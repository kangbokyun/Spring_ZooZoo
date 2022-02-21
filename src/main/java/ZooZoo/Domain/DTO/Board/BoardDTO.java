package ZooZoo.Domain.DTO.Board;

import ZooZoo.Domain.DTO.Category.CategoryDTO;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private int bno;
    private String btitle;
    private String bcontents;
    private int bview;
    private String bcreateddate;
    private List <String> bfile;
    private String bwriter;
    private String activation; // 분양여부
    private LocalDateTime date;

    //카테고리는??
    CategoryDTO categoryDTO = new CategoryDTO();

    public BoardEntity toentity(){
        return BoardEntity.builder()
                .btitle(this.btitle)
                .bcontents(this.bcontents)
                .bview(this.bview)
                .build();
    }


}
