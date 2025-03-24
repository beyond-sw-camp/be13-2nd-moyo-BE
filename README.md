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

- user ban

- user lock
    + 패스워드 5회 오류시 계정 lock
    + 스프링 트랜잭션 전파(`REQUIRES_NEW` 사용)


- `Security`로 애노테이션 기반 권한 관리
    + 로그인된 회원
    + 소유자 확인

</details>

### 2. 이메일 인증
<details>
<summary> [SMTP] </summary>

- 회원 가입

</details>

### 3. 알림
<details>
<summary> [REDIS + SSE] </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
내용~~~~
</details>

### 4. 조회수 및 좋아요
<details>
<summary> [REDIS] </summary>

<!-- summary 아래 한칸 공백 두어야함 -->
내용~~~~
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