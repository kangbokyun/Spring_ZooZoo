package ZooZoo.Domain.DTO.Board;

import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReReplyDTO {
    private int rrno;
    private String rrcontents;
    private String bcreateddate;
    private String rrwriter;
}
