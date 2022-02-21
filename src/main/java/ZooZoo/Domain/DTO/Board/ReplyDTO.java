package ZooZoo.Domain.DTO.Board;

import ZooZoo.Domain.DTO.Category.CategoryDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

    private int rno;
    private String rcontents;
    private LocalDateTime rcreatedDate;
    private String rwriter;
    private int cano;

}
