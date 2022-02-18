package ZooZoo.Controller.Board;

import ZooZoo.Service.ReReply.ReReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReReplyController {
    @Autowired
    ReReplyService reReplyService;

    //대 댓글
    @ResponseBody
    @GetMapping("/ReReply/ReReplyWrite")
    public int ReReplyWrite(@RequestParam("rno") int rno,
                            @RequestParam("bno") int bno,
                            @RequestParam("mno") int mno,
                            @RequestParam("cano") int cano,
                            Model model){
        if(rno == 0 || bno == 0 || mno == 0 || cano == 0){return 0;}
        boolean rs = reReplyService.ReReplyWrite(rno, bno, mno, cano);
        if(rs){
            return 1;
        }else{
            return 2;
        }
    }
}
