package ZooZoo.Service.Free;

import ZooZoo.Domain.DTO.Member.MemberDTO;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardImgEntity;
import ZooZoo.Domain.Entity.Board.BoardImgRepository;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.Category.CategoryRepository;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import ZooZoo.Service.BoardLike.BoardLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FreeBoardService {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardImgRepository bimgRepository;
    @Autowired
    BoardLikeService boardLikeService;
    @Autowired
    private HttpServletRequest request;


    //자유게시판 (카테고리 넘버 4번) (리스트) 가져오기(페이징,검색 처리)
    public Page<BoardEntity> GetAll(Pageable pageable, String keyword, String search) {

        int page =0;
        int categoryNumber = 4;
        if(pageable.getPageNumber() == 0) {  page=0; }
        else {  page=pageable.getPageNumber()-1; }
        pageable = PageRequest.of(page,12, Sort.by(Sort.Direction.DESC,"bno"));

        //작성자가 게시판 엔티티에 없기 때문에 bwriterno
        int mno = 0;
        String bwriter = "";
        if(keyword != null && keyword.equals("bwriter")){
            List<BoardEntity> boardEntities = boardRepository.findFreeBoardByCategory(categoryNumber);
            for(int i=0; i<boardEntities.size(); i++){
                //System.out.println("@@@@@@@@@@@@@@@@@@@@@ getMid() : " +boardEntities.get(i).getMemberEntity().getMid());
                bwriter = boardEntities.get(i).getMemberEntity().getMid();
                //System.out.println("@@@@@@@@@@@ search : " + search);
                if(search.equals(bwriter)){
                    mno = boardEntities.get(i).getMemberEntity().getMno();
                    //System.out.println("--------------------------------------같음------------------------------------");
                    return boardRepository.findAllMno(pageable, categoryNumber, mno);

                }
            }
        }
        if(keyword != null && keyword.equals("btitle")) return boardRepository.findAllTitles(pageable, categoryNumber, search);
        return boardRepository.findAllBoard(pageable, categoryNumber);

    }


    //자유게시판 쓰기
    public String FreeBoardWrite(String btitle, String freebcontents2, List<MultipartFile> files) {
        //1. 게시판 엔티티에다가 카테고리, 멤버, 게시판 이미지 엔티티 주입 //저장을 해서 bno생성하고나서 bimg게시판 이미지 넣기
        BoardImgEntity boardImgEntity = new BoardImgEntity();
        HttpSession session = request.getSession();
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("loginDTO");
        if (memberDTO == null) {
            return "2";
        }
        int mno = memberDTO.getMno();
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(4);

        BoardEntity boardEntity = BoardEntity.builder()
                .bcontents(freebcontents2)
                .btitle(btitle)
                .memberEntity(memberEntity.get())
                .categoryEntity(categoryEntity.get())
                .boardImgEntities( new ArrayList<>() ) //현재 엔티티에 들어가는 값이 없어도 나중에 연결하려면 미리 메모리 할당해줘야됨 안그럼 계속 null로 들어감
                .build();
        int bno = boardRepository.save(boardEntity).getBno();
        String uuidfile = null;

        //멤버 엔티티에 게시판 엔티티 넣어주기
        memberEntity.get().getBoardEntities().add(boardEntity);
        //카테고리 엔티티에 게시판 엔티티 넣어주기
        categoryEntity.get().getBoardEntities().add(boardEntity);

       /* //첨부파일 폴더 생성하기 위해서
        String path = "C:\\FreeBoardIMG\\";
        File Folder = new File(path);

        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
                System.out.println("폴더가 생성되었습니다.");
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }else {
            System.out.println("이미 폴더가 생성되어 있습니다.");
        }*/

        if (files.size() != 0) {
            for (MultipartFile temp : files) {
                UUID uuid = UUID.randomUUID();
                //첨부파일이 비었을 때, 첨부파일 엔티티를 게시판엔티티에 넣어줘야하나요????? 그리고 그 반대도 해야하나요????
                if (temp.getOriginalFilename() == null || temp.getOriginalFilename().equals("")) {
                    return "1";
                } else {
                    uuidfile = uuid.toString() + "_" + temp.getOriginalFilename().replaceAll("_", "-");
                    String filepath = "C:\\Users\\504\\Desktop\\Spring_ZooZoo\\out\\production\\resources\\static\\IMG\\Board\\FreeBoardIMG\\" + uuidfile;
                    try {
                        temp.transferTo(new File(filepath));
                    } catch (Exception e) {
                        System.out.println("파일 저장 실패함" + e);
                    }

                    boardImgEntity = BoardImgEntity.builder()
                            .bimg(uuidfile)
                            .boardEntity(boardEntity)
                            .categoryEntity2(categoryEntity.get())
                            .build();

                    //게시판 엔티티에 게시판 이미지 엔티티 넣어주기
                    bimgRepository.save(boardImgEntity);

                    boardRepository.findById(bno).get().getBoardImgEntities().add(boardImgEntity); //get을 할 수 없음 null이라
                }
            }
            return "1";
        }
        return "1";
    }

    //자유게시판 상세페이지
    @Transactional
    public BoardEntity getFreeBoardView(int bno) {
        //Optional은 fk 못 가져옴
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        String date = entityOptional.get().getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 MMM dd일"));
        HttpSession session = request.getSession();
        if(session.getAttribute(bno+"") == null){
            entityOptional.get().setBview(entityOptional.get().getBview()+1);
            session.setAttribute(String.valueOf(bno),1);
            session.setMaxInactiveInterval(60*60*24);
        }


        return entityOptional.get();
    }

    //자유게시판 삭제
    public boolean deleteBoard(int bno) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(bno);
        if(boardOpt.get() != null){
            boardRepository.delete(boardOpt.get());
            return true;
        }else{
            return false;
        }
    }

    public boolean updateFreeBoard2(String bfile) {
        bimgRepository.delete( bimgRepository.findBybimg(bfile).get());
        return true;
    }
    //자유게시판 수정
    @Transactional
    public String updateFreeBoard(String bcontents, String btitle, int bno, List<MultipartFile> file2) {

        BoardEntity boardEntity= boardRepository.findById(bno).get();
        int bsize = boardEntity.getBoardImgEntities().size();
        int file2size = file2.size();
        boardEntity.setBcontents(bcontents);
        boardEntity.setBtitle(btitle);
        //원래 첨부파일이 없고, 새로운 첨부파일도 없을때 1반환 (글쓰기 완료)
        if(bsize == 0 && file2.size() == 0){  return "1";   }

        //로그인 세션값 없으면 2반환
        HttpSession session = request.getSession();
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("loginDTO");
        if (memberDTO == null) {
            return "2";
        }

        //원래 첨부파일이 없고 새로운 첨부파일이 있을 때
        else {
            for (MultipartFile temp : file2) {
                String uuidfile = null;
                UUID uuid = UUID.randomUUID();
                uuidfile = uuid.toString() + "_" + temp.getOriginalFilename().replaceAll("_", "-");
                String filepath = "C:\\Users\\504\\Desktop\\Spring_ZooZoo\\out\\production\\resources\\static\\IMG\\Board\\FreeBoardIMG\\" + uuidfile;
                try { temp.transferTo(new File(filepath));
                } catch (Exception e) { System.out.println("파일 저장 실패함" + e); }
                     BoardImgEntity boardImgEntity = BoardImgEntity.builder()
                            .bimg(uuidfile)
                            .boardEntity(boardEntity)
                             .categoryEntity2(boardEntity.getCategoryEntity())
                            .build();
                    bimgRepository.save(boardImgEntity);
                    boardRepository.findById(bno).get().getBoardImgEntities().add(boardImgEntity); //get을 할 수 없음 null이라

            }
            return "1";
        }


    }

    //첨부파일 개별삭제
    public boolean deleteBoardImg(int bno, String bimg) {
        Optional<BoardImgEntity> s = bimgRepository.findBybimg(bimg);
        bimgRepository.delete(s.get());
        return true;
    }

    //첨부파일 다운로드
    public void freeBoardFileDown(String bimg, HttpServletResponse response) {
        String path = "C:\\Users\\504\\Desktop\\Spring_ZooZoo\\out\\production\\resources\\static\\IMG\\Board\\FreeBoardIMG\\" + bimg;

        File file = new File(path);
        //파일 이미지가 있으면
        if(file.isFile()) {
            try {
                //다운로드 html [창]
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode( bimg.split("_")[1],"UTF-8")); //다운로드
                OutputStream OutStream = response.getOutputStream();
                //내보내기 할 대상의 파일 읽어오기
                FileInputStream InStream = new FileInputStream(file);
                //읽어오기 시작
                int read = 0;
                byte[] buffer = new byte[1024 * 1024]; //읽어올 바이트 배열
                while ((read = InStream.read(buffer)) != -1) { // -1 : 읽어올게(byte) 없다, [.read] -> 파일읽기
                    //파일 쓰기 [.write]
                    OutStream.write(buffer, 0, read);
                }
            } catch (Exception e) {  System.out.println("첨부파일 다운로드 오류 발생");  }
        }else{   System.out.println("첨부파일이 없어용"); }
    }




//    @Transactional
//    public boolean updateFreeBoard(BoardDTO build) {
//        try{
//            Optional<BoardEntity> boardEntity = boardRepository.findById(build.getBno());
//            Optional<CategoryEntity> cEntityOpt = categoryRepository.findById(4);
//            boardEntity.get().getBoardImgEntities();
//            boardEntity.get().setBtitle(build.getBtitle());
//            boardEntity.get().setBcontents(build.getBcontents());
//            for(int i=0; i<build.getBfile().size(); i++){
//                System.out.println("%%게시판엔티티 :"+boardEntity.get().getBoardImgEntities().get(i));
//                System.out.println("%%build : "+build.getBfile().get(i));
//
//                //엔티티도 null이 아니고 bfile(새로운첨부파일)이 null이 아닐때 첨부파일 바꿔주기
//                if(build.getBfile().get(i)!=null || boardEntity.get().getBoardImgEntities().get(i) != null || !build.getBfile().get(i).equals("") || !boardEntity.get().getBoardImgEntities().equals("") )
//                {
//                    boardEntity.get().getBoardImgEntities().get(i).setBimg(build.getBfile().get(i));
//                    System.out.println(boardEntity.get().getBoardImgEntities().get(i));
//
//                //엔티티는 null(기존첨부파일)이고 build.getBfile(새로운 첨부파일)이 있으면 새로운 첨부파일 저장
//                }else if(boardEntity.get().getBoardImgEntities().get(i) == null && build.getBfile().get(i) !=null){
//                    BoardImgDTO boardImgDTO = BoardImgDTO.builder()
//                            .bimg(build.getBfile().get(i))
//                            .bno(build.getBno())
//                            .build();
//                    BoardImgEntity boardImgEntity = boardImgDTO.toBoardImgEntity();
//                    boardImgEntity.setBoardEntity(boardEntity.get());
//                    boardImgEntity.setCategoryEntity2(cEntityOpt.get());
//                    boardEntity.get().setBoardImgEntities(boardImgEntity.getBoardEntity().getBoardImgEntities());
//                    System.out.println("%%%엔티티가 null이고 새로운 첨부파일이 있을 떄 boardEntity : "+boardEntity.get());
//                    bimgRepository.save(boardImgEntity);
//                //엔티티가 null이 아니고 첨부파일이 없으면 남는 기존 첨부파일을 삭제하기
//                }else{
//                    bimgRepository.delete(boardEntity.get().getBoardImgEntities().get(i));
//                }
//
//            }
//            return  true;
//        }catch(Exception e){
//            System.out.println(e);
//            return false;
//        }
//    }

}
