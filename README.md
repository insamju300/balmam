# 🦶발맘: 실시간 경로 녹화 미디어 촬영 연계 서비스
___
![](https://velog.velcdn.com/images/insamju300/post/213fdab4-4a98-45d7-8043-287abca930ff/image.png)






# 프로젝트 소개
___
* 발맘은 여행 경로를 실시간으로 추적하면서 멀티미디어 촬영과 위치 정보를 연결하여 제공합니다.



#  기술스택
___
- **개발 언어**
  - Java, Python
- **Front-end**
  - HTML, CSS, JavaScript, jQuery, Tailwind, daisyUI
  - API: Google Geocoding API,  Google Maps JavaScript API
- **Back-end**
  - 프레임워크 : SpringBoot, Selenium, FastAPI
  - 라이브러리 : Langchain, Lombok, Tomcat, spring-boot-starter-mail, ffmpeg, jackson
  - 템플릿 엔진 : Thymleaf
  - ORM : MyBatis
- **DB**
  - MySQL
  - 쿼리 브라우저 : SQLyog
- **버전 관리**
  - Git, GitHub
- **디자인**
  - Figma
- **개발 환경**
  - JDK-17, MAVEN, Spring Tool Suit 4, Visual Studio Code, Window 10

# 디자인
___
![](https://velog.velcdn.com/images/insamju300/post/0b5eec21-d468-4a07-bf07-dc44f276c683/image.png)
https://www.figma.com/design/HZJiBjPuZQ6CbJEOJezNmf/%EB%B0%9C%EB%A7%98?node-id=102%3A227&t=W4Ezb6oPzrFwOtsx-1



#   페이지별 기능
___
#### [메인화면]
![](https://velog.velcdn.com/images/insamju300/post/715d5bc3-32ea-4bb7-af1b-f43a865df5e8/image.png)

- 풀스크린 동영상으로 이루어진 메인페이지

#### [사이드 메뉴]
![](https://velog.velcdn.com/images/insamju300/post/840fb9fb-d343-4139-9e28-12effbeb929f/image.png)
- 마우스 hover시 글자가 적히는 듯한 애니메이션 효과 부여

#### [회원 가입]
![](https://velog.velcdn.com/images/insamju300/post/ef8e78e9-2765-4b2c-bfab-e81225b97ca2/image.png)
- 이미지 선택 기능과 유효성 체크 기능이 있습니다.

#### [이메일 인증]
![](https://velog.velcdn.com/images/insamju300/post/1feab127-6e04-49ca-ae48-05b78fa00f37/image.png)
- 회원가입이 완료되면 email로 메일이 발송되고, 해당 메일에서 인증을 완료 하여야 사이트에 접속할 수 있습니다.

#### [비밀번호 찾기]
![](https://velog.velcdn.com/images/insamju300/post/f974896f-3237-4676-a51a-5fc17cd58310/image.png)
- email을 입력하면 비밀번호를 변경할 수 있는 링크가 메일로 발송됩니다.

#### [로그인]
![](https://velog.velcdn.com/images/insamju300/post/5bb48bfb-9a84-4758-bab7-7c6a988e3ca8/image.png)
- 간단한 유효성 체크 기능이 있습니다.

#### [로그아웃]
![](https://velog.velcdn.com/images/insamju300/post/e80444a9-3518-4c76-a035-9cd87a02c466/image.png)


#### [회원정보 상세 보기]
![](https://velog.velcdn.com/images/insamju300/post/7deb0efc-c331-4bec-9347-d0417c62ac51/image.png)
- 사용자의 이미지, 닉네임, 자기 소개, 방문한 도시 수, 등록한 발자취 수를 표시합니다.

#### [회원정보 수정]
![](https://velog.velcdn.com/images/insamju300/post/c2ffb2a9-106b-4f75-bc52-dca6a8ef5046/image.png)

- 사용자의 이미지, 닉네임, 자기 소개를 변경할 수 있습니다. 비밀번호 변경은 변경 화면의 링크가 메일로 발송됩니다.


#### [경로 기록]
![](https://velog.velcdn.com/images/insamju300/post/b2427d6d-909e-4bb8-936a-1b89c833acbe/image.png)
- 유저의 현재 위치를 실시간으로 추적하여 경로를 선으로 표시합니다. 일시정지를 누르면 경로를 선으로 잇는 동작을 중지 하며, 다시 재생 버튼을 누르면 누른 시점 부터 새로운 선을 그립니다.

#### [미디어 촬영]
![](https://velog.velcdn.com/images/insamju300/post/2a4c1051-63ac-485e-be46-2952618d1c86/image.png)
- 사진 및 동영상을 촬영합니다. 촬영한 사진 및 동영상은 촬영한 시점의 좌표와 연결되어 지도에 표시됩니다.

#### [미디어 목록 보기]
![](https://velog.velcdn.com/images/insamju300/post/3508ba27-84f1-481f-85ca-07a5df79dbb6/image.png)
- 지도 상의 마크를 클릭 하면, 해당 위치에서 촬영한 사진 및 동영상 목록을 볼 수 있습니다.

#### [발자취 상세 정보 입력]
![](https://velog.velcdn.com/images/insamju300/post/50059e59-9f83-44ac-909c-f8ca8000a934/image.png)
- 경로 녹화 화면에서 정치 버튼을 누르면, 녹화된 경로를 바탕으로 제목, 태그, 대표 이미지를 선정하여 발자취를 등록할 수 있습니다. 태그는 최대 3개까지 등록 가능하며, 입력 후 enter을 입력하면 추가되고, 태그를 클릭하면 지워집니다.
촬영한 미디어 목록은 선택하여 삭제할 수 있습니다. 


#### [발자취 상세 정보 표시]
![](https://velog.velcdn.com/images/insamju300/post/12d70c81-5076-4e2f-9ecb-103162b43987/image.png)
- 발자취의 이름, 태그, 주요 방문 도시, 작성 시간, 조회수, 좋아요수, 북마크수, 좋아요 및 북마크 상태, 작성자의 닉네임, 작성자의 프로필 이미지, 녹화 시작 시간, 녹화 종료 시간, 총 녹화 시간, 실제 녹화시간이 표시됩니다.

#### [발자취 상세 정보 표시 - 유저 현재 위치 표시]
![](https://velog.velcdn.com/images/insamju300/post/72b8805c-d30a-4164-8daa-90d088c36fd8/image.png)
- 지도 우측의 마스코트 캐릭터를 클릭 하면 상세 정보를 보고 있는 유저의 현재 위치를 지도상에 표시하고 추적합니다. 

#### [발자취 상세 정보 표시 - 경로 재생 기능]
![](https://velog.velcdn.com/images/insamju300/post/48624ac1-f8dd-4776-b552-d294fb53e62e/image.png)
- 지도 우측의 카메라 버튼을 클릭하면 경로 녹화중 찍은 모든 미디어의 목록이 표시됩니다

#### [발자취 상세 정보 표시 - 전체 미디어 목록 보기]
![](https://velog.velcdn.com/images/insamju300/post/f59ae5e4-3bb8-464b-af12-6f7e40c64142/image.png)
- 지도 우측의 재생 버튼을 누르면 경로를 녹화한 순서로 선이 그어지는 애니메이션 효과가 실행됩니다.

#### [발자취 상세 정보 표시 - 좋아요 및 북마크 기능]
![](https://velog.velcdn.com/images/insamju300/post/eeb4cb69-c21f-4c55-83c2-6b5e9136d71c/image.png)
- 좋아요 및 북마크 버튼을 클릭하면 발자취에 좋아요나 북마크를 표시할 수 있습니다. 단 북마크를 따로 모아보는 기능은 미구현 상태 입니다.

#### [태그 및 도시 태그 기능]
![](https://velog.velcdn.com/images/insamju300/post/f05370e1-871a-4d2c-b124-6dedc69cccba/image.png)
- 발자취 상세 정보 페이지 및 발자취 리스트에서 태그 및 도시 태그를 클릭하면 해당 태그에 해당하는 일정 리스트를 보여줍니다

#### [발자취 목록]
![](https://velog.velcdn.com/images/insamju300/post/deb97367-9ad3-481e-a59a-61dcb96f50fe/image.png)
- 발자취 리스트를 무한 스크롤 형식으로 보여줍니다. 
  조회수, 좋아요 수, 북마크 수, 최신 수를 조합하여 정렬합니다.

#### [발자취 수정]
![](https://velog.velcdn.com/images/insamju300/post/88ec9833-016c-4d8d-a240-4c3bd94d1722/image.png)
- 등록한 발자취의 제목, 태그 수정, 미디어 삭제, 대표 이미지 변경이 발자취 수정 페이지에서 가능합니다.

#### [발자취 삭제]
![](https://velog.velcdn.com/images/insamju300/post/4fe6df2e-900b-4d2c-b87b-949509857dba/image.png)
- 확인 창이 나오고 삭제 버튼을 누르면 삭제가 완료됩니다.

#### [항공권 검색]
![](https://velog.velcdn.com/images/insamju300/post/ee4ff73a-d2f0-4286-bada-7140e3495ad5/image.png)
- 출발 공항, 도착 공항, 여행 기간을 입력해서 모든 항공권을 검색합니다.

#### [항공권 예매 링크 및 여행 플랜 추천 태그 입력]
![](https://velog.velcdn.com/images/insamju300/post/eabadd9d-cff6-4351-a361-486d92d121d5/image.png)
- 선택한 항공권의 네이버 항공 예매페이지 링크를 제공 하며, 여행 플랜을 추천 받기 위한 태그를 세개까지 입력받습니다.


#### [여행 플랜 추천]
![](https://velog.velcdn.com/images/insamju300/post/50366a8f-9122-4169-ac36-bf782eb648f3/image.png)

- 입력한 태그 및 항공권을 바탕으로 여행 계획을 시간 별로 작성하여 보여줍니다.


  


#   발맘 - 환경설정 가이드 북
---
### 1. 소스 코드 git
아래 소스들을 클론해 주세요.
스프링 서버 : https://github.com/insamju300/balmam.git
파이썬 서버 : https://github.com/insamju300/balmamPython.git


### 2. 프로젝트 실행에 필요한 프로그램  
[STS4](https://cdn.spring.io/spring-tools/release/STS4/4.22.0.RELEASE/dist/e4.31/spring-tool-suite-4-4.22.0.RELEASE-e4.31.0-win32.win32.x86_64.self-extracting.jar)  
[VSCode](https://code.visualstudio.com)  
[Xampp](https://www.apachefriends.org/)  
[SQLyog](https://s3.amazonaws.com/SQLyog_Community/SQLyog+13.2.1/SQLyog-13.2.1-0.x64Community.exe)  
[파이썬](https://www.python.org/ftp/python/3.12.3/python-3.12.3-amd64.exe)  


### 3. DB(DataBase) 세팅  
1. balman 폴더 내부의 DB.sql 파일의 텍스트를 전체 복사(ctrl+A)해주세요. 
![](https://velog.velcdn.com/images/insamju300/post/8f3bc462-2f34-478e-a195-0fe19a7b79e9/image.png)

2. SQLYog에서 해당 텍스트를 붙여넣고, 전체 쿼리를 실행(F9)해주세요.  
![](https://velog.velcdn.com/images/insamju300/post/908fcfb4-c3a1-4c77-a72c-8b721cd68d85/image.png)



### 4. Gmail 관련 환경 설정
 1. 시스템 환경 변수 편집을 열어주세요.

![](https://velog.velcdn.com/images/insamju300/post/fbe42e4f-9465-4eaa-a18c-3e74f408196a/image.png)

2. MAIL_USERNAME에 유저의 google email을 환경변수로,
   MAIL_PASSWORD에 유저의 google 패스워드를 환경변수로 지정해주세요.
![](https://velog.velcdn.com/images/insamju300/post/80e58ef3-e4bd-47fb-bb75-c2e3f041a72f/image.png)

3. 앱 비밀번호는 google 계정 -> 보안-> 2단계 인증에서 취득할 수 있습니다.
![](https://velog.velcdn.com/images/insamju300/post/54c8dd09-6c10-4181-bd4a-9c82c141fcd4/image.png)


### 5. API KEY 관련 설정
1. googleMap 키 설정을 위해 아래 파일명으로 아래 내용의 파일을 만들어 주세요.
   파일명: js_keys.js
   파일내용:
  ``` javascript
  function getGoogleMapKey(){
      return "your_googlemap_key";
  }

  ```
  배치 위치: balmam\src\main\resources\static
  ![](https://velog.velcdn.com/images/insamju300/post/8159c41b-5526-493e-86bc-36c9f93e2e5d/image.png)

2. OPENAI_API 키 설정을 위해 아래 파일명으로 아래 내용의 파일을 만들어 주세요.
   openai의 api키는 아래 사이트에서 얻을 수 있습니다.  
   https://openai.com/index/openai-api  
   파일명: .env
   파일내용:
  ``` python
    OPENAI_API_KEY=your_open_ai_api_key

  ```
  배치 위치: balmamPython
  ![](https://velog.velcdn.com/images/insamju300/post/836e774d-b94e-456b-8738-eab7b2221eb5/image.png)


### 6. 이미지 썸네일 추출을 위한 외부 프로그램 설정
1. [https://github.com/BtbN/FFmpeg-Builds/releases](https://github.com/BtbN/FFmpeg-Builds/releases) 에서 ffmpeg-master-latest-win64-gpl-shared.zip을 다운받아주세요.
![](https://velog.velcdn.com/images/insamju300/post/dc36e4db-677a-4d9f-8476-6296ab0f5bba/image.png)

2. 다음과 같은 디렉토리가 되게 위에서 다운받은 파일을 배치해주세요.
C:/files/balmam/tools/ffmpeg/bin/ffmpeg.exe
![](https://velog.velcdn.com/images/insamju300/post/887615e2-e60d-4076-8f23-a4a65b06e927/image.png)


### 7. Python fast api 설정
구동시 필요한 프로그램  
 - VSCode  
 - Python 3.12.3
 
1. VS code에서 FIle -> OpenFolder을 눌러주세요.![](https://velog.velcdn.com/images/insamju300/post/012aa05d-c29e-46fd-a718-458dfde72511/image.png)

2. 클론한 balmamPython 폴더를 선택해 주세요.
![](https://velog.velcdn.com/images/insamju300/post/c0c1a8d6-9d55-4f3a-b4cd-e2f38ec1630e/image.png)


3.  Terminal -> New Terminal을 선택해 주세요.  
![](https://velog.velcdn.com/images/insamju300/post/461927a7-69dc-4668-94d0-af79bd1ca386/image.png)  

4. 프로그램에 필요한 라이브 러리 파일들을 받아오기 위해 터미널에 다음 명령어 들을 입력해주세요. (Python이 설치되어 경로설정도 되어있는 걸 전제로 합니다.)  
pip install fastapi  
pip install selenium  
pip install langchain  
pip install python-dotenv  
pip install langchain_openai  
pip install bs4  
pip install lxml  
pip install unicorn

![](https://velog.velcdn.com/images/insamju300/post/815e117b-a67c-4e52-9a2a-76616b7b811b/image.png)  

5. 다음 명령어를 통해 fast api를 실행해 주세요.
uvicorn main:app --reload

![](https://velog.velcdn.com/images/insamju300/post/83cea1ed-3c32-4f47-939d-ef149990b7d3/image.png)
