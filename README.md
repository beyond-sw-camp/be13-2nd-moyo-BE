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

> 사용 이유

- 팀 가입 신청(쪽지)
  + gif

- 신고 해결
  + gif
</details>

### 4. 조회수 및 좋아요
<details>
<summary> [REDIS] </summary>

> 발전과정?

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