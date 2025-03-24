package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.common.exception.PostException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.team.entity.Team;
import com.beyond.backend.domain.team.repository.TeamRepository;
import com.beyond.backend.domain.user.entity.UserSortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.user.dto.AllUserResponseDto;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminRequestDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;
import com.beyond.backend.domain.user.dto.OneUserResponseDto;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public DeleteUserByAdminResponseDto delete(Long userNo) {

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo));
                
        userRepository.delete(user);
        return new DeleteUserByAdminResponseDto();
    }

    ///  모든 사용자 조회 -- ROLE 필터 ..
    @Override
    public Page<AllUserResponseDto> getUsers(UserSortOption userSortOption, Pageable pageable) {
        return userRepository.getUsers(userSortOption, pageable);
    }

    @Override
    public OneUserResponseDto getOneUser(Long userNo) {

        // controller 에서 검증
        // 여기서 userno 는 조회하고자 하는 회원

        User user = userRepository.findById(userNo).orElseThrow(
            () ->new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo)
        );

        return new OneUserResponseDto(user);
    }

    @Override
    public Page<UserPostResponseDto> getUserAllPost(BoardType boardType, Long userNo, Pageable pageable) {

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo));

        Page<UserPostResponseDto> userPosts = postRepository.getUserPosts(user.getNo(), boardType, pageable);

        return userPosts;
    }



}

