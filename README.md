<aside>

</aside>

# ❗️프로젝트 환경 구성

해당 프로젝트는 아래의 환경으로 구성되어 있습니다.

- kotlin `1.9.25`
- spring boot `3.4.3`
- Java `21` → Docker는 17
- postgres `15.12`
- JUnit `5.11.4`
- AssertJ Core `3.27.3`

---

**❓ spring boot `3.4.3` 을 선택한 이유**


  [Spring Initializr](https://start.spring.io/)에서 버전을 선택할 때, 정식 릴리즈된 Spring Boot 버전인 `3.4.3`과 `3.3.9` 중에서 고민했다. [**Spring Boot 3.4 Release Notes**](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes?utm_source=chatgpt.com)를 참고했을때, 3.4가 3.3으로부터 큰 변경 사항이 없기도 하고 3.4도 Kotlin 1.9를 지원하므로 OSS/Enterprise 지원 날짜가 더 많이 남은 `3.4.3` 을 선택했다.

  다만, `@ConfigurationProperties` 유효성 검사 부분이 변경되어 3.3 버전에서는 `@ConfigurationProperties` 클래스에서 중첩된 속성의 유효성 검사가 `@Valid` 여부와 상관없이 수행되었었는데, 3.4 버전부터는 Bean Validation 사양을 따르기 때문에 `@Valid`가 적용된 중첩 속성에 대해서만 유효성 검사가 수행된다고 한다.

  만약 필요한 정보(환경 변수, DB 정보 등)들을 properties 파일에 넣고 `@ConfigurationProperties`를 사용하여 클래스에 바인딩한다고 해보자. 클래스의 내부 객체에 `@Valid` 를 붙이지 않아도 3.3 버전에서는 내부 객체 안에 있는 속성에 `@NotNull` 같이 유효성 검사가 가능하지만, 3.4 버전은 중첩된 객체는 `@Valid`가 있어야 유효성 검사가 가능하다는 것이다.

  > **🪄 즉, 3.4 버전을 사용한다면, 중첩된 객체의 유효성 검사를 위해 `@Valid`를 반드시 명시해야 한다. 기존 코드에서 `@Valid`가 누락된 경우, 예상과 다르게 유효성 검사가 수행되지 않을 수 있으므로 주의해야 한다.**
>


<br><br>

## 실행 방법

`application.properties` 에 비어있는 값을 넣은 후, 아래 명령어로 실행

(저는 Local DB로 구현했으나, 실행의 편의성을 위해 Docker 실행 방법을 첨부합니다.)

```bash
docker compose up -d db
docker compose build
docker compose up aichatbot 
```

<br><br>

# **✅ 구현 체크리스트**

- [X] **사용자 관리 및 인증 기능**
    - [X]  회원 가입
        - 가입 시 받아야 할 필수 정보: 이메일, 패스워드, 이름
    - [X] 로그인
        - 로그인 시 받아야 할 필수 정보: 이메일, 패스워드
        - 로그인 완료 시 JWT(JSON Web Tokens) 형식의 토큰이 발급됩니다
    - [X] 인증
      - 회원가입 및 로그인을 제외한 모든 요청은 JWT 토큰을 확인하여 인증합니다.

- [X] **대화(chat) 관리 기능**
  - [X] 대화 생성
      - [X] 질문을 입력받고 생성된 답변을 응답합니다.
      - [X] 추가 옵션을 파라미터로 받을 수 있습니다
          - isStreaming(boolean): `true` 설정될 경우 stream 형태로 응답합니다.
          - model(string): 설정될 경우 해당 모델로 응답을 생성합니다.
  - [ ] 대화 목록 조회
      - [X] 요청한 유저의 모든 대화를 응답하는 기능입니다.
      - [X] 스레드 단위로 그룹화된 대화의 목록을 응답해야 합니다.
      - [ ] 각 유저는 자신이 생성한 스레드와 대화만 조회할 수 있으며, 관리자는 모든 스레드와 대화를 조회할 수 있습니다.
      - [X] 생성일시 기준으로 오름차순/내림차순 정렬이 가능하며 페이지네이션이 가능해야 합니다.
  - [X] 스레드 삭제
      - [X] 특정 스레드를 선택하여 삭제하는 기능입니다.
      - [X] 각 유저는 자신이 생성한 스레드만 삭제 가능합니다.

- [ ] 사용자 피드백 관리 기능
- [ ] 분석 및 보고 기능

<br><br>

# 💡 메인 유스케이스


<br><br>

# 🤔 개선이 필요한 부분

* 대화(chat) 관리의 대화 목록 조회 기능에서  `자신이 생성한 스레드와 대화만 조회`를 충족하지 못하였다. 만약 userId에 다른 사용자의 아이디를 넣어 요청하면 다른 사용자의 채팅 목록을 조회할 수 있게 된다.
* 사용자 피드백 관리 기능과 분석 및 보고 기능 구현이 필요하다.
* 예외 처리(Ex) 요청한 사용자가 자원의 소유주가 맞는지)가 부족하다.