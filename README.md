# ZooZoo Club  
경기도 유기동물, 분양동물 커뮤니티 사이트

### 시연영상 ([바로가기](https://https://www.youtube.com/watch?v=doywPxdSFEg)) 

## 1. 개요 
### 1.1 프로젝트 목적

 - 건강한 반려동물 사회 구축에 이바지 할 수 있는 정보 공유
 	- 1인 가구 증가로 인한 반려동물 픽업 빈도 증가 추세
 	- 책임감없이 반려동물을 데려오고 못키우겠어서 유기되는 동물의 수 증가 추세
 - 수업내용 개념 재확립 및 계층형 게시판 구조 설계와 구조에 맞는 게시글/댓글 CRUD 기능 구현

### 1.2 Target
- 유기동물 및 반려동물 입양을 원하는 고객
- 그 외 동물 관련 정보를 원하는 고객(동물병원정보, 입양후기 등)

### 1.3 개발환경 
- 운영체제 : Windows10
- Front-end : html, CSS, Bootstrap
- Back-end : JDK 11, Spring Framework
- Database : MySQL
- Server : Tomcat
- Version Control : Git

## 2. 개발일정

- 기간 : 2022.01.21 ~ 2022.02.21
- History  

|날짜|내용|
|---|---|
|2022.01.21|계획 설계|
|2022.01.24<br>~ 2022.01.26|레이아웃 설계 및 수정|
|2022.01.27|로그인, 회원가입, 아이디/비밀번호 찾기 설계 및 구현|
|2022.02.07|로그인, 회원가입, 아이디/비밀번호 찾기 설계 및 구현 마무리,  게시판 등록, 리스트, 상세보기 설계 및 구현|
|2022.02.08|게시판 등록, 리스트, 상세보기 설계 및 구현|
|2022.02.10<br>~ 2022.02.11|게시판 수정, 삭제, 검색 설계 및 구현|
|2022.02.15<br>~ 2022.02.18|세부기능 구현 및 오류 디버깅|
|2022.02.21|마무리 및 발표준비|
|2022.02.22|프로젝트 발표|

## 3. 주요기능 개발 우선순위
|우선순위|기능|
|---|---|
|1|Member : 로그인 / 회원가입 / 아이디찾기 / 비밀번호찾기<br>Board : 글 등록 / 글 상세보기 / 파일관리 / 관련 API 가져오기|
|2|Member : 내정보찾기 / 정보수정 / 회원탈퇴<br>Board : 글 수정 / 글 삭제 / 글 상세보기 / 관련 API 활용하기|
|3|Board : 페이징, 검색, 댓글|
|4|Board : 기타 세부기능 

## 4. 개발 인원 및 역할 분담
 - 강보균(조장) : 프로젝트 설계, 팀원 역할분담, 팀원 서포트, Readme 작성 
	 + Member : 회원가입, 로그인
	 + Board: 분양게시판
	 + others : 메인페이지
 - 장용범 : Readme 작성
 	+ Member : 비밀번호 찾기, 마이페이지
 	+ Board : 유기게시판
 	+ others : 메인페이지
 - 장희동
 	+ Member : 아이디 찾기
 	+ Board : 자유게시판
 - 최병호
 	+ Board : 병원게시판
 	+ others : 메인페이지
 - 공통 : 주제선정 / UI 설계 (Figma 정리) / DB 설계

## 5. 프로젝트 협업 규칙
### 5.1 명명규칙
- Project : PascalCase(UpperCamelCase) 적용
- Folder, File : PascalCase(UpperCamelCase) 적용
- Class, Interface : PascalCase(UpperCamelCase) 적용
- Method
	- 이동(Mapping) : goTo—
	- 컨트롤러(Mapping) : —Controller
- 주석 : Controller(Mapping) / Service(Method) 설명기재

### 5.2 코드공유방식 : GitHub, Kakaotalk
### 5.3 Font : 어그로체L
<!-- ### 5.4 application.properties

<details>
<summary>여기를 눌러주세요</summary>
<div markdown="1">       

```
server.port = 

spring.datasource..driver-class-name = 
com.mysql.cj.jdbc.Driver
spring.datasource.url = 
jdbc:mysql://localhost:3307/zoozoo?serverTimezone=Asia/Seoul
spring.datasource.username = root
spring.datasource.password = 1234

spring.jpa.hibernate.ddl-auto = update
spring.jpa.show_sql = true
#spring.jpa.properties.hibernate.format_sql = true
logging.level.org.hibernate.type.descriptor.sql = trace
spring.jpa.database.platform = 
org.hibernate.dialect.MySQL8Dialect

spring.devtools.restart.enabled = true
```
	
</div>
</details>

### 5.5 build.gradle

<details>
<summary>여기를 눌러주세요</summary>
<div markdown="1">       

```
스프링부트 제공
implementation 
'org.springframework.boot:spring-boot-starter-web' 

스프링부트 테스트
testImplementation 
'org.springframework.boot:spring-boot-starter-test' 

롬북 라이브러리 제공
compileOnly 
'org.projectlombok:lombok'

롬북 연결 라이브러리 제공
annotationProcessor 
'org.projectlombok:lombok' 

타임리프
implementation 
'org.springframework.boot:spring-boot-starter-thymeleaf' 

JPA사용
implementation 
'org.springframework.boot:spring-boot-starter-data-jpa' 

DB연동
runtimeOnly 'mysql:mysql-connector-java' 

자동빌드 라이브러리
developmentOnly 
'org.springframework.boot:spring-boot-devtools' 

API
implementation group:
'com.googlecode.json-simple', name: 'json-simple', version: '1.1.1'

채팅
implementation 
'org.springframework.boot:spring-boot-starter-websocket' 

크롤링
implementation 
'org.jsoup:jsoup:1.14.2' 
```

</div>
</details> -->


## 6. ERD
- Table : Total 6EA
<details>
<summary>여기를 눌러주세요</summary>
<div markdown="1">       

![image](https://user-images.githubusercontent.com/87436495/155252830-5f25632f-2045-4153-92e4-c120b55fea9b.png)
  
</div>
</details>

## 7. 메인기능 상세설명
### 7.1 옵션 및 키워드 검색 (동물병원 게시판)
<details>
<summary>여기를 눌러주세요</summary>
<div markdown="1">       

![image](https://user-images.githubusercontent.com/87436495/155255446-7c91f924-3985-4846-8546-76f2a9f20ace.png)
  
</div>
</details>

### 7.2 파일 첨부 Drag & Drop
<details>
<summary>여기를 눌러주세요</summary>
<div markdown="1">       

![image](https://user-images.githubusercontent.com/87436495/155256084-faf0a671-92b0-487e-b546-7836fb296ba2.png)
![image](https://user-images.githubusercontent.com/87436495/155256138-1413a73b-9dab-45ab-be01-f5112ee55983.png)
  
</div>
</details>

## 8. 보완할 점
- 모든 게시판에 무한 계층형 댓글 기능 추가 (현재 일부 페이지만 1depth까지 대댓글 구현)
- 분양동물 게시판 : 댓글 등록 후 대댓글 화면 출력오류 수정 (현재 페이지 Reload 후 대댓글 가능)

## 9. 후기
### 강보균
- API가 주가되는 게시판을 만들면서 DB와 Entity를 사용하지 않아 따라오는 어려움이 있었습니다. 데이터를 쉽게 가공하지 못하고 가공을 하려면 나름의 알고리즘을 a부터 z까지 짜서 사용했어야 해서 시간이 조금 더 걸렸었지만 수업시간에 진행하지 않았던 엔티티를 이용하지 않는 페이징, 시간출력 포맷, 대댓글 등 까지 생각해보고 설계하여 코드를 짜보는 귀중한 경험을 하고 한단계 발전하는 시간이 됐습니다.
### 장용범
- Open API의 데이터를 사용하면서 Entity를 사용하지 않는 부분에 대해 어려움이 있었습니다. 특히 다른 팀원들과 달리 API 타입이 xml이고 수업시간에도 다뤄보지 않아서 많은 어려움이 있었습니다. Open API 가공, Pagination 등 직접 알고리즘을 구축하고 기능을 구현하며 Open API에 대해 좀 더 공부할 수 있는 시간을 가질 수 있었습니다. 또한 계층형 게시판 구조로 설계하고 게시글과 댓글의 CRUD를 구현하면서 게시판에 대해서도 공부할 수 있는 귀중한 시간이었습니다.
### 장희동
- 계층형 DB를 접하면서, 이제까지 배웠던 것들보다 더 압축된 DB 디자인을 따르는 것이 처음이라 막막했습니다. 모르는 부분은 팀원들에게 물어보면서 최대한 제 스스로 해결하려고 노력했습니다. 또한 대댓글 기능을 구현하면서 실제 상용화되는 사이트들은 더욱이 견고하고 촘촘한 DB구조를 사용하고 있다는 점을 느끼게 되었습니다.
### 최병호
- 동물 병원 게시판을 진행하면서 API를 활용하여 데이터를 가공해서 써야 하는 경우가 대다수 였는데 직접 알고리즘을 짜서 페이지를 출력하는 과정을 통해서 게시판과 페이징에 대해 다시 되돌아보는 시간이 되었습니다.
또한 이번 프로젝트를 통해서 설계의 중요성을 다시 한번 돌아보게 되었는데 로직을 돌리는 과정이 많다 보니 페이지의 출력 부분이 느려지는 부분을 체감하여 항상 코드가 되기만 하는 과정보다는 어떻게 하면 코드를 조금 더 효율적으로 작성할 수 있을지에 대한 생각을 해보게 되었습니다.
