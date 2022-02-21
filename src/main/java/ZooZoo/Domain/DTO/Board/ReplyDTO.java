package ZooZoo.Domain.DTO.Board;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDTO {
    private int rno;
    private String rcontents;
    private String bupdateDate;
    private String rwriter;
    private Integer rindex;

}
