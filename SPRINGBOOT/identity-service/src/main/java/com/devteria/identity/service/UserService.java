package com.devteria.identity.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.devteria.identity.dto.request.CreatePatientAccount;
import com.devteria.identity.dto.request.CreatePatientDTO;
import com.devteria.identity.feignclient.PatientServiceClient;
import com.devteria.identity.mapper.PatientMapper;
import feign.FeignException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devteria.identity.constant.Roles;
import com.devteria.identity.dto.request.UserCreationRequest;
import com.devteria.identity.dto.request.UserUpdateRequest;
import com.devteria.identity.dto.response.UserResponse;
import com.devteria.identity.entity.User;
import com.devteria.identity.exception.AppException;
import com.devteria.identity.exception.ErrorCode;
import com.devteria.identity.mapper.UserMapper;
import com.devteria.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private final UserMapper userMapper;
    private final PatientMapper patientMapper;
    private PasswordEncoder passwordEncoder;
    private PatientServiceClient patientServiceClient;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        user.setRole(Roles.USER);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        log.info("User created: {}", user);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse createPatient(CreatePatientAccount request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        UserCreationRequest userCreationRequest = patientMapper.toUserCreationRequest(request);
        User user = userMapper.toUser(userCreationRequest);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        user.setRole(Roles.USER);

        try {
            user = userRepository.save(user);

            CreatePatientDTO createPatientDTO = patientMapper.toCreatePatientDTO(request);
            createPatientDTO.setId(UUID.fromString(user.getId()));

            try {
                patientServiceClient.createPatient(createPatientDTO);
            } catch (FeignException ex) {
                log.error("Failed to create patient for user {}: {}", user.getId(), ex.getMessage());
                // Log chi tiết về mã lỗi và chuyển hướng nếu có
                if (ex.status() >= 300 && ex.status() < 400) {
                    log.error("Redirect detected. Check if URL or server configuration has changed.");
                }
                throw new AppException(ErrorCode.FAILED_TO_CREATE_PATIENT);
            }

        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.USER_EXISTED);
        } catch (Exception ex) {
            log.error("Unexpected error: {}", ex.getMessage());
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        log.info("User created: {}", user);
        return userMapper.toUserResponse(user);
    }



    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setUpdatedAt(LocalDate.now());



        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
