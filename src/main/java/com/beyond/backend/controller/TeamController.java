package com.beyond.backend.controller;

import com.beyond.backend.domain.team.dto.TeamDetailDto;
import com.beyond.backend.domain.team.dto.TeamDto;
import com.beyond.backend.domain.team.dto.TeamLeaderDto;
import com.beyond.backend.domain.team.dto.TeamMemberListDto;
import com.beyond.backend.domain.team.dto.TeamResponseDto;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.team.entity.TeamJoinStatus;
import com.beyond.backend.domain.team.service.TeamService;
import com.beyond.backend.domain.teamUser.entity.TeamUser;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p> 팀 컨트롤러
 *
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : TeamController
 * <p>author         : hongjm
 * <p>date           : 2025-02-03
 * <p>description    : 팀 컨트롤러
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        hongjm           최초 생성
 * 2025-02-18        hongjm           팀 조회 기능 추가
 * 2025-02-20        hongjm           팀 CRUD 수정
 * 2025-02-22        hongjm           팀원 추가/제거 등등 기능 추가
 * 2025-02-23        hongjm           기능 추가 및 코드 정리
 * 2025-02-24        hongjm           코드 정리
 * 2025-02-26        hongjm           URL 할당 및 중복 코드 통합
 * 2025-03-21        hongjm           팀 리더 확인, 상세보기
 */
@Tag(name = "05 팀 API", description = "팀 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    /**
     * 팀 생성 메서드
     * @param teamDto 팀 정보
     * @return teamResponseDto
     */
    @PostMapping
    @Operation(summary = "팀 생성 메서드", description = "팀 생성 메서드입니다.")
    public ResponseEntity<TeamResponseDto> createTeam(
            @Valid @RequestBody TeamDto teamDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        TeamResponseDto teamResponseDto = teamService.createTeam(userDetails.getNo(), teamDto);
        return ResponseEntity.status(HttpStatus.OK).body(teamResponseDto);
    }

    /**
     * 팀 수정 메소드
     * @param teamNo 팀 번호
     * @param teamDto 팀 정보
     * @return teamResponseDto
     */
    @PutMapping("/{teamNo}")
    @Operation(summary = "팀 수정 메서드", description = "팀 수정 메서드 입니다.")
    public ResponseEntity<TeamResponseDto> updateTeam(
            @PathVariable Long teamNo,
            @Valid @RequestBody TeamDto teamDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        TeamResponseDto teamResponseDto = new TeamResponseDto(teamNo, teamDto);

        teamService.updateTeam(userDetails.getNo(), teamResponseDto);

        return ResponseEntity.status(HttpStatus.OK).body(teamResponseDto);
    }

    /**
     * 모든 팀 조회 메서드
     * @param userNo        (옵션) 유저 번호
     * @param teamName      (옵션) 팀이름
     * @param teamIntroduce (옵션) 팀 설명
     * @param projectStatus (옵션) 팀 상태
     * @param page          최소 출력 값
     * @param size          최대 출력 값
     * @return teamSearch 검색 결과값 반환
     */
    @GetMapping
    @Operation(summary = "해당 유저의 모든 팀 조회 메서드", description = "유저 번호로 해당 유저의 모든 팀을 조회하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo" , description = "유저 번호"),
            @Parameter(name = "teamName" , description = "팀 이름"),
            @Parameter(name = "teamIntroduce" , description = "팀 설명"),
            @Parameter(name = "projectStatus" , description = "프로젝트 상태"),
            @Parameter(name = "page" , description = "페이지 번호", example = "0"),
            @Parameter(name = "size" , description = "한 페이지 결과 수", example = "10")
    })
    public ResponseEntity<PageImpl<TeamResponseDto>> getUserTeams(
            @RequestParam(required = false) Long userNo,
            @RequestParam(required = false) String teamName,
            @RequestParam(required = false) String teamIntroduce,
            @RequestParam(required = false) ProjectStatus projectStatus,
            @RequestParam int page,
            @RequestParam int size){

        PageImpl<TeamResponseDto> teamSearch = teamService.filterUserTeams(userNo, teamName, teamIntroduce, projectStatus, page, size);
        return ResponseEntity.status(HttpStatus.OK). body(teamSearch);
    }

    /**
     * 팀 삭제 메서드
     * @param teamNo 팀no
     * @return 없음
     * @throws Exception 팀이 존재하지 않습니다.
     */
    @DeleteMapping("/{teamNo}")
    @Operation(summary = "팀 삭제 메서드", description = "팀 삭제 메서드입니다.")
    @Parameters({ @Parameter(name = "teamNo" , description = "삭제할 팀 번호") })
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamNo,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        teamService.deleteTeam(userDetails.getNo(), teamNo);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }

    /**
     * 팀 상세 정보
     * @param teamNo 팀번호
     * @return teamDetailDto
     * @throws Exception
     */
    @GetMapping("/{teamNo}")
    @Operation(summary = "팀 상세 정보", description = "팀 상세 메서드입니다.")
    public ResponseEntity<TeamDetailDto> getTeamDetails(
            @PathVariable Long teamNo) throws Exception {
        TeamDetailDto teamDetailDto = teamService.getTeamDetailDto(teamNo);
        return ResponseEntity.status(HttpStatus.OK).body(teamDetailDto);
    }

    /**
     * [팀장] 팀원 목록 조회
     * @param teamNo 팀번호
     * @return TeamMemberListDto
     * @throws Exception 권한이 없습니다!
     */
    @GetMapping("/{teamNo}/setting/members")
    @Operation(summary = "[팀장] 팀원 신청 목록 조회", description = "팀원 신청 목록 조회 메서드입니다.")
    @Parameters({@Parameter(name = "teamNo" , description = "팀 번호", example = "1"),})
    public ResponseEntity<List<TeamMemberListDto>> getMemberRequest(
            @PathVariable Long teamNo,
            @RequestParam TeamJoinStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        List<TeamMemberListDto> teamMemberRequest = teamService.getTeamMemberRequest(userDetails.getNo(), teamNo, status);
        return ResponseEntity.status(HttpStatus.OK).body(teamMemberRequest);
    }

    /**
     * 팀장 확인
     * @param teamNo 팀 번호
     * @return Boolean
     */
    @GetMapping("/leader-role")
    @Operation(summary = "팀장 확인 메서드", description = "팀장 확인 메서드입니다.")
    public ResponseEntity<TeamLeaderDto> isTeamLeader(
            @RequestParam(required = false) Long teamNo,
            @RequestParam(required = false) Long projectNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        TeamLeaderDto TeamLeaderDto = teamService.isTeamLeader(userDetails.getNo(), teamNo,projectNo);
        return ResponseEntity.status(HttpStatus.OK).body(TeamLeaderDto);
    }

    /**
     * [팀장] 팀원 가입 수락 메서드
     * @param teamNo 팀번호
     * @param userNo 신청한 유저의 유저번호
     * @return 정상적으로 수락되었습니다.
     * @throws Exception 신청한 유저가 없습니다!
     * @throws Exception 이미 가입된 유저입니다!
     */
    @PutMapping("/{teamNo}/setting/members/accept/{userNo}")
    @Operation(summary = "[팀장] 팀원 가입 수락 메서드", description = "팀원 가입 수락 메서드입니다.")
    @Parameters({
            @Parameter(name = "teamNo" , description = "팀 번호", example = "1"),
            @Parameter(name = "userNo" , description = "유저 번호")
    })
    public ResponseEntity<String> teamAccept (@PathVariable Long teamNo,
                                              @PathVariable Long userNo) throws Exception {
        teamService.teamAccept(teamNo, userNo);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 수락 되었습니다.");
    }

    /**
     * [팀장] 팀원 가입 거부 메서드
     * @param teamNo 팀번호
     * @param userNo 신청한 유저의 유저번호
     * @return 정상적으로 처리 되었습니다.
     * @throws Exception 신청한 유저가 없습니다!
     */
    @PutMapping("/{teamNo}/setting/members/delete/{userNo}")
    @Operation(summary = "[팀장] 팀원 가입 거부 메서드", description = "팀원 가입 거부 메서드입니다.")
    @Parameters({
            @Parameter(name = "teamNo" , description = "팀 번호", example = "1"),
            @Parameter(name = "userNo" , description = "유저 번호")
    })
    public ResponseEntity<String> teamDelete(@PathVariable Long teamNo,
                                             @PathVariable Long userNo) throws Exception {
        teamService.teamDelete(teamNo, userNo);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 처리 되었습니다.");
    }

    /**
     * 팀장 권한 양도
     * @param teamNo 팀 번호
     * @param nextUserNo 양도받을 유저 번호
     * @return 정상적으로 처리되었습니다.
     * @throws Exception
     */
    @PutMapping("/{teamNo}/setting/members")
    @Operation(summary = "[팀장] 팀장 권한 양도", description = "팀장 권한을 양도하는 메서드입니다.")
    public ResponseEntity<String> teamLeaderSwap(@PathVariable Long teamNo,
                                                 @RequestParam Long nextUserNo,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        teamService.teamLeaderSwap(userDetails.getNo(), teamNo, nextUserNo);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 처리 되었습니다.");
    }

    /**
     * 팀원 목록 조회 메서드
     * @param teamNo 팀번호
     * @return TeamMemberListDto
     * @throws Exception 팀이 존재하지 않습니다.
     */
    @GetMapping("/{teamNo}/setting/members/find")
    @Operation(summary = "팀원 목록 조회 메서드", description = "팀원 목록 조회 메서드입니다.")
    @Parameters({@Parameter(name = "teamNo" , description = "팀 번호", example = "1")})
    public ResponseEntity<List<TeamMemberListDto>> getUserTeams( @PathVariable Long teamNo ) throws Exception {
        List<TeamMemberListDto> teamMembers = teamService.getTeamMembers(teamNo);
        return ResponseEntity.status(HttpStatus.OK).body(teamMembers);
    }

    /**
     * 팀원 가입 신청 메서드
     * @param teamNo 팀번호
     * @return 정상적으로 신청되었습니다.
     * @throws Exception 팀이 존재하지 않습니다.
     * @throws Exception 유저가 존재하지 않습니다.
     * @throws Exception 모집중이 아닙니다!
     * @throws Exception 이미 등록되었거나 요청한 팀 입니다!!
     */
    @PostMapping("/{teamNo}/join-request")
    @Operation(summary = "팀원 가입 신청 메서드", description = "팀원 가입 신청 메서드입니다.")
    @Parameters({@Parameter(name = "teamNo" , description = "팀 번호", example = "1"),})
    public ResponseEntity<String> teamJoinRequest(@PathVariable Long teamNo,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        teamService.teamJoinRequest(userDetails.getNo(), teamNo);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 신청되었습니다.");
    }

    /**
     * 팀원 가입 취소 메서드
     * @param teamNo 팀번호
     * @return 정상적으로 취소되었습니다.
     * @throws Exception 신청하지 않았거나 존재하지 않는 팀입니다.
     * @throws Exception 이미 처리된 요청입니다.
     */
    @DeleteMapping("/{teamNo}/join-cancel")
    @Operation(summary = "팀원 가입 취소 메서드", description = "팀원 가입 취소  메서드입니다.")
    @Parameters({@Parameter(name = "teamNo", description = "팀 번호", example = "1"),})
    public ResponseEntity<String> teamJoinRequestCancel(@PathVariable Long teamNo,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        teamService.teamJoinRequestCancel(userDetails.getNo(), teamNo);

        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 취소되었습니다.");
    }


    /**
     * 팀 번호로 팀 리더 username 조회
     *
     * @param teamNo 팀 번호
     * @return username
     */
    @GetMapping("/{teamNo}/leader-username")
    @Operation(summary = "팀 리더 username 조회", description = "팀 번호로 팀 리더의 username을 반환합니다.")
    @Parameter(name = "teamNo", description = "팀 번호")
    public ResponseEntity<String> getLeaderUsername(@PathVariable Long teamNo) {
        String username = teamService.findLeaderUsernameByTeamNo(teamNo);
        return ResponseEntity.status(HttpStatus.OK).body(username);
    }

    /**
     * 팀원 역할 변경
     * @param teamNo 팀번호
     * @return TeamMemberListDto
     * @throws Exception 팀이 존재하지 않습니다.
     */
    @PutMapping("/{teamNo}/setting/members/role")
    public ResponseEntity<TeamMemberListDto> changeTeamRole(
            @PathVariable Long teamNo,
            @RequestParam(required = false) Long userNo,
            @RequestParam String newRole,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        TeamMemberListDto teamMembers = teamService.changeTeamRole(teamNo, userNo, userDetails.getNo(),newRole);
        return ResponseEntity.status(HttpStatus.OK).body(teamMembers);
    }
}