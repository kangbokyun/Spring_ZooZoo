package ZooZoo.Service.Loss;

import ZooZoo.Domain.DTO.Board.BoardDTO;
import ZooZoo.Domain.DTO.Board.LossDTO;
import ZooZoo.Domain.DTO.Pagination;
import ZooZoo.Domain.DTO.Pagination1;
import ZooZoo.Domain.Entity.Board.BoardEntity;
import ZooZoo.Domain.Entity.Board.BoardRepository;
import ZooZoo.Domain.Entity.Board.LossEntity;
import ZooZoo.Domain.Entity.Board.LossRepository;
import ZooZoo.Domain.Entity.Category.CategoryEntity;
import ZooZoo.Domain.Entity.Category.CategoryRepository;
import ZooZoo.Domain.Entity.Member.MemberEntity;
import ZooZoo.Domain.Entity.Member.MemberRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.transaction.Transactional;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LossService {

    public ArrayList<LossDTO> gettot() {
        // API 전체 데이터 (리스트 안에 리스트)
        ArrayList<ArrayList<LossDTO>> lists = new ArrayList<>();
        // API 전체 데이터 (리스트 안의 리스트 데이터를 전부 풀어 다시 저장)
        ArrayList<LossDTO> finallist = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            ArrayList<LossDTO> lossDTOS = totget(i); // API 1회분 호출
            if (lossDTOS.size() != 0) {
                lists.add(lossDTOS); // 리스트에 저장
            }
        }

        for (int j = 0; j < lists.size(); j++) {
            ArrayList<LossDTO> getlist = lists.get(j);
            for (int i = 0; i < lists.get(j).size(); i++) {
                LossDTO lossDTO = LossDTO.builder()
                        .SIGUN_CD(getlist.get(i).getSIGUN_CD())
                        .SIGUN_NM(getlist.get(i).getSIGUN_NM())
                        .ABDM_IDNTFY_NO(getlist.get(i).getABDM_IDNTFY_NO())
                        .THUMB_IMAGE_COURS(getlist.get(i).getTHUMB_IMAGE_COURS())
                        .RECEPT_DE(getlist.get(i).getRECEPT_DE())
                        .newRECEPT_DE(getlist.get(i).getNewRECEPT_DE())
                        .DISCVRY_PLC_INFO(getlist.get(i).getDISCVRY_PLC_INFO())
                        .SPECIES_NM(getlist.get(i).getSPECIES_NM())
                        .details(getlist.get(i).getDetails())
                        .COLOR_NM(getlist.get(i).getCOLOR_NM())
                        .AGE_INFO(getlist.get(i).getAGE_INFO())
                        .BDWGH_INFO(getlist.get(i).getBDWGH_INFO())
                        .PBLANC_IDNTFY_NO(getlist.get(i).getPBLANC_IDNTFY_NO())
                        .PBLANC_BEGIN_DE(getlist.get(i).getPBLANC_BEGIN_DE())
                        .newPBLANC_BEGIN_DE(getlist.get(i).getNewPBLANC_BEGIN_DE())
                        .PBLANC_END_DE(getlist.get(i).getPBLANC_END_DE())
                        .newPBLANC_END_DE(getlist.get(i).getNewPBLANC_END_DE())
                        .IMAGE_COURS(getlist.get(i).getIMAGE_COURS())
                        .STATE_NM(getlist.get(i).getSTATE_NM())
                        .SEX_NM(getlist.get(i).getSEX_NM())
                        .NEUT_YN(getlist.get(i).getNEUT_YN())
                        .SFETR_INFO(getlist.get(i).getSFETR_INFO())
                        .SHTER_NM(getlist.get(i).getSHTER_NM())
                        .SHTER_TELNO(getlist.get(i).getSHTER_TELNO())
                        .PROTECT_PLC(getlist.get(i).getPROTECT_PLC())
                        .JURISD_INST_NM(getlist.get(i).getJURISD_INST_NM())
                        .CHRGPSN_NM(getlist.get(i).getCHRGPSN_NM())
                        .CHRGPSN_CONTCT_NO(getlist.get(i).getCHRGPSN_CONTCT_NO())
                        .PARTCLR_MATR(getlist.get(i).getPARTCLR_MATR())
                        .REFINE_LOTNO_ADDR(getlist.get(i).getREFINE_LOTNO_ADDR())
                        .REFINE_ROADNM_ADDR(getlist.get(i).getREFINE_ROADNM_ADDR())
                        .city(getlist.get(i).getCity())
                        .REFINE_ZIP_CD(getlist.get(i).getREFINE_ZIP_CD())
                        .REFINE_WGS84_LOGT(getlist.get(i).getREFINE_WGS84_LOGT())
                        .REFINE_WGS84_LAT(getlist.get(i).getREFINE_WGS84_LAT())
                        .build();
                finallist.add(lossDTO);
            }
        }

        int totcount = finallist.size(); // 데이터 전체 개수
        int totindex = (int) Math.ceil(totcount * 1.0 / 1000); // 데이터 인덱스 개수
        int totpage = (int) Math.ceil(totcount * 1.0 / 12); // 총 페이지 개수
        System.out.println("데이터 전체 개수 : " + totcount);
        System.out.println("데이터 인덱스 개수 : " + totindex);
        System.out.println("총 페이지 개수 : " + totpage);
        return finallist;
    }


    // 전체 리스트 - api 총 데이터
    public ArrayList<LossDTO> totget(int x) {

        ArrayList<LossDTO> lossDTOS = new ArrayList<>();

        try {
            // max page = 110
            String urlStr = "https://openapi.gg.go.kr/AbdmAnimalProtect?KEY=f116bb9347d04a38a639e01395505d21&pIndex=" + x + "&pSize=1000";
            // Instantiate the Factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(urlStr);

            // optional, but recommended
            doc.getDocumentElement().normalize();

            // get <staff>
            NodeList list = doc.getElementsByTagName("row");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                Element element = (Element) node;

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    // get text
                    String SIGUN_CD = element.getElementsByTagName("SIGUN_CD").item(0).getTextContent();
                    String SIGUN_NM = element.getElementsByTagName("SIGUN_NM").item(0).getTextContent();
                    String ABDM_IDNTFY_NO = element.getElementsByTagName("ABDM_IDNTFY_NO").item(0).getTextContent();
                    String THUMB_IMAGE_COURS = element.getElementsByTagName("THUMB_IMAGE_COURS").item(0).getTextContent();
                    // 접수일자 - 삽입
                    String RECEPT_DE = element.getElementsByTagName("RECEPT_DE").item(0).getTextContent();
                    StringBuffer newRECEPT_DE = null;
                    StringBuffer buffer = new StringBuffer(RECEPT_DE);
                    buffer.insert(4, "-");
                    buffer.insert(7, "-");
                    newRECEPT_DE = buffer;
                    String DISCVRY_PLC_INFO = element.getElementsByTagName("DISCVRY_PLC_INFO").item(0).getTextContent();
                    String STATE_NM = element.getElementsByTagName("STATE_NM").item(0).getTextContent();

                    String SPECIES_NM = element.getElementsByTagName("SPECIES_NM").item(0).getTextContent();
                    SPECIES_NM = SPECIES_NM.replace("[", "");
                    String result = SPECIES_NM.split("] ")[0];
                    String details = null;
                    try {
                        details = SPECIES_NM.split("]")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        details = "";
                    }
                    String COLOR_NM = element.getElementsByTagName("COLOR_NM").item(0).getTextContent();
                    String AGE_INFO = element.getElementsByTagName("AGE_INFO").item(0).getTextContent();
                    String BDWGH_INFO = element.getElementsByTagName("BDWGH_INFO").item(0).getTextContent();
                    String PBLANC_IDNTFY_NO = element.getElementsByTagName("PBLANC_IDNTFY_NO").item(0).getTextContent();
                    // 공고시작일자 - 삽입
                    String PBLANC_BEGIN_DE = element.getElementsByTagName("PBLANC_BEGIN_DE").item(0).getTextContent();
                    StringBuffer newPBLANC_BEGIN_DE = null;
                    StringBuffer buffer1 = new StringBuffer(PBLANC_BEGIN_DE);
                    buffer1.insert(4, "-");
                    buffer1.insert(7, "-");
                    newPBLANC_BEGIN_DE = buffer1;
                    // 공고종료일자 - 삽입
                    String PBLANC_END_DE = element.getElementsByTagName("PBLANC_END_DE").item(0).getTextContent();
                    StringBuffer newPBLANC_END_DE = null;
                    StringBuffer buffer2 = new StringBuffer(PBLANC_END_DE);
                    buffer2.insert(4, "-");
                    buffer2.insert(7, "-");
                    newPBLANC_END_DE = buffer2;
                    String IMAGE_COURS = element.getElementsByTagName("IMAGE_COURS").item(0).getTextContent();
                    // 성별 표기방식 변경
                    String SEX_NM = element.getElementsByTagName("SEX_NM").item(0).getTextContent();
                    String sex = null;
                    if (SEX_NM.equals("F")) {
                        sex = "암컷";
                    } else if (SEX_NM.equals("M")) {
                        sex = "수컷";
                    } else if (SEX_NM.equals("Q")) {
                        sex = "미상";
                    }
                    String NEUT_YN = element.getElementsByTagName("NEUT_YN").item(0).getTextContent();
                    String SFETR_INFO = element.getElementsByTagName("SFETR_INFO").item(0).getTextContent();
                    String SHTER_NM = element.getElementsByTagName("SHTER_NM").item(0).getTextContent();
                    String SHTER_TELNO = element.getElementsByTagName("SHTER_TELNO").item(0).getTextContent();
                    String PROTECT_PLC = element.getElementsByTagName("PROTECT_PLC").item(0).getTextContent();
                    String JURISD_INST_NM = element.getElementsByTagName("JURISD_INST_NM").item(0).getTextContent();
                    String CHRGPSN_NM = element.getElementsByTagName("CHRGPSN_NM").item(0).getTextContent();
                    String CHRGPSN_CONTCT_NO = element.getElementsByTagName("CHRGPSN_CONTCT_NO").item(0).getTextContent();
                    String PARTCLR_MATR = element.getElementsByTagName("PARTCLR_MATR").item(0).getTextContent();
                    String REFINE_LOTNO_ADDR = element.getElementsByTagName("REFINE_LOTNO_ADDR").item(0).getTextContent();
                    String REFINE_ROADNM_ADDR = element.getElementsByTagName("REFINE_ROADNM_ADDR").item(0).getTextContent();
                    String city = REFINE_ROADNM_ADDR.split(" ")[0];
                    String REFINE_ZIP_CD = element.getElementsByTagName("REFINE_ZIP_CD").item(0).getTextContent();
                    String REFINE_WGS84_LOGT = element.getElementsByTagName("REFINE_WGS84_LOGT").item(0).getTextContent();
                    String REFINE_WGS84_LAT = element.getElementsByTagName("REFINE_WGS84_LAT").item(0).getTextContent();

                    // dto에 값 넣기
                    LossDTO lossDTO = LossDTO.builder()
                            .SIGUN_CD(SIGUN_CD)
                            .SIGUN_NM(SIGUN_NM)
                            .ABDM_IDNTFY_NO(ABDM_IDNTFY_NO)
                            .THUMB_IMAGE_COURS(THUMB_IMAGE_COURS)
                            .RECEPT_DE(RECEPT_DE)
                            .newRECEPT_DE(newRECEPT_DE)
                            .DISCVRY_PLC_INFO(DISCVRY_PLC_INFO)
                            .SPECIES_NM(result)
                            .details(details)
                            .COLOR_NM(COLOR_NM)
                            .AGE_INFO(AGE_INFO)
                            .BDWGH_INFO(BDWGH_INFO)
                            .PBLANC_IDNTFY_NO(PBLANC_IDNTFY_NO)
                            .PBLANC_BEGIN_DE(PBLANC_BEGIN_DE)
                            .newPBLANC_BEGIN_DE(newPBLANC_BEGIN_DE)
                            .PBLANC_END_DE(PBLANC_END_DE)
                            .newPBLANC_END_DE(newPBLANC_END_DE)
                            .IMAGE_COURS(IMAGE_COURS)
                            .STATE_NM(STATE_NM)
                            .SEX_NM(sex)
                            .NEUT_YN(NEUT_YN)
                            .SFETR_INFO(SFETR_INFO)
                            .SHTER_NM(SHTER_NM)
                            .SHTER_TELNO(SHTER_TELNO)
                            .PROTECT_PLC(PROTECT_PLC)
                            .JURISD_INST_NM(JURISD_INST_NM)
                            .CHRGPSN_NM(CHRGPSN_NM)
                            .CHRGPSN_CONTCT_NO(CHRGPSN_CONTCT_NO)
                            .PARTCLR_MATR(PARTCLR_MATR)
                            .REFINE_LOTNO_ADDR(REFINE_LOTNO_ADDR)
                            .REFINE_ROADNM_ADDR(REFINE_ROADNM_ADDR)
                            .city(city)
                            .REFINE_ZIP_CD(REFINE_ZIP_CD)
                            .REFINE_WGS84_LOGT(REFINE_WGS84_LOGT)
                            .REFINE_WGS84_LAT(REFINE_WGS84_LAT)
                            .build();
                    lossDTOS.add(lossDTO);
                }
            }
            return lossDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 전체 리스트 (API 데이터 호출)
    public ArrayList<LossDTO> totlosslist() {

        ArrayList<LossDTO> lossDTOS = new ArrayList<>();

        try {
            // max page = 110
            String urlStr = "https://openapi.gg.go.kr/AbdmAnimalProtect?KEY=f116bb9347d04a38a639e01395505d21&pIndex=1&pSize=1000";
            // Instantiate the Factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(urlStr);

            // optional, but recommended
            doc.getDocumentElement().normalize();

            // get <staff>
            NodeList list = doc.getElementsByTagName("row");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                Element element = (Element) node;

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    // get text
                    String SIGUN_CD = element.getElementsByTagName("SIGUN_CD").item(0).getTextContent();
                    String SIGUN_NM = element.getElementsByTagName("SIGUN_NM").item(0).getTextContent();
                    String ABDM_IDNTFY_NO = element.getElementsByTagName("ABDM_IDNTFY_NO").item(0).getTextContent();
                    String THUMB_IMAGE_COURS = element.getElementsByTagName("THUMB_IMAGE_COURS").item(0).getTextContent();
                    // 접수일자 - 삽입
                    String RECEPT_DE = element.getElementsByTagName("RECEPT_DE").item(0).getTextContent();
                    StringBuffer newRECEPT_DE = null;
                    StringBuffer buffer = new StringBuffer(RECEPT_DE);
                    buffer.insert(4, "-");
                    buffer.insert(7, "-");
                    newRECEPT_DE = buffer;
                    String DISCVRY_PLC_INFO = element.getElementsByTagName("DISCVRY_PLC_INFO").item(0).getTextContent();
                    String STATE_NM = element.getElementsByTagName("STATE_NM").item(0).getTextContent();

                    String SPECIES_NM = element.getElementsByTagName("SPECIES_NM").item(0).getTextContent();
                    SPECIES_NM = SPECIES_NM.replace("[", "");
                    String result = SPECIES_NM.split("] ")[0];
                    String details = null;
                    try {
                        details = SPECIES_NM.split("]")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        details = "";
                    }
                    String COLOR_NM = element.getElementsByTagName("COLOR_NM").item(0).getTextContent();
                    String AGE_INFO = element.getElementsByTagName("AGE_INFO").item(0).getTextContent();
                    String BDWGH_INFO = element.getElementsByTagName("BDWGH_INFO").item(0).getTextContent();
                    String PBLANC_IDNTFY_NO = element.getElementsByTagName("PBLANC_IDNTFY_NO").item(0).getTextContent();
                    // 공고시작일자 - 삽입
                    String PBLANC_BEGIN_DE = element.getElementsByTagName("PBLANC_BEGIN_DE").item(0).getTextContent();
                    StringBuffer newPBLANC_BEGIN_DE = null;
                    StringBuffer buffer1 = new StringBuffer(PBLANC_BEGIN_DE);
                    buffer1.insert(4, "-");
                    buffer1.insert(7, "-");
                    newPBLANC_BEGIN_DE = buffer1;
                    // 공고종료일자 - 삽입
                    String PBLANC_END_DE = element.getElementsByTagName("PBLANC_END_DE").item(0).getTextContent();
                    StringBuffer newPBLANC_END_DE = null;
                    StringBuffer buffer2 = new StringBuffer(PBLANC_END_DE);
                    buffer2.insert(4, "-");
                    buffer2.insert(7, "-");
                    newPBLANC_END_DE = buffer2;
                    String IMAGE_COURS = element.getElementsByTagName("IMAGE_COURS").item(0).getTextContent();
                    // 성별 표기방식 변경
                    String SEX_NM = element.getElementsByTagName("SEX_NM").item(0).getTextContent();
                    String sex = null;
                    if (SEX_NM.equals("F")) {
                        sex = "암컷";
                    } else if (SEX_NM.equals("M")) {
                        sex = "수컷";
                    } else if (SEX_NM.equals("Q")) {
                        sex = "미상";
                    }
                    String NEUT_YN = element.getElementsByTagName("NEUT_YN").item(0).getTextContent();
                    String SFETR_INFO = element.getElementsByTagName("SFETR_INFO").item(0).getTextContent();
                    String SHTER_NM = element.getElementsByTagName("SHTER_NM").item(0).getTextContent();
                    String SHTER_TELNO = element.getElementsByTagName("SHTER_TELNO").item(0).getTextContent();
                    String PROTECT_PLC = element.getElementsByTagName("PROTECT_PLC").item(0).getTextContent();
                    String JURISD_INST_NM = element.getElementsByTagName("JURISD_INST_NM").item(0).getTextContent();
                    String CHRGPSN_NM = element.getElementsByTagName("CHRGPSN_NM").item(0).getTextContent();
                    String CHRGPSN_CONTCT_NO = element.getElementsByTagName("CHRGPSN_CONTCT_NO").item(0).getTextContent();
                    String PARTCLR_MATR = element.getElementsByTagName("PARTCLR_MATR").item(0).getTextContent();
                    String REFINE_LOTNO_ADDR = element.getElementsByTagName("REFINE_LOTNO_ADDR").item(0).getTextContent();
                    String REFINE_ROADNM_ADDR = element.getElementsByTagName("REFINE_ROADNM_ADDR").item(0).getTextContent();
                    String city = REFINE_ROADNM_ADDR.split(" ")[0];
                    String REFINE_ZIP_CD = element.getElementsByTagName("REFINE_ZIP_CD").item(0).getTextContent();
                    String REFINE_WGS84_LOGT = element.getElementsByTagName("REFINE_WGS84_LOGT").item(0).getTextContent();
                    String REFINE_WGS84_LAT = element.getElementsByTagName("REFINE_WGS84_LAT").item(0).getTextContent();

                    // dto에 값 넣기
                    LossDTO lossDTO = LossDTO.builder()
                            .SIGUN_CD(SIGUN_CD)
                            .SIGUN_NM(SIGUN_NM)
                            .ABDM_IDNTFY_NO(ABDM_IDNTFY_NO)
                            .THUMB_IMAGE_COURS(THUMB_IMAGE_COURS)
                            .RECEPT_DE(RECEPT_DE)
                            .newRECEPT_DE(newRECEPT_DE)
                            .DISCVRY_PLC_INFO(DISCVRY_PLC_INFO)
                            .SPECIES_NM(result)
                            .details(details)
                            .COLOR_NM(COLOR_NM)
                            .AGE_INFO(AGE_INFO)
                            .BDWGH_INFO(BDWGH_INFO)
                            .PBLANC_IDNTFY_NO(PBLANC_IDNTFY_NO)
                            .PBLANC_BEGIN_DE(PBLANC_BEGIN_DE)
                            .newPBLANC_BEGIN_DE(newPBLANC_BEGIN_DE)
                            .PBLANC_END_DE(PBLANC_END_DE)
                            .newPBLANC_END_DE(newPBLANC_END_DE)
                            .IMAGE_COURS(IMAGE_COURS)
                            .STATE_NM(STATE_NM)
                            .SEX_NM(sex)
                            .NEUT_YN(NEUT_YN)
                            .SFETR_INFO(SFETR_INFO)
                            .SHTER_NM(SHTER_NM)
                            .SHTER_TELNO(SHTER_TELNO)
                            .PROTECT_PLC(PROTECT_PLC)
                            .JURISD_INST_NM(JURISD_INST_NM)
                            .CHRGPSN_NM(CHRGPSN_NM)
                            .CHRGPSN_CONTCT_NO(CHRGPSN_CONTCT_NO)
                            .PARTCLR_MATR(PARTCLR_MATR)
                            .REFINE_LOTNO_ADDR(REFINE_LOTNO_ADDR)
                            .REFINE_ROADNM_ADDR(REFINE_ROADNM_ADDR)
                            .city(city)
                            .REFINE_ZIP_CD(REFINE_ZIP_CD)
                            .REFINE_WGS84_LOGT(REFINE_WGS84_LOGT)
                            .REFINE_WGS84_LAT(REFINE_WGS84_LAT)
                            .build();
                    lossDTOS.add(lossDTO);
                }
            }
            return lossDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 필터링(조회) 리스트
    public ArrayList<LossDTO> losslist(String sex, String kind, String city, String state) {
        ArrayList<LossDTO> totLosslist = totlosslist(); // 전체 리스트
        ArrayList<LossDTO> getlist = new ArrayList<>();

        try {
            if ((sex == null && kind == null && city == null && state == null) || (sex.equals("total") && kind.equals("total") && city.equals("total") && state.equals("total"))) {
                return totLosslist; // 초기화면 (검색 없음)
            }
            for (int i = 0; i < totLosslist.size(); i++) {
                if (sex != null && kind != null && city != null && state != null && totLosslist.get(i).getSEX_NM().equals(sex) && totLosslist.get(i).getSPECIES_NM().equals(kind) && totLosslist.get(i).getCity().equals(city) && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 전체 조건 검색
                } else if ((state == null || state.equals("total")) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i)); // 상태만 total
                } else if ((sex == null || sex.equals("total")) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && city != null && totLosslist.get(i).getCity().equals(city) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 성별만 total
                } else if ((kind == null || kind.equals("total")) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && city != null && totLosslist.get(i).getCity().equals(city) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 종류만 total
                } else if ((city == null || city.equals("total")) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 시군구만 total
                } else if (((state == null && sex == null) || (state.equals("total") && sex.equals("total"))) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i)); // 상태, 성별 total
                } else if (((state == null && kind == null) || (state.equals("total") && kind.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i)); // 상태, 종류 total
                } else if (((state == null && city == null) || (state.equals("total") && city.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind)) {
                    getlist.add(totLosslist.get(i)); // 상태, 시군구 total
                } else if (((kind == null && city == null) || (kind.equals("total") && city.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 종류, 시군구 total
                } else if (((sex == null && city == null) || (sex.equals("total") && city.equals("total"))) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i));  // 성별, 시군구 total
                } else if (((sex == null && kind == null) || (sex.equals("total") && kind.equals("total"))) && city != null && totLosslist.get(i).getCity().equals(city) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 성별, 종류 total
                } else if (((state == null && sex == null && kind == null) || (state.equals("total") && sex.equals("total") && kind.equals("total"))) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i));  //상태, 성별, 종류 total
                } else if (((state == null && sex == null && city == null) || (state.equals("total") && sex.equals("total") && city.equals("total"))) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind)) {
                    getlist.add(totLosslist.get(i));  //상태, 성별, 시군구 total
                } else if (((state == null && kind == null && city == null) || (state.equals("total") && kind.equals("total") && city.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex)) {
                    getlist.add(totLosslist.get(i)); //상태, 종류, 시군구 total
                } else if (((city == null && sex == null && kind == null) || (city.equals("total") && sex.equals("total") && kind.equals("total"))) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); //성별, 종류, 시군구 total
                }
            }
            return getlist;

        } catch (Exception e) {
        }
        return null;
    }

    // 메인 공고
    public ArrayList<LossDTO> totlossnotice() {
        ArrayList<LossDTO> totLosslist = totlosslist();
        ArrayList<LossDTO> totlossnotice = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        String current = format.format(time);
        Date today = null;
        System.out.println(totLosslist.size() + "진짜냐?");
        for (int i = 0; i < totLosslist.size(); i++) {
            try {
                today = format.parse(current);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date end = null;
            try {
                end = format.parse(totLosslist.get(i).getPBLANC_END_DE());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int result = today.compareTo(end);

            if (result < 0) {
                totlossnotice.add(totLosslist.get(i));
            }
        }
        System.out.println("확인1 : " + totlossnotice.size());
        return totlossnotice;
    }

    // 필터링(공지 중 동물 조회) 리스트
    public ArrayList<LossDTO> lossnoticelist(String sex, String kind, String city, String state) {
        ArrayList<LossDTO> totLosslist = totlossnotice(); // 전체 리스트
        ArrayList<LossDTO> getlist = new ArrayList<>();

        try {
            if ((sex == null && kind == null && city == null && state == null) || (sex.equals("total") && kind.equals("total") && city.equals("total") && state.equals("total"))) {
                return totLosslist; // 초기화면 (검색 없음)
            }
            for (int i = 0; i < totLosslist.size(); i++) {
                if (sex != null && kind != null && city != null && state != null && totLosslist.get(i).getSEX_NM().equals(sex) && totLosslist.get(i).getSPECIES_NM().equals(kind) && totLosslist.get(i).getCity().equals(city) && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 전체 조건 검색
                } else if ((state == null || state.equals("total")) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i)); // 상태만 total
                } else if ((sex == null || sex.equals("total")) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && city != null && totLosslist.get(i).getCity().equals(city) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 성별만 total
                } else if ((kind == null || kind.equals("total")) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && city != null && totLosslist.get(i).getCity().equals(city) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 종류만 total
                } else if ((city == null || city.equals("total")) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 시군구만 total
                } else if (((state == null && sex == null) || (state.equals("total") && sex.equals("total"))) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i)); // 상태, 성별 total
                } else if (((state == null && kind == null) || (state.equals("total") && kind.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i)); // 상태, 종류 total
                } else if (((state == null && city == null) || (state.equals("total") && city.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind)) {
                    getlist.add(totLosslist.get(i)); // 상태, 시군구 total
                } else if (((kind == null && city == null) || (kind.equals("total") && city.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 종류, 시군구 total
                } else if (((sex == null && city == null) || (sex.equals("total") && city.equals("total"))) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i));  // 성별, 시군구 total
                } else if (((sex == null && kind == null) || (sex.equals("total") && kind.equals("total"))) && city != null && totLosslist.get(i).getCity().equals(city) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); // 성별, 종류 total
                } else if (((state == null && sex == null && kind == null) || (state.equals("total") && sex.equals("total") && kind.equals("total"))) && city != null && totLosslist.get(i).getCity().equals(city)) {
                    getlist.add(totLosslist.get(i));  //상태, 성별, 종류 total
                } else if (((state == null && sex == null && city == null) || (state.equals("total") && sex.equals("total") && city.equals("total"))) && kind != null && totLosslist.get(i).getSPECIES_NM().equals(kind)) {
                    getlist.add(totLosslist.get(i));  //상태, 성별, 시군구 total
                } else if (((state == null && kind == null && city == null) || (state.equals("total") && kind.equals("total") && city.equals("total"))) && sex != null && totLosslist.get(i).getSEX_NM().equals(sex)) {
                    getlist.add(totLosslist.get(i)); //상태, 종류, 시군구 total
                } else if (((city == null && sex == null && kind == null) || (city.equals("total") && sex.equals("total") && kind.equals("total"))) && state != null && totLosslist.get(i).getSTATE_NM().contains(state)) {
                    getlist.add(totLosslist.get(i)); //성별, 종류, 시군구 total
                }
            }
            return getlist;

        } catch (Exception e) {
        }
        return null;
    }

    // 상세페이지
    public ArrayList<LossDTO> getlossboard(String ABDM_IDNTFY_NO) {
        ArrayList<LossDTO> totLosslist = totlosslist();
        ArrayList<LossDTO> getlossDTOS = new ArrayList<>();
        for (int i = 0; i < totLosslist.size(); i++) {
            if (totLosslist.get(i).getABDM_IDNTFY_NO().equals(ABDM_IDNTFY_NO)) {
                getlossDTOS.add(totLosslist.get(i));
            }
        }
        return getlossDTOS;
    }

    // 페이징 처리
    public ArrayList<LossDTO> parsenum(ArrayList<LossDTO> parses, int page) {
        ArrayList<LossDTO> parsepage = new ArrayList<>();
        Pagination1 pagination = new Pagination1();
        /*시작 페이지 값을 가져온다*/
        /*int page */
        /*화면에 뿌릴 페이지 사이즈 가져오기 */
        int pagesize = pagination.getPageSize();

        // 끝 페이지
        int maxPage = page * pagesize;

        // 시작페이지
        int minPage = (maxPage - pagesize) + 1;  // maxPage - maxpage-pagesize   1000 -
        // 전체 리스트의 사이즈의 갯수보다 maxPage가 크다면 maxPage를 parses.size()값을 줘서 값을 맞추는것임
        if (maxPage > parses.size()) {
            maxPage = parses.size();
            /*minPage = */
        }
        for (int i = minPage - 1; i < maxPage; i++) {
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
    public boolean replywrite(String apikey, int cano, String rcontents, int mno) {

        // 카테고리
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);

        // entity에 넣기
        BoardEntity boardEntity = BoardEntity.builder()
                .bcontents(rcontents)
                .apikey(apikey)
                .categoryEntity(categoryEntity.get())
                .memberEntity(memberEntity.get())
                .build();

        // 댓글 save
        boardRepository.save(boardEntity);
        return true;
    }


    // 해당 게시물 모든 댓글 출력
    @Transactional
    public List<BoardEntity> getreplylist(String apikey, int cano) {

        List<BoardEntity> replyEntities = new ArrayList<>();

        try {
            replyEntities = boardRepository.findAllReply(apikey, cano);

        } catch (Exception e) {
        }

        return replyEntities;
    }

    // 댓글 삭제
    @Transactional
    public boolean replydelete(int bno) {
        boardRepository.delete(boardRepository.findById(bno).get());
        return true;
    }

    // 댓글 수정
    @Transactional
    public boolean replyupdate(int bno, String newcontents) {
        // 댓글 가져오기
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        // 내용 수정
        boardEntity.setBcontents(newcontents);
        return true;
    }

    // 분양하기

}