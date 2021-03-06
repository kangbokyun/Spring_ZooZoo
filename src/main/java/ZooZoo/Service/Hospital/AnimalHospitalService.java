package ZooZoo.Service.Hospital;

import ZooZoo.Controller.Hospital.HospitalDto;
import ZooZoo.Domain.DTO.Pagination;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.Category.CategoryRepository;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import ZooZoo.Domain.Entity.Reply.ReplyEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalHospitalService {

    // 총 토탈 값 가져오는 함수
    public int getapitotal(){
        try {

            String asd2 [] = new String[2];
            String test[] = new String[2];
            URL url = new URL("https://openapi.gg.go.kr/Animalhosptl?KEY=ccec045fa339456eaefa46cb9c828bc8&Type=json&pIndex=1&pSize=1000");
            BufferedReader bf = new BufferedReader( new InputStreamReader( url.openStream() , "UTF-8") );
            String result = bf.readLine();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            JSONArray jsonArray = (JSONArray)jsonObject.get("Animalhosptl");
            JSONObject jsonObject3 = (JSONObject) jsonArray.get(0);
            JSONArray jsonArray2 = (JSONArray)jsonObject3.get("head");
            String asd = jsonArray2.get(0).toString();
            asd2 = asd.split(":");
            test = asd2[1].split("}");

            int total = Integer.parseInt(test[0]);
            return total;
        }catch(Exception e){
            return 0;
        }
    }

    // 토탈값에 맞게 array를 합친 함수
    public JSONArray getapi(int total){
        try {
            JSONArray arr = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONArray rowArray = new JSONArray();
            int defdt = total/1000;
            String s_defdt = null;

            for(int i = 1; i<=defdt+1; i++){
                s_defdt = Integer.toString(defdt);
                String s_i = Integer.toString(i);
                URL url = new URL("https://openapi.gg.go.kr/Animalhosptl?KEY=ccec045fa339456eaefa46cb9c828bc8&Type=json&pIndex="+s_i+"&pSize=1000");
                BufferedReader bf = new BufferedReader( new InputStreamReader( url.openStream() ,"UTF-8") );
                String result = bf.readLine();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject2 = (JSONObject)jsonParser.parse(result);
                JSONArray jsonArray = (JSONArray)jsonObject2.get("Animalhosptl");
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(1);
                rowArray = (JSONArray)jsonObject3.get("row");
                arr.addAll(rowArray);
            }
            return arr;
        }catch(Exception e){
            return null;
        }
    }

    // 검색 알고리즘 테스트 값 뽑아내기
    public ArrayList<HospitalDto> parseapisearch(JSONArray jsonArray, String keyword, String search, String status){
        JSONObject jsonObject = new JSONObject();
        ArrayList<HospitalDto> parse = new ArrayList<>();
        ArrayList<HospitalDto> parse2 = new ArrayList<>();
        String temp = "";
        try{
            for(int i = 0; i<jsonArray.size(); i++){
                jsonObject = (JSONObject)jsonArray.get(i);
                HospitalDto hospitalDto = new HospitalDto(
                        (String)jsonObject.get("BIZPLC_NM"), // 병원 이름
                        (String)jsonObject.get("REFINE_ROADNM_ADDR"), // 주소
                        (String)jsonObject.get("BSN_STATE_NM"),// 영업 상태명
                        (String)jsonObject.get("LOCPLC_FACLT_TELNO"), // 전화번호
                        (String)jsonObject.get("REFINE_WGS84_LOGT"), // 위도
                        (String)jsonObject.get("REFINE_WGS84_LAT"), // 경도
                        (String)jsonObject.get("SIGUN_NM"), // 시 정보
                        (String)jsonObject.get("SIGUN_CD"), // 시 정보코드
                        (String)jsonObject.get("LICENSG_DE") // 동물병원 인 허가 일자
                );

                /*정규식을 통한 알고리즘 수정 전화번호 변경하기 */
                if (hospitalDto.getLocplcfaclttelno() == null) {
                    hospitalDto.setLocplcfaclttelno("");
                }
                if (hospitalDto.getLocplcfaclttelno().length() == 8) {
                    hospitalDto.setLocplcfaclttelno(hospitalDto.getLocplcfaclttelno().replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2"));
                }else if (hospitalDto.getLocplcfaclttelno().length() == 12) {
                    hospitalDto.setLocplcfaclttelno(hospitalDto.getLocplcfaclttelno().replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3"));
                }else if(hospitalDto.getLocplcfaclttelno().length() == 7){
                    hospitalDto.setLocplcfaclttelno(hospitalDto.getLocplcfaclttelno().replaceFirst("^([0-9]{3})([0-9]{4})$", "$1-$2"));
                }
                hospitalDto.setLocplcfaclttelno(hospitalDto.getLocplcfaclttelno().replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3"));
                /*정규식을 통한 알고리즘 수정 전화번호 변경하기 end */
                parse.add(hospitalDto);
            }

            if(keyword != null || search != null || status != null){
                System.out.println(status);
                System.out.println(search);
                System.out.println(keyword);

                if(status.equals("")&&keyword.equals("")&&keyword.equals("")){
                    parse2.clear();
                    for(int i = 0; i<parse.size(); i++){ // 해당 크기만큼의 사이즈를 가지고와서
                        if(parse.get(i).getBsnstatenm().matches("(.*)"+"정상"+"(.*)")){ // 값이 일치한다면
                            HospitalDto hospitalDto = new HospitalDto(
                                    parse.get(i).getBizplcnm(), // 병원 이름
                                    parse.get(i).getRefineroadnmaddr(), // 주소
                                    parse.get(i).getBsnstatenm(),// 영업 상태명
                                    parse.get(i).getLocplcfaclttelno(), // 전화번호
                                    parse.get(i).getLogt(), // 위도
                                    parse.get(i).getLat(), // 경도
                                    parse.get(i).getSigunnm(), // 시 정보
                                    parse.get(i).getSiguncd(), // 시 정보코드
                                    parse.get(i).getLicensg_de()
                            );
                            System.out.println(parse.get(i).getLicensg_de());
                            parse2.add(hospitalDto);
                        }
                    }
                    return parse2;
                }
            if(status.equals("정상")) {
                if (keyword.equals("병원")) {
                    parse2.clear();
                    System.out.println(parse.size());
                    for (int i = 0; i < parse.size(); i++) { // 해당 크기만큼의 사이즈를 가지고와서
                        if (parse.get(i).getBizplcnm().matches("(.*)"+search+"(.*)")&&
                                parse.get(i).getBsnstatenm().matches("(.*)"+status+"(.*)")) { // 값이 일치한다면
                            HospitalDto hospitalDto = new HospitalDto(
                                    parse.get(i).getBizplcnm(), // 병원 이름
                                    parse.get(i).getRefineroadnmaddr(), // 주소
                                    parse.get(i).getBsnstatenm(),// 영업 상태명
                                    parse.get(i).getLocplcfaclttelno(), // 전화번호
                                    parse.get(i).getLogt(), // 위도
                                    parse.get(i).getLat(), // 경도
                                    parse.get(i).getSigunnm(), // 시 정보
                                    parse.get(i).getSiguncd(), // 시 정보코드
                                    parse.get(i).getLicensg_de()
                            );
                            parse2.add(hospitalDto);
                        }
                    }
                    return parse2;
                }
                if (keyword.equals("주소") || keyword == "주소") {
                    parse2.clear();
                    System.out.println(parse.size());
                    for (int i = 0; i < parse.size(); i++) { // 해당 크기만큼의 사이즈를 가지고와서
                        if(parse.get(i).getRefineroadnmaddr() == null || parse.get(i).getRefineroadnmaddr().equals("")){
                            parse.get(i).setRefineroadnmaddr("");
                        }
                        if (parse.get(i).getRefineroadnmaddr().matches("(.*)"+search + "(.*)")&&
                                parse.get(i).getBsnstatenm().matches("(.*)"+status+"(.*)")) { // 값이 일치한다면
                            HospitalDto hospitalDto = new HospitalDto(
                                    parse.get(i).getBizplcnm(), // 병원 이름
                                    parse.get(i).getRefineroadnmaddr(), // 주소
                                    parse.get(i).getBsnstatenm(),// 영업 상태명
                                    parse.get(i).getLocplcfaclttelno(), // 전화번호
                                    parse.get(i).getLogt(), // 위도
                                    parse.get(i).getLat(), // 경도
                                    parse.get(i).getSigunnm(), // 시 정보
                                    parse.get(i).getSiguncd(), // 시 정보코드
                                    parse.get(i).getLicensg_de() //동물병원 인허가 일자
                            );
                            parse2.add(hospitalDto);
                        }
                    }
                    return parse2;
                }
            }
            if(status.equals("폐업")) {
                if (keyword.equals("병원")) {
                    parse2.clear();
                    for (int i = 0; i < parse.size(); i++) { // 해당 크기만큼의 사이즈를 가지고와서
                        if (parse.get(i).getBizplcnm().matches("(.*)"+search+"(.*)")&&
                                parse.get(i).getBsnstatenm().matches("(.*)"+status+"(.*)")) { // 값이 일치한다면
                            HospitalDto hospitalDto = new HospitalDto(
                                    parse.get(i).getBizplcnm(), // 병원 이름
                                    parse.get(i).getRefineroadnmaddr(), // 주소
                                    parse.get(i).getBsnstatenm(),// 영업 상태명
                                    parse.get(i).getLocplcfaclttelno(), // 전화번호
                                    parse.get(i).getLogt(), // 위도
                                    parse.get(i).getLat(), // 경도
                                    parse.get(i).getSigunnm(), // 시 정보
                                    parse.get(i).getSiguncd(), // 시 정보코드
                                    parse.get(i).getLicensg_de() //동물병원 인허가 일자
                            );
                            parse2.add(hospitalDto);
                        }
                    }
                    return parse2;
                }
                if (keyword.equals("주소") || keyword == "주소") {
                    parse2.clear();
                    for (int i = 0; i < parse.size(); i++) { // 해당 크기만큼의 사이즈를 가지고와서

                        if(parse.get(i).getRefineroadnmaddr() == null || parse.get(i).getRefineroadnmaddr().equals("")){
                            parse.get(i).setRefineroadnmaddr("");
                        }
                        if (parse.get(i).getRefineroadnmaddr().matches( "(.*)"+search+"(.*)")&&
                                parse.get(i).getBsnstatenm().matches("(.*)"+status+"(.*)")) { // 값이 일치한다면

                            HospitalDto hospitalDto = new HospitalDto(
                                    parse.get(i).getBizplcnm(), // 병원 이름
                                    parse.get(i).getRefineroadnmaddr(), // 주소
                                    parse.get(i).getBsnstatenm(),// 영업 상태명
                                    parse.get(i).getLocplcfaclttelno(), // 전화번호
                                    parse.get(i).getLogt(), // 위도
                                    parse.get(i).getLat(), // 경도
                                    parse.get(i).getSigunnm(), // 시 정보
                                    parse.get(i).getSiguncd(), // 시 정보코드
                                    parse.get(i).getLicensg_de() //동물병원 인허가 일자
                            );
                            parse2.add(hospitalDto);
                        }
                    }
                    return parse2;
                }
            }
                if(status.equals("선택")) {
                    if (keyword.equals("병원")) {
                        parse2.clear();
                        for (int i = 0; i < parse.size(); i++) { // 해당 크기만큼의 사이즈를 가지고와서
                            if (parse.get(i).getBizplcnm().matches("(.*)"+search+"(.*)")) { // 값이 일치한다면
                                HospitalDto hospitalDto = new HospitalDto(
                                        parse.get(i).getBizplcnm(), // 병원 이름
                                        parse.get(i).getRefineroadnmaddr(), // 주소
                                        parse.get(i).getBsnstatenm(),// 영업 상태명
                                        parse.get(i).getLocplcfaclttelno(), // 전화번호
                                        parse.get(i).getLogt(), // 위도
                                        parse.get(i).getLat(), // 경도
                                        parse.get(i).getSigunnm(), // 시 정보
                                        parse.get(i).getSiguncd(), // 시 정보코드
                                        parse.get(i).getLicensg_de() //동물병원 인허가 일자
                                );
                                parse2.add(hospitalDto);
                            }
                        }
                        return parse2;
                    }
                    if (keyword.equals("주소")) {
                        parse2.clear();
                        for (int i = 0; i < parse.size(); i++) { // 해당 크기만큼의 사이즈를 가지고와서
                            if(parse.get(i).getRefineroadnmaddr() == null || parse.get(i).getRefineroadnmaddr().equals("")){
                                parse.get(i).setRefineroadnmaddr("");
                            }

                            if (parse.get(i).getRefineroadnmaddr().matches( "(.*)"+search+"(.*)")) { // 값이 일치한다면

                                HospitalDto hospitalDto = new HospitalDto(
                                        parse.get(i).getBizplcnm(), // 병원 이름
                                        parse.get(i).getRefineroadnmaddr(), // 주소
                                        parse.get(i).getBsnstatenm(),// 영업 상태명
                                        parse.get(i).getLocplcfaclttelno(), // 전화번호
                                        parse.get(i).getLogt(), // 위도
                                        parse.get(i).getLat(), // 경도
                                        parse.get(i).getSigunnm(), // 시 정보
                                        parse.get(i).getSiguncd(), // 시 정보코드
                                        parse.get(i).getLicensg_de() //동물병원 인허가 일자
                                );
                                parse2.add(hospitalDto);
                            }
                        }
                        return parse2;
                    }
                }

                return parse2;
            }
        }catch (Exception e){

        } return parse2;
    }

    // 내가 원하는 값 파싱해서 다 가져오기
    public ArrayList<HospitalDto> parseapi(JSONArray jsonArray){
        JSONObject jsonObject = new JSONObject();
        ArrayList<HospitalDto> parse = new ArrayList<>();
        try{
            for(int i = 0; i<jsonArray.size(); i++){
                jsonObject = (JSONObject)jsonArray.get(i);
                HospitalDto hospitalDto = new HospitalDto(
                        (String)jsonObject.get("BIZPLC_NM"), // 병원 이름
                        (String)jsonObject.get("REFINE_ROADNM_ADDR"), // 주소
                        (String)jsonObject.get("BSN_STATE_NM"),// 영업 상태명
                        (String)jsonObject.get("LOCPLC_FACLT_TELNO"), // 전화번호
                        (String)jsonObject.get("REFINE_WGS84_LOGT"), // 위도
                        (String)jsonObject.get("REFINE_WGS84_LAT"), // 경도
                        (String)jsonObject.get("SIGUN_NM"), // 시 정보
                        (String)jsonObject.get("SIGUN_CD"), // 시 정보코드
                        (String)jsonObject.get("LICENSG_DE") // 동물병원 인 허가 일자
                );
                parse.add(hospitalDto);
            }
            return parse;
        }catch (Exception e){

        } return parse;
    }

    // 페이징 처리 값 가져와서
    public ArrayList<HospitalDto> parsenum(ArrayList<HospitalDto> parses, int page){
        ArrayList<HospitalDto> parsepage = new ArrayList<>();
        Pagination pagination = new Pagination();
        /*시작 페이지 값을 가져온다*/
        /*int page */
        /*화면에 뿌릴 페이지 사이즈 가져오기 */
        int pagesize = pagination.getPageSize();

        // 끝 페이지
        int maxPage = page * pagesize;

        // 시작페이지
        int minPage = (maxPage-pagesize)+1;  // maxPage - maxpage-pagesize   1000 -

        // 전체 리스트의 사이즈의 갯수보다 maxPage가 크다면 maxPage를 parses.size()값을 줘서 값을 맞추는것임
        if(maxPage > parses.size()){
            maxPage = parses.size();
            /*minPage = */
        }

        for(int i = minPage-1; i<maxPage; i++){
            parsepage.add(parses.get(i));
        }

        return parsepage;
    }

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public boolean replywrite(String apikey, int cano, String rcontents, String bstar,int mno){
        // 카테고리
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);

        // entity에 넣기
        BoardEntity boardEntity = BoardEntity.builder()
                .bcontents(rcontents)
                .apikey(apikey)
                .bstar(bstar)
                .categoryEntity(categoryEntity.get())
                .memberEntity(memberEntity.get())
                .build();

        // 댓글 save
        boardRepository.save(boardEntity);
        return true;
    }

    /*리뷰 출력*/
    public List<BoardEntity> getreplylist(String apikey, int cano) {
        List<BoardEntity> replyEntities = new ArrayList<>();

        try {
            replyEntities = boardRepository.findAllReply(apikey, cano);

        } catch (Exception e) {
        }

        return replyEntities;
    }

    /*리뷰 평점 내기*/
    public double getreplyavg(String apikey, int cano){
        List<BoardEntity> replyEntities = new ArrayList<>();
        double avg;
        double d_avg = 0;

        try {
            replyEntities = boardRepository.findAllReply(apikey, cano);
            for(int i=0; i<replyEntities.size(); i++){
                String s_avg = replyEntities.get(i).getBstar();
                d_avg = Double.parseDouble(s_avg) + d_avg;
                System.out.println(i);
            }
            d_avg = d_avg/replyEntities.size();
            return d_avg;
        } catch (Exception e) {

        }
        return d_avg;
    }

    /*리뷰 삭제하기*/
    @Transactional
    public boolean deletereply(int bno){

        Optional<BoardEntity> replyEntity = boardRepository.findById(bno);

        if(replyEntity.get() != null){
            boardRepository.delete(replyEntity.get());
            return true;
        }else{
            return false;
        }
    }

    /*리뷰 수정하기 */
    @Transactional
    public boolean updatereply(int bno, String rcontents, String bstar){
        // 댓글 가져오기
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        System.out.println("bno : " + bno);
        System.out.println("내용 : " + rcontents);
        System.out.println(boardRepository.findById(bno).get());
        // 내용 수정
        boardEntity.setBcontents(rcontents);
        boardEntity.setBstar(bstar);

        return true;
    }

    /*평점 계산하는 함수 짜기 */

}