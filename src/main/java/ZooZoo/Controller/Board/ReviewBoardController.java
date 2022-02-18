package ZooZoo.Controller.Board;

import ZooZoo.Domain.DTO.Board.BoardDTO;
import ZooZoo.Domain.DTO.Category.CategoryDTO;
import ZooZoo.Domain.DTO.Member.MemberDTO;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardImgEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import ZooZoo.Domain.Entity.Reply.ReplyRepository;
import ZooZoo.Service.BoardLike.BoardLikeService;
import ZooZoo.Service.Reply.ReplyService;
import ZooZoo.Service.Review.ReviewBoardService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.Member;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class ReviewBoardController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    ReviewBoardService reviewBoardService;

    @Autowired
    ReplyService replyService;

    @Autowired
    BoardLikeService boardLikeService;

    @Autowired
    BoardRepository boardRepository;

    //후기 게시판 이동
    @GetMapping("/ReviewBoardList")
    public String goToReviewBoardList(Model model, @PageableDefault Pageable pageable) {
        HttpSession session = request.getSession();
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("loginDTO");
        Page<BoardEntity> boardEntities = reviewBoardService.GetAll(pageable);
        //System.out.println("Page boardEntities : " + boardEntities);
        model.addAttribute("boardEntities", boardEntities);
        model.addAttribute("memberDTO",memberDTO);
        return "Board/Review/ReviewBoardMain";
    }

    //후기게시판 쓰기 이동
    @GetMapping("/Board/Review/ReviewBoardWrite")
    public String goToReviewBoardWrite(){
        return "Board/Review/ReviewBoardWrite";
    }

    //후기게시판 쓰기 드래그 앤 드롭
    @ResponseBody
    @RequestMapping(value = { "/Board/Review/ReviewBoardWriteController" }, method = RequestMethod.POST, produces = "json/plain;charset=UTF-8")
    public String uploadPath(MultipartHttpServletRequest mtfRequest) {


        String res = "1";
        String btitle = request.getParameter("freebtitle");
        String bcontents = request.getParameter("freebcontents");
        //태그 연습
        System.out.println("제목 > " + btitle);
        System.out.println("내용 > " + bcontents);

        //전달받은 제목, 타이틀이 없으면 3반환
        if (btitle == null || btitle.equals("") || bcontents == null || bcontents.equals("")) {
            return "3";
        }
        //전달받은 제목, 내용이 있으면 (첨부파일은 multipart라 null이래도 사이즈는 0이다.)
        if (mtfRequest != null) {
            List<MultipartFile> fileList = mtfRequest.getFiles("fileList");
            //첨부파일이 없어도 글 써야됨
            if (fileList.isEmpty()) {
                String rs = reviewBoardService.ReviewBoardWrite(btitle, bcontents, fileList);
                return rs;
            }
            //첨부파일이 있으면
            for (int i = 0; i < fileList.size(); i++) {
                MultipartFile multi = fileList.get(i);
                System.out.println("multi : " + multi);
                //첨부파일이 null이면
                if (multi == null) {
                    return "0";
                    //첨부파일이 있는데 사이즈가 0이다?? 말도안되니까 리턴
                } else if (multi.getSize() == 0) {
                    return "0";
                    //첨부파일이 null이 아니고, 사이즈도 0이 아니니까 글 등록해야됨
                } else {
                    String rs2 = reviewBoardService.ReviewBoardWrite(btitle, bcontents, fileList);
                    System.out.println("파일명 : " + multi.getOriginalFilename() + " / 파일 사이즈 : " + multi.getSize());
                    return rs2;
                }
            }
        }
        return res;
    }

    //후기게시판 상세페이지
    @GetMapping("/Board/Review/ReviewBoardView/{bno}")
    public String goToFreeBoardView(@PathVariable("bno") int bno, Model model) {
        HttpSession session =  request.getSession();
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("loginDTO");
        ArrayList<String> bimglist = new ArrayList<String>();
        try {
            BoardEntity boardEntity = reviewBoardService.getReviewBoardView(bno);
            for(int i =0; i<boardEntity.getBoardImgEntities().size(); i++){
                bimglist.add(boardEntity.getBoardImgEntities().get(i).getBimg());
            }
            List<ReplyEntity> replyEntities = replyService.getAllReplys(bno, boardEntity.getCategoryEntity().getCano());
            model.addAttribute("bimglist",bimglist);
            model.addAttribute("boardEntity", boardEntity);
            model.addAttribute("replyEntities",replyEntities);
            int likeCountNo = boardLikeService.likeCount(bno);
            model.addAttribute("likeCountNo",likeCountNo);
        } catch(Exception e){
            System.out.println(e);
        }

        //좋아요 되었는지??
        Optional<BoardEntity> boardEntity2 =boardRepository.findById(bno);
        if(memberDTO != null) {
            System.out.println("@@@@@@@@@@@@@@@@@memberDTO.getMno() : " + memberDTO.getMno());
            int rs = boardLikeService.likeCheck(bno, boardEntity2.get().getCategoryEntity().getCano(), memberDTO.getMno());
            System.out.println("좋아요 되었습니까?? : " + rs);
            model.addAttribute("rs",rs);
        }


        model.addAttribute("memberDTO",memberDTO);
        return "Board/Review/ReviewBoardView";
    }

    //후기게시판 게시물 삭제
    @GetMapping("/Board/Review/ReviewBoardDelete")
    @ResponseBody
    public int FreeBoardDelete(@RequestParam("bno") int bno){
        boolean result = reviewBoardService.deleteBoard(bno);
        if(result){
            return 1;
        }else{
            return 2;
        }
    }

    //후기게시판 게시물 수정페이지 이동
    @GetMapping("/Board/Review/goToReviewBoardUpdate/{bno}")
    public String goToReviewBoardUpdate(@PathVariable("bno") int bno, Model model){
        BoardEntity boardEntity = reviewBoardService.getReviewBoardView(bno);

        //리스트에 담아서 첨부파일 보여주기
        List<String> imgList = new ArrayList<>();
        if(boardEntity.getBoardImgEntities().size() != 0){
            for(int i=0; i<boardEntity.getBoardImgEntities().size(); i++){
                //원래 첨부파일 보여주는 거
                String realImg = boardEntity.getBoardImgEntities().get(i).getBimg().toString().split("_")[1];
                imgList.add(realImg);
            }
        }else{
            System.out.println("없음");
        }
        System.out.println(imgList);
        //첨부파일이 없다면???????
        //원래 첨부파일 보여주는 거
        String imgStr = imgList.toString().replace("[","").replace("]","");
        model.addAttribute("imgStr",imgStr);
        model.addAttribute("boardEntity", boardEntity);

        return "Board/Review/ReviewBoardUpdate";
    }

    //후기게시판 수정 이미지 개별 삭제
    @ResponseBody
    @GetMapping ("/Board/Reivew/ReviewBoardImgDelete")
    public String ReviewBoardImgDelete(@RequestParam("bno") int bno, @RequestParam("bimg") String bimg){
        boolean rs = reviewBoardService.deleteBoardImg(bno, bimg);
        if(rs) {
            return "1";
        }else {
            return "2";
        }
    }


    //후기게시판 수정처리
    @ResponseBody
    @RequestMapping(value = {"/Board/Review/ReviewBoardUpdate"}, method = RequestMethod.POST, produces = "json/plain;charset=UTF-8")
    public String ReviewBoardUpdate(MultipartHttpServletRequest mtfRequest,
                                  @RequestParam("bcontents") String bcontents,
                                  @RequestParam("btitle") String btitle,
                                  @RequestParam("bno") int bno,
                                  Model model){

        String res = "1";
        System.out.println("제목 > " + btitle);
        System.out.println("내용 > " + bcontents);
        System.out.println(bcontents+", "+btitle+", "+bno+", "+mtfRequest);
        if (btitle == null || btitle.equals("") || bcontents == null || bcontents.equals("")) {
            return "3";
        }
        //수정 요청이 있으면
        if (mtfRequest != null) {
            List<MultipartFile> fileList = mtfRequest.getFiles("fileList");
            //첨부파일이 없으면 ("")이면 저장해야됨, 첨부파일이 없어도 글은 수정할 수 있어야됨
            if (fileList.isEmpty()) {
                String rs = reviewBoardService.updateFreeBoard(bcontents, btitle, bno, fileList);
                return rs;
            }
            //첨부파일이 있을 때
            for (int i = 0; i < fileList.size(); i++) {
                MultipartFile multi = fileList.get(i);
                System.out.println("multi : " + multi);
                //첨부파일 리스트의 i번째가 null이면?
                if (multi == null) {
                    return "0";
                    //첨부파일 리스트가 null은 아닌데 사이즈가 0이면??
                } else if (multi.getSize() == 0) {
                    return "0";
                    //첨부파일 리스트가 null이 아니고 사이즈가 0이 아니면 글 수정하기
                } else {
                    String rs2 = reviewBoardService.updateFreeBoard(bcontents, btitle,  bno, fileList);
                    System.out.println("파일명 : " + multi.getOriginalFilename() + " / 파일 사이즈 : " + multi.getSize());
                    return rs2;
                }
            }
        }
        //1 반환 (글쓰기 완료)
        return res;

    }

    //첨부파일 다운로드 처리
    @GetMapping("/Board/Review/ReviewBoardDownload")
    public void ReviewBoardDownload(@RequestParam("bimg") String bimg, HttpServletResponse response){
        reviewBoardService.freeBoardFileDown(bimg, response);
    }


    //후기게시판 댓글 달기
    @ResponseBody
    @GetMapping("/Board/Review/ReviewBoardReplyWrite")
    public int ReviewBoardReplyWrite(@RequestParam("bno") int bno,
                                   @RequestParam("frcontents") String frcontents){
        int cano = 5;
        int rs = replyService.writeReply(bno, cano, frcontents);

        if(rs == 1){
            return 1; //로그인 안했을 때
        }else if(rs==2){
            return 2; //댓글 내용 없을때

        }else if(rs==3){
            return 3; //정상
        }else{
            return 4; //버그 비정상
        }
    }

  //후기게시판 댓글 삭제
    @ResponseBody
    @GetMapping("/Board/Review/ReviewBoardReplyDelete")
    public int ReviewBoardReplyDelete(@RequestParam("rno") int rno){
        boolean rs = replyService.deleteReply(rno);
        if(rs){
            return 1;
        }else{
            return 2;
        }

    }

    // 댓글 수정
    @GetMapping("/Board/Review/ReviewBoardReplyUpdate")
    @ResponseBody
    public String ReviewBoardReplyUpdate(@RequestParam("bno") int bno, @RequestParam("newcontents") String newcontents){
        replyService.replyupdate(bno, newcontents);
        return "1";
    }
}












