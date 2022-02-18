package ZooZoo.Controller.Board;

import ZooZoo.Domain.DTO.Board.BoardDTO;
import ZooZoo.Domain.DTO.Board.LossDTO;
import ZooZoo.Domain.DTO.Board.ShareDTO;
import ZooZoo.Domain.DTO.Member.MemberDTO;
import ZooZoo.Domain.DTO.Pagination;
import ZooZoo.Domain.DTO.Pagination1;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Service.Free.FreeBoardService;
import ZooZoo.Service.Loss.LossService;
import ZooZoo.Service.Share.ShareService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BoardController {
    @Autowired
    ShareService shareService;
    @Autowired
    FreeBoardService freeBoardService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    LossService lossService;

    // 분양게시판으로
    @GetMapping("/ShareBoardList")
    public String goToShareBoardList(Model model, @RequestParam(defaultValue = "1") int page) {
        ShareDTO shareDTO = new ShareDTO();
        ArrayList<ShareDTO> shareDTOS = new ArrayList<>();
        ArrayList<String> petshop = shareService.Share(null);
        ArrayList<String> oneStep = new ArrayList<>();
        ArrayList<String> twoStep = new ArrayList<>();
        ArrayList<String> threeStep = new ArrayList<>();
        ArrayList<String> addrpost = new ArrayList<>();
        ArrayList<String> addrx = new ArrayList<>();
        ArrayList<String> addry = new ArrayList<>();
        ArrayList<String> oldaddress = new ArrayList<>();
        ArrayList<String> code = new ArrayList<>();
        ArrayList<String> agreedate = new ArrayList<>();
        ArrayList<String> tel = new ArrayList<>();
        String[] s = new String[petshop.size()];
        int totalSize = petshop.size();
        Pagination paging = new Pagination(totalSize, page);
        int j = 0;
        if (page != 80) {
            for (int i = (page - 1) * 100; i < paging.getPageSize() * page; i++) {
                s[i] = petshop.get(i);
                oneStep.add(s[i].split(":")[0]);
                twoStep.add(s[i].split(":")[1]);
                threeStep.add(s[i].split(":")[2]);
                addrx.add(s[i].split(":")[3]);
                addry.add(s[i].split(":")[4]);
                addrpost.add(s[i].split(":")[5]);
                code.add(s[i].split(":")[6]);
                agreedate.add(s[i].split(":")[7]);
                oldaddress.add(s[i].split(":")[8]);
                tel.add(s[i].split(":")[9]);
                shareDTOS.add(new ShareDTO(oneStep.get(j), threeStep.get(j), twoStep.get(j), addrpost.get(j), addrx.get(j), addry.get(j), oldaddress.get(j), code.get(j), agreedate.get(j), tel.get(j)));
                j++;
            }
        } else {
            for (int i = (page - 1) * 100; i < totalSize; i++) {
                s[i] = petshop.get(i);
                oneStep.add(s[i].split(":")[0]);
                twoStep.add(s[i].split(":")[1]);
                threeStep.add(s[i].split(":")[2]);
                addrx.add(s[i].split(":")[3]);
                addry.add(s[i].split(":")[4]);
                addrpost.add(s[i].split(":")[5]);
                code.add(s[i].split(":")[6]);
                agreedate.add(s[i].split(":")[7]);
                oldaddress.add(s[i].split(":")[8]);
                tel.add(s[i].split(":")[9]);
                shareDTOS.add(new ShareDTO(oneStep.get(j), threeStep.get(j), twoStep.get(j), addrpost.get(j), addrx.get(j), addry.get(j), oldaddress.get(j), code.get(j), agreedate.get(j), tel.get(j)));
                j++;
            }
        }

        String[] select = {
                "김포시", "파주시", "고양시", "양주시", "동두천시", "포천시", "가평군",
                "남양주시", "양평군", "여주시", "이천시", "광주시", "하남시", "성남시",
                "용인시", "안성시", "오산시", "평택시", "화성시", "수원시", "안산시",
                "의왕시", "과천시", "군포시", "시흥시", "광명시", "부천시"
        };
        ArrayList<String> selectArr = new ArrayList<>();
        for (int i = 0; i < select.length; i++) {
            selectArr.add(select[i]);
        }

        model.addAttribute("select", selectArr);
        model.addAttribute("pagination", paging);
        model.addAttribute("share", shareDTOS);

        return "Board/Share/ShareBoardList";
    }

    // 셀렉트 선택 됐을 때
    @GetMapping("/ShareBoardListController")
    public String ShareBoardListController(Model model, @RequestParam(defaultValue = "1") int page) {
        String option = request.getParameter("option");
        HttpSession session = request.getSession();
        int lastpageno = 0;
        if (option != null) {
            String op = option;
            session.setAttribute("option", op);
        } else {
            option = (String) session.getAttribute("option");
        }

        if(option == null || option.equals("null") || option.equals(null)) { // 처음 페이지 열 때
            System.out.println("1");
            ShareDTO shareDTO = new ShareDTO();
            ArrayList<ShareDTO> shareDTOS = new ArrayList<>();
            ArrayList<String> petshop = shareService.Share(option);
            ArrayList<String> oneStep = new ArrayList<>();
            ArrayList<String> twoStep = new ArrayList<>();
            ArrayList<String> threeStep = new ArrayList<>();
            ArrayList<String> addrpost = new ArrayList<>();
            ArrayList<String> addrx = new ArrayList<>();
            ArrayList<String> addry = new ArrayList<>();
            ArrayList<String> oldaddress = new ArrayList<>();
            ArrayList<String> code = new ArrayList<>();
            ArrayList<String> agreedate = new ArrayList<>();
            ArrayList<String> tel = new ArrayList<>();
            String[] s = new String[petshop.size()];
            int totalSize = petshop.size();
            Pagination paging = new Pagination(totalSize, page);
            int j = 0;
            if (page != 80) {
                for (int i = (page - 1) * 100; i < paging.getPageSize() * page; i++) {
                    s[i] = petshop.get(i);
                    oneStep.add(s[i].split(":")[0]);
                    twoStep.add(s[i].split(":")[1]);
                    threeStep.add(s[i].split(":")[2]);
                    addrx.add(s[i].split(":")[3]);
                    addry.add(s[i].split(":")[4]);
                    addrpost.add(s[i].split(":")[5]);
                    code.add(s[i].split(":")[6]);
                    agreedate.add(s[i].split(":")[7]);
                    oldaddress.add(s[i].split(":")[8]);
                    tel.add(s[i].split(":")[9]);
                    shareDTOS.add(new ShareDTO(oneStep.get(j), threeStep.get(j), twoStep.get(j), addrpost.get(j), addrx.get(j), addry.get(j), oldaddress.get(j), code.get(j), agreedate.get(j), tel.get(j)));
                    j++;
                }
            } else {
                for (int i = (page - 1) * 100; i < totalSize; i++) {
                    s[i] = petshop.get(i);
                    oneStep.add(s[i].split(":")[0]);
                    twoStep.add(s[i].split(":")[1]);
                    threeStep.add(s[i].split(":")[2]);
                    addrx.add(s[i].split(":")[3]);
                    addry.add(s[i].split(":")[4]);
                    addrpost.add(s[i].split(":")[5]);
                    code.add(s[i].split(":")[6]);
                    agreedate.add(s[i].split(":")[7]);
                    oldaddress.add(s[i].split(":")[8]);
                    tel.add(s[i].split(":")[9]);
                    shareDTOS.add(new ShareDTO(oneStep.get(j), threeStep.get(j), twoStep.get(j), addrpost.get(j), addrx.get(j), addry.get(j), oldaddress.get(j), code.get(j), agreedate.get(j), tel.get(j)));
                    j++;
                }
            }

            String[] select = {
                    "김포시", "파주시", "고양시", "양주시", "동두천시", "포천시", "가평군",
                    "남양주시", "양평군", "여주시", "이천시", "광주시", "하남시", "성남시",
                    "용인시", "안성시", "오산시", "평택시", "화성시", "수원시", "안산시",
                    "의왕시", "과천시", "군포시", "시흥시", "광명시", "부천시"
            };
            ArrayList<String> selectArr = new ArrayList<>();
            for (int i = 0; i < select.length; i++) {
                selectArr.add(select[i]);
            }

            model.addAttribute("select", selectArr);
            model.addAttribute("pagination", paging);
            model.addAttribute("share", shareDTOS);
            return "Board/Share/ShareTable";
        } else { // 카테고리 선택되면
            ShareDTO shareDTO = new ShareDTO();
            ArrayList<ShareDTO> shareDTOS = new ArrayList<>();
            ArrayList<String> petshop = shareService.Share(option);
            ArrayList<String> oneStep = new ArrayList<>();
            ArrayList<String> twoStep = new ArrayList<>();
            ArrayList<String> threeStep = new ArrayList<>();
            ArrayList<String> addrpost = new ArrayList<>();
            ArrayList<String> addrx = new ArrayList<>();
            ArrayList<String> addry = new ArrayList<>();
            ArrayList<String> oldaddress = new ArrayList<>();
            ArrayList<String> code = new ArrayList<>();
            ArrayList<String> agreedate = new ArrayList<>();
            ArrayList<String> tel = new ArrayList<>();
            String[] s = new String[petshop.size()];
            int totalSize = petshop.size();
            Pagination paging = new Pagination(totalSize, page);
            int j = 0;
            if ((paging.getTotalListCnt() - paging.getStartIndex()) > 100) {
                for (int i = (page - 1) * 100; i < paging.getPageSize() * page; i++) {
                    s[i] = petshop.get(i);
                    oneStep.add(s[i].split(":")[0]);
                    twoStep.add(s[i].split(":")[1]);
                    threeStep.add(s[i].split(":")[2]);
                    addrx.add(s[i].split(":")[3]);
                    addry.add(s[i].split(":")[4]);
                    addrpost.add(s[i].split(":")[5]);
                    code.add(s[i].split(":")[6]);
                    agreedate.add(s[i].split(":")[7]);
                    oldaddress.add(s[i].split(":")[8]);
                    tel.add(s[i].split(":")[9]);
                    shareDTOS.add(new ShareDTO(oneStep.get(j), threeStep.get(j), twoStep.get(j), addrpost.get(j), addrx.get(j), addry.get(j), oldaddress.get(j), code.get(j), agreedate.get(j), tel.get(j)));
                    j++;
                }
            } else {
                for (int i = (page - 1) * 100; i < totalSize; i++) {
                    s[i] = petshop.get(i);
                    oneStep.add(s[i].split(":")[0]);
                    twoStep.add(s[i].split(":")[1]);
                    threeStep.add(s[i].split(":")[2]);
                    addrx.add(s[i].split(":")[3]);
                    addry.add(s[i].split(":")[4]);
                    addrpost.add(s[i].split(":")[5]);
                    code.add(s[i].split(":")[6]);
                    agreedate.add(s[i].split(":")[7]);
                    oldaddress.add(s[i].split(":")[8]);
                    tel.add(s[i].split(":")[9]);
                    shareDTOS.add(new ShareDTO(oneStep.get(j), threeStep.get(j), twoStep.get(j), addrpost.get(j), addrx.get(j), addry.get(j), oldaddress.get(j), code.get(j), agreedate.get(j), tel.get(j)));
                    j++;
                }
            }

            String[] select = {
                    "김포시", "파주시", "고양시", "양주시", "동두천시", "포천시", "가평군",
                    "남양주시", "양평군", "여주시", "이천시", "광주시", "하남시", "성남시",
                    "용인시", "안성시", "오산시", "평택시", "화성시", "수원시", "안산시",
                    "의왕시", "과천시", "군포시", "시흥시", "광명시", "부천시"
            };
            ArrayList<String> selectArr = new ArrayList<>();
            for (int i = 0; i < select.length; i++) {
                selectArr.add(select[i]);
            }

            model.addAttribute("select", selectArr);
            model.addAttribute("pagination", paging);
            model.addAttribute("share", shareDTOS);
            return "Board/Share/ShareTable";
        }
    }

    // 분양게시판 상세보기
    @GetMapping("/ShareBoardView/{shareno}")
    public String SBView(ShareDTO shareDTO, Model model) {
        System.out.println("shareDTO : " + shareDTO);
        String sno = shareDTO.toString().split("shareno=")[1];
        String no = sno.split("ShareDTO")[0];
        String name1 = shareDTO.toString().split("sharename=")[1];
        String name = name1.split(",")[0];
        String address1 = shareDTO.toString().split("shareaddress=")[1];
        String address = address1.split(",")[0];
        String addrx1 = shareDTO.toString().split("addrx=")[1];
        String addrx = addrx1.split(",")[0];
        String addry1 = shareDTO.toString().split("addry=")[1];
        String addry = addry1.split(",")[0];
        String oldaddress1 = shareDTO.toString().split("oldaddress=")[1];
        String oldaddress = oldaddress1.split(",")[0];
        String post1 = shareDTO.toString().split("post=")[1];
        String post = post1.split(",")[0];
        String code1 = shareDTO.toString().split("code=")[1];
        String code = code1.split(",")[0];
        String agreedate1 = shareDTO.toString().split("agreedate=")[1];
        String agreedate = agreedate1.split(",")[0];
        String tel1 = shareDTO.toString().split("tel=")[1];
        String tel = tel1.split(",")[0];

        ShareDTO dto = new ShareDTO();
        dto.setShareno(no);
        dto.setSharename(name);
        dto.setShareaddress(address);
        dto.setShareaddrx(addrx);
        dto.setShareaddry(addry);
        dto.setShareoldaddress(oldaddress);
        dto.setSharepost(post);
        dto.setSharecode(code);
        dto.setShareagreedate(agreedate);
        dto.setSharetel(tel);

        List<BoardDTO> review = shareService.ReviewView(addrx, addry, agreedate, code);
        model.addAttribute("shareDTO", dto);
        model.addAttribute("review", review);

        return "Board/Share/ShareBoardView";
    }

    // 대댓글 출력
    @GetMapping("/RIReplyView")
    public String getRIReply(@RequestParam("bno")int bno, Model model) {
        List<BoardDTO> RIRArr = shareService.RIReplyView(bno);
        model.addAttribute("RIR", RIRArr);
        return "Board/Share/RIReply";
    }

    // 분양 리뷰 쓰기
    @GetMapping("/ShareReply")
    @ResponseBody
    public String ShareReview(@RequestParam("btitle")String btitle, @RequestParam("bcontents")String bcontents, @RequestParam("addrx")String addrx, @RequestParam("addry")String addry, @RequestParam("agreedate")String agreedate, @RequestParam("code")String code) {
        HttpSession session = request.getSession();
        MemberDTO logSession = (MemberDTO)session.getAttribute("loginDTO");
        if(logSession.getMid() != null) {
            boolean result = shareService.ReviewWrite(btitle, bcontents, addrx, addry, agreedate, code);
            if (result) {
                return "1";
            } else {
                return "2";
            }
        } else {
            return "3";
        }
    }

    // 분양 대댓글
    @GetMapping("/RIReply")
    @ResponseBody
    public String RIReply(@RequestParam("bno")int bno, @RequestParam("RIReply")String RIReply) {
        boolean result = shareService.RIReplyWrite(bno, RIReply);
        if(result) {
            return "1";
        } else {
            return "2";
        }
    }

    // 분양 리뷰 출력
    @GetMapping("/ShareReviewView")
    public String ShareReviewView(Model model) {

        return "Board/Share/ShareSidebar";
    }

    @GetMapping("/ShareSidebar")
    public String goToShareSidebar() {
        return "Board/Share/ShareSidebar";
    }

    // 유기게시판으로
    @GetMapping("/LossBoardlist")
    public String goToLossBoardList(Model model, @RequestParam(defaultValue = "1") int page) {

        String sex = request.getParameter("sex"); // 성별
        String kind = request.getParameter("kind"); // 축종
        String city = request.getParameter("city"); // 시군구
        String state = request.getParameter("state"); // 시군구
        HttpSession session = request.getSession();

        if (sex != null || kind != null || city != null || state != null) {
            session.setAttribute("sex", sex);
            session.setAttribute("kind", kind);
            session.setAttribute("city", city);
            session.setAttribute("state", state);
        } else {
            sex = (String) session.getAttribute("sex");
            kind = (String) session.getAttribute("kind");
            city = (String) session.getAttribute("city");
            state = (String) session.getAttribute("state");
        }


        ArrayList<LossDTO> parses = lossService.losslist(sex, kind, city, state); // 필터링 게시물
        ArrayList<LossDTO> parsesPage = lossService.parsenum(parses, page); // 페이징

        Pagination1 pagination = new Pagination1(parses.size(), page);

        model.addAttribute("parsesPage", parsesPage);
        model.addAttribute("pagination", pagination);
        return "Board/Loss/LossBoardlist";
    }

    // 공지게시판으로
    @GetMapping("/LossBoardNotice")
    public String goToLossBoardNotice(Model model, @RequestParam(defaultValue = "1") int page) {

        String sex = request.getParameter("sex"); // 성별
        String kind = request.getParameter("kind"); // 축종
        String city = request.getParameter("city"); // 시군구
        String state = request.getParameter("state"); // 시군구
        HttpSession session = request.getSession();

        if (sex != null || kind != null || city != null || state != null) {
            session.setAttribute("sex", sex);
            session.setAttribute("kind", kind);
            session.setAttribute("city", city);
            session.setAttribute("state", state);
        } else {
            sex = (String) session.getAttribute("sex");
            kind = (String) session.getAttribute("kind");
            city = (String) session.getAttribute("city");
            state = (String) session.getAttribute("state");
        }

        ArrayList<LossDTO> parses = lossService.lossnoticelist(sex, kind, city, state); // 필터링 게시물
        ArrayList<LossDTO> parsesPage = lossService.parsenum(parses, page); // 페이징

        Pagination1 pagination = new Pagination1(parses.size(), page);

        model.addAttribute("parsesPage", parsesPage);
        model.addAttribute("pagination", pagination);
        return "Board/Loss/LossBoardNotice";
    }

    // 상세페이지로
    @GetMapping("/Board/Loss/LossBoardView/{ABDM_IDNTFY_NO}")
    public String goToLossBoardView(Model model, @PathVariable("ABDM_IDNTFY_NO") String ABDM_IDNTFY_NO) {
        ArrayList<LossDTO> lossDTOS = lossService.getlossboard(ABDM_IDNTFY_NO);
        // 세션 호출
        HttpSession session = request.getSession();
        MemberDTO memberDto = (MemberDTO) session.getAttribute("loginDTO");
        String apikey = lossDTOS.get(0).getABDM_IDNTFY_NO();
        int cano = 1;

        // 해당 게시물 댓글 호출
        List<BoardEntity> replyEntities = lossService.getreplylist(apikey, cano);

        model.addAttribute("loginDTO",memberDto);
        model.addAttribute("lossDTOS", lossDTOS);
        model.addAttribute("replyEntities", replyEntities);
        return "Board/Loss/LossBoardView";
    }

    // 댓글 작성
    @GetMapping("/replywrite")
    @ResponseBody
    public String replywrite(@RequestParam("apikey") String apikey,
                             @RequestParam("cano") int cano,
                             @RequestParam("rcontents") String rcontents,Model model) {
        HttpSession session = request.getSession();
        MemberDTO memberDto = (MemberDTO) session.getAttribute("loginDTO");

        // 로그인 안되어 있을 경우
        if (memberDto == null) {
            return "2";
        } else {
            lossService.replywrite(apikey, cano, rcontents, memberDto.getMno());
            return "1";
        }
    }

    // 댓글 삭제
    @GetMapping("/replydelete")
    @ResponseBody
    public int replydelete(@RequestParam("bno") int bno) {

        lossService.replydelete(bno);
        return 1;
    }

    // 댓글 수정
    @GetMapping("/replyupdate")
    @ResponseBody
    public String replyupdate(@RequestParam("bno") int bno, @RequestParam("newcontents") String newcontents){
        lossService.replyupdate(bno, newcontents);
        return "1";
    }
}