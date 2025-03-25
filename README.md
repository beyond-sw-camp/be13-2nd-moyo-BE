# be13-2nd-4team

# 팀원 소개
- 최건
- 임현조
- 홍도현
- 송현준
- 김민석
- 홍재민

---
# 기술 스택
### backend
- spring boot
- spring security
- jwt
- spring data jpa
- mariaDB
- redis

---
### frontend
- vue.js

### devops
- jenkins
- docker
---
# 서비스 소개
## 배경

## 목표

---
# 기획
<details>
<summary> 요구사항 명세서 </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
내용~~~~
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
# 시스템 아키텍처

---
# 주요기술
### 1. 로그인 및 어드민
<details>
<summary> [Spring Security + JWT] </summary>

- 로그인
  + gif

- User ban
  + gif
  + 회원 신고
  + 관리자 처리

- User lock
  + gif
  + 패스워드 5회 오류시 계정 lock
  + 스프링 트랜잭션 전파(`REQUIRES_NEW` 사용)
  ````java
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increasePasswordErrorCount(User user) {
        user.updatePasswordErrorCount(user.getPasswordErrorCount() + 1);
        userRepository.save(user);
    }
  ````


- `Security`로 애노테이션 기반 권한 관리
  ````java
    @GetMapping("/posts/{postNo}/with-comments")
    @PreAuthorize("hasPermission(#postNo, 'POST_ACCESS')")
    public ResponseEntity<UserPostResponseDto> getPostDetail(
            @PathVariable Long postNo,
            HttpServletRequest request) {
  
        //내용 생략
  
        postService.viewPost(postNo, request);
    }
  ````
  + 로그인된 회원(`@AuthenticationPrincipal`)
  + 소유자 확인 및 권한(`@PreAuthorize`)

</details>

### 2. 이메일 인증
<details>
<summary> [SMTP] </summary>

> Simple Mail Transfer Protocol, 이메일 전송에 사용되는 네트워크 프로토콜

- 회원 가입
  + gif

</details>

### 3. 알림
<details>
<summary> [REDIS + SSE] </summary>

> 단방향 통신으로 단순한 알림 기능에 최적화

- 플로우(그림)

- 팀 가입 신청(쪽지)
  + gif

- 신고 해결
  + gif


</details>

### 4. 조회수 및 좋아요
<details>
<summary> [REDIS] </summary>

````java
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.no = :postNo")
    void increaseViewCount(@Param("postNo") Long postNo);
````

````java
private String getUserId(HttpServletRequest request) {
    String userIdentifier = "";
    CustomUserDetails userDetails = authService.getCurrentUser();

    // 로그인 된 사용자인 경우(회원)
    if (userDetails != null) {
        User user = userDetails.getUser();
        if (user != null) {
            //사용자 번호를 해시값으로 변환하여 식별자 생성
            userIdentifier = "user:" + user.getNo().hashCode();
            log.info("{} 님이 조회함", user.getUsername());
        }
    } else { //비로그인 사용자인 경우(게스트)
        //IP 주소 가져오기
        String ipAddress = request.getRemoteAddr();


        if (ipAddress != null && !ipAddress.isEmpty()) {
            // X-Forwarded-For 헤더가 있는 경우, 첫 번째 IP(클라이언트 실제 IP) 사용
            ipAddress = ipAddress.split(",")[0].trim();
        } else {
            // 헤더가 없는 경우 직접 연결된 IP 사용
            ipAddress = request.getRemoteAddr();
        }

        //User-Agent 정보 가져오기
        String userAgent = request.getHeader("User-Agent");

        //User-Agent 정보가 없는 경우 IP만으로 식별자 생성
        if (userAgent == null || userAgent.isEmpty()) {
            userIdentifier = "guest:" + ipAddress.hashCode();
        } else {//IP와 User-Agent를 조합하여 더 고유한 식별자 생성
            String identifier = ipAddress + userAgent;
            userIdentifier = "guest:" + (long) identifier.hashCode();
            }
            log.info("ip : {} User-Agent : {}인 게스트가 조회함", ipAddress, userAgent);

        }

        return userIdentifier;
    }
````

</details>

### 4. 일정관리
<details>
<summary> [Scheduler] </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
내용~~~~
</details>

### 5. 배포
<details>
<summary> [DOCKER + JENKINS] </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
내용~~~~
</details>

<br><br><br><br><br><br>
<br><br><br><br><br><br>
<br><br><br><br><br><br>