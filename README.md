
![moyobom - 복사본](https://github.com/user-attachments/assets/acca31f3-3328-4ec4-bcb6-2818fdb3c31f)

# 모여봄

# 1. 팀원 소개
| 최건 | 임현조 | 홍도현 | 송현준 | 김민석 | 홍재민 |
|:------:|:------:|:------:|:------:|:------:|:------:|
| [GitHub](https://github.com/gjaku1031) | [GitHub](https://github.com/limhyunjo) | [GitHub](https://github.com/dh0522) | [GitHub](https://github.com/Hyeonjunnn) | [GitHub](https://github.com/mlnstone) | [GitHub](https://github.com/MSP-31) |
|  Backend<br>Devops | Backend<br>Frontend | Backend<br>Frontend | Backend<br>Frontend | Backend<br>Frontend | Backend<br>Frontend |

---
# 2. 기술 스택

### BackEnd
<div>
  <img src="https://img.shields.io/badge/Java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT"> <br>
  
  <img src="https://img.shields.io/badge/Spring%20Boot-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Security-%236DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/querydsl-0769AD?style=for-the-badge&logo=&logoColor=white">
</div>

### DataBase
<div>
  <img src="https://img.shields.io/badge/MariaDB-%23003545?style=for-the-badge&logo=mariadb&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-%23DC382D?style=for-the-badge&logo=redis&logoColor=white">
</div>


### Frontend

![Vue.js](https://img.shields.io/badge/vue.js-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)


### Devops
<div>
  <img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white">
  <img src="https://img.shields.io/badge/jenkins-%23d24939.svg?style=for-the-badge&logo=jenkins&logoColor=white">
  <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">   
</div>


### Tools
<div>
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white" alt="GitHub">  
</div>


---
# 3. 서비스 소개
## 3-1. 배경
현대 소프트웨어 개발 환경에서는 협업의 중요성이 날로 커지고 있습니다. 
특히 팀 프로젝트에서는 개개인의 기술적 역량만큼이나 팀원 간의 호흡과 시너지가 프로젝트의 
성공을 좌우합니다. 그러나 기존의 개발자 커뮤니티와 구인 플랫폼들은 주로 기술적 스펙과 
경력에만 초점을 맞추고 있어, 실제 함께 일할 사람들 간의 성향, 작업 방식, 커뮤니케이션 
스타일 등을 고려한 매칭이 어려웠습니다.
<br>
많은 개발자들이 "기술적으로는 우수하지만 협업이 힘들었던 경험" 또는 "기술 스택은 맞았지만 
프로젝트 진행 방식이 달라 어려움을 겪었던 경험"을 토로합니다. 이러한 문제를 해결하기 위해
, 단순한 기술 매칭을 넘어 실제로 '함께 일하기 좋은' 팀원을 찾을 수 있는 플랫폼의 필요성이
대두되었습니다.

## 3-2. 소개
Moyobom 프로젝트는 개발자들이 자신과 잘 맞는 팀원을 찾고, 함께 프로젝트를 진행할 수 
있도록 도와주는 개발자 매칭 서비스입니다. 본 서비스는 단순한 구인구직 플랫폼을 넘어, 
개발자들의 기술적 역량뿐만 아니라 작업 스타일, 커뮤니케이션 방식, 프로젝트 관리 성향 
등을 종합적으로 고려한 매칭 시스템을 제공합니다.

---
# 4. 기획
<details>
<summary> 요구사항 명세서 </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
[요구사항 명세서.pdf](https://github.com/user-attachments/files/19478785/2.-.pdf)
</details>
<details>
<summary> API 명세서 </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
[API 명세서.pdf](https://github.com/user-attachments/files/19478768/2.-.API.pdf)
</details>
<details>
<summary> 테스트 명세서 </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
[테스트 명세서.pdf](https://github.com/user-attachments/files/19478789/2.-.pdf)
</details>

<details>
<summary> WBS </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
내용~~~~
</details>
<details>
<summary> ERD </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
내용~~~~
</details>

---

# 5. 시스템 아키텍처 및 기술적 특징
![Image](https://github.com/user-attachments/assets/a7023a16-eaf4-4265-8938-91518eec0fef)

<details>
<summary>1. 인증 및 권한 관리</summary>

### JWT 기반 인증
* 액세스 토큰(15분) 및 리프레시 토큰(24시간) 사용
* Redis에 리프레시 토큰 저장 및 블랙리스트 관리
* 토큰에 사용자 식별자 및 역할 정보 포함

<details>
<summary>코드 예시</summary>

```java
// JWT 토큰 생성
public String createAccessToken(String username, String role) {
    Map<String, String> claims = new HashMap<>();
    claims.put("username", username);
    claims.put("role", role);
    return createToken(claims, ACCESS_TOKEN_EXP);
}

// 리프레시 토큰 생성 및 Redis 저장
public String createRefreshToken(String username) {
    Map<String, String> claims = Map.of("username", username);
    String refreshToken = createToken(claims, REFRESH_TOKEN_EXP);
    redisTemplate.opsForValue().set("refresh:" + username, refreshToken, REFRESH_TOKEN_EXP, TimeUnit.MILLISECONDS);
    return refreshToken;
}
```
</details>

### 권한 관리
* CustomPermissionEvaluator 구현으로 세밀한 권한 제어
* 리소스 소유자 검증(게시글, 댓글, 팀 등)
* @PreAuthorize 어노테이션으로 메서드 레벨 접근 제어

<details>
<summary>코드 예시</summary>

```java
// 권한 검증 어노테이션 사용 예시
@PostMapping("/{postNo}")
@PreAuthorize("hasPermission(#postNo, 'POST')")
public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postNo, @Valid @RequestBody PostDto postDto) {
    // 메서드 내용
}

// CustomPermissionEvaluator 구현 일부
@Override
public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    Long resourceId = (Long) targetDomainObject;
    switch (permission.toString().toUpperCase()) {
        case "POST": return isPostOwner(resourceId, authentication);
        case "COMMENT": return isCommentOwner(resourceId, authentication);
        case "TEAM": return isTeamOwner(resourceId, authentication);
        // 기타 케이스
    }
    return false;
}
```
</details>
</details>

<details>
<summary>2. 데이터 처리 및 성능 최적화</summary>

### 조회수 처리
* Redis를 활용한 중복 조회 방지(24시간)
* 사용자 식별을 위해 로그인 사용자는 ID 해시, 비로그인 사용자는 IP+User-Agent 조합 사용

<details>
<summary>코드 예시</summary>

```java
@Transactional
public void viewPost(CustomUserDetails userDetails, Long postNo, HttpServletRequest request) {
    // 비활성화된 게시글은 조회수 증가 제외
    if (post.getPostStatus() == PostStatus.INACTIVE) {
        return;
    }
    
    // Redis에 저장할 고유 키 생성
    String key = "post:view:" + postNo + ":" + getUserId(userDetails, request);
    Boolean isNotViewed = redisTemplate.opsForValue().setIfAbsent(key, "Viewed", Duration.ofHours(24));
    
    // 첫 조회인 경우에만 조회수 증가
    if (Boolean.TRUE.equals(isNotViewed)) {
        postRepository.increaseViewCount(postNo);
    }
}
```
</details>

### 알림 시스템
* Redis Pub/Sub 기반 실시간 알림
* SSE(Server-Sent Events)를 통한 클라이언트 푸시
* 15초 간격의 하트비트로 연결 유지

<details>
<summary>코드 예시</summary>

```java
// SSE 구독 설정
@GetMapping("/subscribe")
public SseEmitter subscribe(String username) {
    return notificationService.createEmitter(username);
}

// 하트비트 전송 로직
public void sendHeartbeat(String username, SseEmitter emitter, ScheduledExecutorService executor) {
    try {
        emitter.send(SseEmitter.event().name("heartbeat").data("heartbeat"));
    } catch (IOException e) {
        redisSubscriber.removeEmitter(username, emitter);
        executor.shutdown();
    }
}
```
</details>

### 데이터 접근
* Spring Data JPA 및 QueryDSL 사용
* N+1 문제 해결을 위한 최적화(fetch join 등)
* 페이징 처리로 대량 데이터 효율적 조회

<details>
<summary>코드 예시</summary>

```java
// QueryDSL을 활용한 검색 쿼리 예시
public Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, 
                                       String keyword, Pageable pageable, PostSortOption sortOption) {
    // QueryDSL 구현
    JPAQuery<Post> query = queryFactory.selectFrom(post)
            .where(post.boardType.eq(boardType), post.postStatus.eq(PostStatus.ACTIVE))
            .leftJoin(post.user).fetchJoin(); // N+1 문제 해결
    
    // 검색 조건 추가
    if (option != null && keyword != null) {
        switch (option) {
            case TITLE: query.where(post.postTitle.containsIgnoreCase(keyword)); break;
            case CONTENT: query.where(post.postContent.containsIgnoreCase(keyword)); break;
            case WRITER: query.where(post.user.username.containsIgnoreCase(keyword)); break;
        }
    }
    
    // 정렬 및 페이징 처리
    // ...
}
```
</details>
</details>

<details>
<summary>3. 보안 및 검증</summary>

### 비밀번호 보안
* BCrypt 암호화
* 5회 오류 시 계정 잠금(REQUIRES_NEW 트랜잭션 전파 사용)

<details>
<summary>코드 예시</summary>

```java
// 비밀번호 검증 및 오류 카운트 증가
public void validPwd(String password, User user) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
        authTransactionService.increasePasswordErrorCount(user);
        log.info("Password error count: {}", user.getPasswordErrorCount());
        throw new UserException(ExceptionMessage.USER_INPUT_MISMATCH);
    }
}

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void increasePasswordErrorCount(User user) {
    user.updatePasswordErrorCount(user.getPasswordErrorCount() + 1);
    userRepository.save(user);
}
```
</details>

### 입력 데이터 검증
* Bean Validation을 통한 DTO 검증
* 커스텀 예외 처리로 일관된 오류 응답

<details>
<summary>코드 예시</summary>

```java
// DTO 검증 예시
public class PostDto {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하여야 합니다.")
    private String title;
    
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;
    
    // 기타 필드 및 메서드
}

// 글로벌 예외 핸들러 (일부)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUserException(UserException e) {
        ApiErrorResponseDto response = new ApiErrorResponseDto(
            e.getExceptionMessage().getStatus(),
            e.getExceptionMessage().getMessage(),
            e.getDetail()
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getExceptionMessage().getStatus()));
    }
}
```
</details>
</details>

<details>
<summary>4. 기타 주요 기능</summary>

### 팀 및 프로젝트 관리
* 팀 생성, 가입 신청, 승인 프로세스 구현
* 프로젝트-팀 연결 구조로 협업 환경 제공
* 팀장 권한 관리 및 양도 기능

<details>
<summary>코드 예시</summary>

```java
// 팀원 가입 신청/수락 처리
public void teamAccept(Long teamNo, Long userNo) throws Exception {
    Long teamUserNo = teamUserRepository.findByUserNoForTeamUserNo(teamNo, userNo);
    
    TeamUser teamUser = teamUserRepository.findById(teamUserNo)
            .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    
    if (teamUser.getStatus() == TeamJoinStatus.Approved) {
        throw new IllegalArgumentException("이미 가입된 유저입니다!");
    }
    
    // 알림 전송 로직...
    teamUser.setStatus(TeamJoinStatus.Approved);
    teamUserRepository.save(teamUser);
}
```
</details>

### 스케줄링 및 배치 처리
* @EnableScheduling을 활용한 주기적 작업 수행
* 시스템 자원 최적화를 위한 배치 처리

<details>
<summary>코드 예시</summary>

```java
@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
public void cleanupExpiredData() {
    log.info("Starting daily cleanup job");
    // 만료된 데이터 정리 로직
}
```
</details>
</details>


<br><br><br><br><br><br>
<br><br><br><br><br><br>
<br><br><br><br><br><br>
