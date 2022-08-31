package ZooZoo.Controller.BoardLike;

import ZooZoo.Service.BoardLike.BoardLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BoardLikeController {

    @Autowired 
    HttpServletRequest request;

    @Autowired
    BoardLikeService boardLikeService;

    //추천 눌렀을 때
    @ResponseBody
    @GetMapping ("/Board/Free/ClickFreeBoardLike")
    public int ClickFreeBoardLike(@RequestParam("bno") int bno,
                                  @RequestParam("cano") int cano,
                                  @RequestParam("mno") int mno){

        int rs = boardLikeService.clickLike(bno, cano, mno);
        return rs;
    }

}
