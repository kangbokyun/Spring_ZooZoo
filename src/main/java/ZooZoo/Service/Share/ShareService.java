package ZooZoo.Service.Share;

import ZooZoo.Domain.DTO.Board.BoardDTO;
import ZooZoo.Domain.DTO.Board.ShareDTO;
import ZooZoo.Domain.DTO.Member.MemberDTO;
import ZooZoo.Domain.DTO.Pagination;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.Category.CategoryRepository;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import org.apache.logging.log4j.util.PropertySource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShareService {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MemberRepository memberRepository;

    public ArrayList<String> Share(String area) {
        try {
            ArrayList<String> total = new ArrayList<>();
            for (int qq = 1; qq <= 8; qq++) {
                URL url = new URL("https://openapi.gg.go.kr/AnimalSale?Key=d33e0915e37c453abb4d9a94d8f265ed&Type=json&pIndex=" + qq + "&pSize=1000");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String result = bufferedReader.readLine();
                JSONParser jsonParser = new JSONParser(); // JSON을 쓰기 위해 사용
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result); // 오브젝트에 가져온 데이터 넣기
                JSONArray jsonArray = (JSONArray) jsonObject.get("AnimalSale"); // 오브젝트에 있는 데이터 배열에 넣기
                JSONObject object = (JSONObject) jsonArray.get(1); // head, row가 있는데 이 중 1번째(row)만 오브젝트에 넣기
                JSONArray arr = (JSONArray) object.get("row"); // row(key)의 값(value) 가져와 배열에 넣기
                ArrayList<String> address = new ArrayList<>();
                ArrayList<String> address2 = new ArrayList<>();
                ArrayList<String> addrX = new ArrayList<>();
                ArrayList<String> addrY = new ArrayList<>();
                ArrayList<String> addrpost = new ArrayList<>();
                ArrayList<String> oldaddress = new ArrayList<>();
                ArrayList<String> code = new ArrayList<>();
                ArrayList<String> agreedate = new ArrayList<>();
                ArrayList<String> tel = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> info = new ArrayList<>();
                if (area == null) {
                    for (int i = 0; i < arr.size(); i++) {
                        if (arr.get(i) != null) {
                            JSONObject obj = (JSONObject) arr.get(i); // 오브젝트에 i번째 데이터 넣기
                            JSONObject obj1 = (JSONObject) arr.get(i);
//                            System.out.println(obj);

                            if (obj.get("REFINE_ROADNM_ADDR") == null) {
                                address.add("정보없음");
                            } else {
                                address.add((String) obj.get("REFINE_ROADNM_ADDR"));
                            }
                            if (obj.get("ROADNM_ZIP_CD") == null) {
                                addrpost.add("정보없음");
                            } else {
                                addrpost.add((String) obj.get("ROADNM_ZIP_CD"));
                            }
                            if (obj.get("LOCPLC_FACLT_TELNO") == null) {
                                tel.add("정보없음");
                            } else {
                                tel.add((String) obj.get("LOCPLC_FACLT_TELNO"));
                            }
                            if (obj.get("BSN_STATE_DIV_CD") == null) {
                                code.add("정보없음");
                            } else {
                                code.add((String) obj.get("BSN_STATE_DIV_CD"));
                            }
                            if (obj.get("LICENSG_DE") == null) {
                                agreedate.add("정보없음");
                            } else {
                                agreedate.add((String) obj.get("LICENSG_DE"));
                            }
                            if (obj.get("REFINE_LOTNO_ADDR") == null) {
                                oldaddress.add("정보없음");
                            } else {
                                oldaddress.add((String) obj.get("REFINE_LOTNO_ADDR"));
                            }
                            if (obj.get("REFINE_WGS84_LAT") == null) {
                                addrX.add("정보없음");
                            } else {
                                addrX.add((String) obj.get("REFINE_WGS84_LAT"));
                            }
                            if (obj.get("REFINE_WGS84_LOGT") == null) {
                                addrY.add("정보없음");
                            } else {
                                addrY.add((String) obj.get("REFINE_WGS84_LOGT"));
                            }
                            if (obj.get("BIZPLC_NM") == null) {
                                name.add("정보없음");
                            } else {
                                name.add((String) obj.get("BIZPLC_NM"));
                            }

                            if (qq == 1) {
                                info.add((i + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
//                            System.out.println(info.size());
                            } else {
                                info.add((total.size() + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
                            }
                        }
                        total.add(info.get(i));
                    }
                } else {
                    int count = 0;
                    for (int i = 0; i < arr.size(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i); // 오브젝트에 i번째 데이터 넣기
                        JSONObject obj1 = (JSONObject) arr.get(i);

                        if (obj.get("REFINE_ROADNM_ADDR") == null) {
                            address.add("정보없음");
                        } else {
                            address.add((String) obj.get("REFINE_ROADNM_ADDR"));
                        }
                        if (obj.get("ROADNM_ZIP_CD") == null) {
                            addrpost.add("정보없음");
                        } else {
                            addrpost.add((String) obj.get("ROADNM_ZIP_CD"));
                        }
                        if (obj.get("LOCPLC_FACLT_TELNO") == null) {
                            tel.add("정보없음");
                        } else {
                            tel.add((String) obj.get("LOCPLC_FACLT_TELNO"));
                        }
                        if (obj.get("BSN_STATE_DIV_CD") == null) {
                            code.add("정보없음");
                        } else {
                            code.add((String) obj.get("BSN_STATE_DIV_CD"));
                        }
                        if (obj.get("LICENSG_DE") == null) {
                            agreedate.add("정보없음");
                        } else {
                            agreedate.add((String) obj.get("LICENSG_DE"));
                        }
                        if (obj.get("REFINE_LOTNO_ADDR") == null) {
                            oldaddress.add("정보없음");
                        } else {
                            oldaddress.add((String) obj.get("REFINE_LOTNO_ADDR"));
                        }
                        if (obj.get("REFINE_WGS84_LAT") == null) {
                            addrX.add("정보없음");
                        } else {
                            addrX.add((String) obj.get("REFINE_WGS84_LAT"));
                        }
                        if (obj.get("REFINE_WGS84_LOGT") == null) {
                            addrY.add("정보없음");
                        } else {
                            addrY.add((String) obj.get("REFINE_WGS84_LOGT"));
                        }
                        if (obj.get("BIZPLC_NM") == null) {
                            name.add("정보없음");
                        } else {
                            name.add((String) obj.get("BIZPLC_NM"));
                        }

                        if (address.get(i).equals("정보없음") && area == null) {
                            address2.add(address.get(i));
                            if (qq == 1) {
                                info.add((i + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
                            } else {
                                info.add((total.size() + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
                            }
                            total.add(info.get(count - 1));
                        }
                        if (address.get(i).contains(" " + area + " ")) {
                            address2.add(address.get(i));
                            if (qq == 1 && address.size() == 0) {
                                info.add((i + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
                                count += 1;
                            } else {
                                info.add((total.size() + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
                                count += 1;
                            }
                            total.add(info.get(count - 1));
                        }
                        if (area.contains("인천광역시") && address.get(i).contains("인천광역시 ")) {
                            address2.add("인천광역시");
                            if (qq == 1) {
                                info.add((i + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
                            } else {
                                info.add((total.size() + 1) + ":" + address.get(i) + ":" + name.get(i) + ":" + addrX.get(i) + ":" + addrY.get(i) + ":" + addrpost.get(i) + ":" + code.get(i) + ":" + agreedate.get(i) + ":" + oldaddress.get(i) + ":" + tel.get(i));
                            }
                            total.add(info.get(count - 1));
                        }
                    }
                }
            }
            return total;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    // 리뷰쓰기
    public boolean ReviewWrite(String btitle, String bcontents, String addrx, String addry, String agreedate, String code) {
        HttpSession session = request.getSession();
        MemberDTO getmem = (MemberDTO) session.getAttribute("loginDTO");
        Optional<MemberEntity> memberEntity = memberRepository.findById(getmem.getMno());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(2);
        String api = null;
        System.out.println("getMid : " + getmem.getMid());
        System.out.println("getMno : " + getmem.getMno());
        ArrayList<String> x1 = new ArrayList<>();
        ArrayList<String> y1 = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        ArrayList<String> y = new ArrayList<>();

        if (addrx == null || addrx.equals("정보없음") || addry == null || addry.equals("정보없음") || agreedate == null|| agreedate.equals("정보없음") ) {
            api = agreedate + code;
        } else {
            x.add(addrx.split("\\.")[0]);
            x1.add(addrx.split("\\.")[1]);
            y.add(addry.split("\\.")[0]);
            y1.add(addry.split("\\.")[1]);
            if ((Integer.parseInt(x1.get(0)) + Integer.parseInt(y1.get(0))) > 4) {
                int plusxy = Integer.parseInt((x.get(0) + y.get(0)) + 1);
                api = agreedate + (plusxy + "");
            } else {
                int plusxy = Integer.parseInt(x.get(0) + y.get(0));
                api = agreedate + (plusxy + "");
            }
        }

        BoardEntity boardEntity = BoardEntity.builder()
                .btitle(btitle)
                .bcontents(bcontents)
                .apikey(api)
                .categoryEntity(categoryEntity.get())
                .memberEntity(memberEntity.get())
                .build();

        boardRepository.save(boardEntity);
        return true;
    }

    // 리뷰출력
    public List<BoardDTO> ReviewView(String addrx, String addry, String agreedate, String code) {
        String api = null;
        ArrayList<String> x1 = new ArrayList<>();
        ArrayList<String> y1 = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        ArrayList<String> y = new ArrayList<>();
        if (addrx == null || addrx.equals("정보없음") || addry == null || addry.equals("정보없음") || agreedate == null|| agreedate.equals("정보없음") ) {
            api = agreedate + code;
        } else {
            x.add(addrx.split("\\.")[0]);
            x1.add(addrx.split("\\.")[1]);
            y.add(addry.split("\\.")[0]);
            y1.add(addry.split("\\.")[1]);

            if ((Integer.parseInt(x1.get(0)) + Integer.parseInt(y1.get(0))) > 4) {
                int plusxy = Integer.parseInt((x.get(0) + y.get(0)) + 1);
                api = agreedate + (plusxy + "");
            } else {
                int plusxy = Integer.parseInt(x.get(0) + y.get(0));
                api = agreedate + (plusxy + "");
            }
        }
        List<BoardEntity> getreview = boardRepository.findAll();
        List<BoardDTO> review = new ArrayList<>();
        for (BoardEntity boardEntity : getreview) {
            if (boardEntity.getApikey() != null && boardEntity.getApikey().equals(api)) {
                String mid = memberRepository.getById(boardEntity.getMemberEntity().getMno()).getMid();
                String date = boardEntity.getUpdateDate().toString().split("T")[0];
                String time1 = boardEntity.getUpdateDate().toString().split("T")[1];
                String time = time1.split("\\.")[0];
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                Date dd = new Date(); Date getT = null; Date getD = null;

                try {
                    getT = sdf2.parse(time);
                    getD = sdf2.parse(sdf2.format(dd));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int afterday = 0; String last = null;

		    long second = (getD.getTime() - getT.getTime())/1000;
		    long minute = (getT.getTime() - getD.getTime())/(60 * 1000);
		    long hour = (getT.getTime() - getD.getTime())/(60 * 60 * 1000);
                if(hour < 24 && Integer.parseInt(date.replace("-", "")) >= Integer.parseInt(sdf1.format(dd).replace("-", ""))) {
                    if(Integer.parseInt(Long.toString(minute).replace("-", "")) < 60) {
                        String change = Long.toString(minute).replace("-", "");
                        last = change + "분 전";
                    } else {
                        String change = Long.toString(hour).replace("-", "");
                        last = change + "시간 전";
                    }
                } else {
                    int afterday1 = Integer.parseInt(date.replace("-", "")) - Integer.parseInt(sdf1.format(dd).replace("-", ""));
                    String aft = Integer.toString(afterday1);
                    afterday = Integer.parseInt(aft.replace("-", ""));
                    last = afterday + "일 전";
                }
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setBno(boardEntity.getBno());

                if(boardEntity.getBtitle().length() > 10) {
                    String btitle = boardEntity.getBtitle().substring(0, 12) + "...";
                    boardDTO.setBtitle(btitle);
                } else {
                    boardDTO.setBtitle(boardEntity.getBtitle());
                }
                boardDTO.setBcontents(boardEntity.getBcontents());
                boardDTO.setBwriter(mid);
                boardDTO.setBcreateddate(last);
                review.add(boardDTO);
            }
        }

        List<BoardDTO> reviewView = new ArrayList<>();
        int j = review.size() - 1;
        for(int i = 0; i < review.size(); i++) {
            reviewView.add(review.get(j));
//            System.out.println("reviewView.get(i) : " + reviewView.get(j));
            j--;
        }

        return reviewView;
    }

    // 대댓글 쓰기
    public boolean RIReplyWrite(int bno, String RIReply) {
        HttpSession session = request.getSession();
        MemberDTO mno = (MemberDTO) session.getAttribute("loginDTO");
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno.getMno());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(2);
        BoardEntity board = BoardEntity.builder()
                .apikey(Integer.toString(bno))
                .bcontents(RIReply)
                .memberEntity(memberEntity.get())
                .categoryEntity(categoryEntity.get())
                .build();
        boardRepository.save(board);
        return true;
    }

    // 대댓글 출력
    public List<BoardDTO> RIReplyView(int bno) {
        List<BoardEntity> RIR = boardRepository.findAll();
        List<BoardDTO> RIRDTO = new ArrayList<>();
        for(BoardEntity boardEntity : RIR) {
            if(boardEntity.getApikey() != null && boardEntity.getApikey().equals(Integer.toString(bno))) {
                String date = boardEntity.getUpdateDate().toString().split("T")[0];
                String time1 = boardEntity.getUpdateDate().toString().split("T")[1];
                String time = time1.split("\\.")[0];
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                Date dd = new Date(); Date getT = null; Date getD = null;

                try {
                    getT = sdf2.parse(time);
                    getD = sdf2.parse(sdf2.format(dd));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int afterday = 0; String last = null;

                long second = (getD.getTime() - getT.getTime())/1000;
                long minute = (getT.getTime() - getD.getTime())/(60 * 1000);
                long hour = (getT.getTime() - getD.getTime())/(60 * 60 * 1000);
                if(hour < 24 && Integer.parseInt(date.replace("-", "")) >= Integer.parseInt(sdf1.format(dd).replace("-", ""))) {
                    if(Integer.parseInt(Long.toString(minute).replace("-", "")) < 60) {
                        String change = Long.toString(minute).replace("-", "");
                        last = change + "분 전";
                    } else {
                        String change = Long.toString(hour).replace("-", "");
                        last = change + "시간 전";
                    }
                } else {
                    int afterday1 = Integer.parseInt(date.replace("-", "")) - Integer.parseInt(sdf1.format(dd).replace("-", ""));
                    String aft = Integer.toString(afterday1);
                    afterday = Integer.parseInt(aft.replace("-", ""));
                    last = afterday + "일 전";
                }
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setBno(boardEntity.getBno());
                boardDTO.setBcontents(boardEntity.getBcontents());
                Optional<MemberEntity> mid = memberRepository.findById(boardEntity.getMemberEntity().getMno());
                boardDTO.setBwriter(mid.get().getMid());
                boardDTO.setBcreateddate(last);
                RIRDTO.add(boardDTO);
            }
        }
        return RIRDTO;
    }

    // 대댓글 창에 뿌려줄 댓글 내용
    public BoardDTO getReply(int bno) {
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        String date = boardEntity.getUpdateDate().toString().split("T")[0];
        String time1 = boardEntity.getUpdateDate().toString().split("T")[1];
        String time = time1.split("\\.")[0];
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        Date dd = new Date(); Date getT = null; Date getD = null;

        try {
            getT = sdf2.parse(time);
            getD = sdf2.parse(sdf2.format(dd));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int afterday = 0; String last = null;

        long second = (getD.getTime() - getT.getTime())/1000;
        long minute = (getT.getTime() - getD.getTime())/(60 * 1000);
        long hour = (getT.getTime() - getD.getTime())/(60 * 60 * 1000);
        if(hour < 24 && Integer.parseInt(date.replace("-", "")) >= Integer.parseInt(sdf1.format(dd).replace("-", ""))) {
            if(Integer.parseInt(Long.toString(minute).replace("-", "")) < 60) {
                String change = Long.toString(minute).replace("-", "");
                last = change + "분 전";
            } else {
                String change = Long.toString(hour).replace("-", "");
                last = change + "시간 전";
            }
        } else {
            int afterday1 = Integer.parseInt(date.replace("-", "")) - Integer.parseInt(sdf1.format(dd).replace("-", ""));
            String aft = Integer.toString(afterday1);
            afterday = Integer.parseInt(aft.replace("-", ""));
            last = afterday + "일 전";
        }
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBno(boardEntity.getBno());
        boardDTO.setBwriter(boardEntity.getMemberEntity().getMid());
        boardDTO.setBcontents(boardEntity.getBcontents());
        boardDTO.setBtitle(boardEntity.getBtitle());
        boardDTO.setBcreateddate(last);

        System.out.println("!@#!@!@#!@#!@#!#@!#@!@#" + boardDTO);

        return boardDTO;
    }

    // 댓글 수정
    @Transactional
    public boolean Update(int bno, String title, String contents) {
        Optional<BoardEntity> boardEntity = boardRepository.findById(bno);
        boardEntity.get().setBtitle(title);
        boardEntity.get().setBcontents(contents);
        return true;
    }

    // 대댓 삭제
    public boolean DeleteRIR(int bno) {
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        if(boardEntity != null) {
            boardRepository.deleteById(bno);
            return true;
        } else {
            return false;
        }
    }
}

