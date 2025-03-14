package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.user.dto.AllUserResponseDto;
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
    private final ProjectRepository projectRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public DeleteUserByAdminResponseDto deleteUserByAdmin(DeleteUserByAdminRequestDto dto) {

        User user = userRepository.findById(dto.getUserNo())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + dto.getUserNo()));
                
        userRepository.delete(user);
      //  userRepository.deleteById(user.getNo());

        return new DeleteUserByAdminResponseDto();
    }

    ///  모든 사용자 조회 -- ROLE 필터 ..
    @Override
    public Page<AllUserResponseDto> getUsers(Pageable pageable) {
        return userRepository.getUsers(pageable);
    }



    @Override
    public OneUserResponseDto getOneUser(Long userNo) {

        // controller 에서 검증
        // 여기서 userno 는 조회하고자 하는 회원

        User user = userRepository.findById(userNo).orElseThrow(
            () ->new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo)
        );


        return new OneUserResponseDto(
            user.getUsername(),
            user.getRole(),
            user.getEmail(),
            user.getPhoneNum(),
            user.getBanned(),
            user.getPasswordErrorCount(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUserStatus()
        );
    }






}
