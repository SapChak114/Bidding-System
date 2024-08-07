package com.bidding.users.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.bidding.users.dao.User;
import com.bidding.users.dto.request.PaginationRequest;
import com.bidding.users.dto.request.UserRequestDto;
import com.bidding.users.dto.request.UserUpdateRequestDto;
import com.bidding.users.dto.response.APIResponse;
import com.bidding.users.dto.response.PaginationResponse;
import com.bidding.users.dto.response.UserResponseDto;
import com.bidding.users.repository.UsersRepository;
import com.bidding.users.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.bidding.users.constants.UserManagementConstants.*;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final ObjectMapper mapper;

    public UserServiceImpl(UsersRepository usersRepository, ObjectMapper mapper) {
        this.usersRepository = usersRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public APIResponse<Object> createUser(UserRequestDto userRequestDto) {
        try {
            log.info("Starting user creation process for email: {}", userRequestDto.getEmail());
            User user = mapper.convertValue(userRequestDto, User.class);
            if (StringUtil.notNullNorEmpty(userRequestDto.getPassword())) {
                user.setPassword(BCrypt.hashpw(userRequestDto.getPassword(), BCrypt.gensalt()));
            } else {
                return createResponse(EMPTY_PASSWORD, HttpStatus.BAD_REQUEST);
            }
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            user = usersRepository.save(user);
            log.info("User created successfully with ID: {}", user.getId());
            return createResponse(mapper.convertValue(user, UserResponseDto.class), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_CREATING_USER, e);
        }
    }

    @Override
    @Transactional
    public APIResponse<Object> updateUser(Long id, UserUpdateRequestDto userRequestDto) {
        try {
            log.info("Starting user update process for ID: {}", id);
            User user = usersRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));

            mapper.updateValue(user, userRequestDto);

            user = usersRepository.save(user);

            log.info("User updated successfully with ID: {}", user.getId());
            return createResponse(mapper.convertValue(user, UserResponseDto.class), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating user with ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_UPDATING_USER, e);
        }
    }

    @Override
    public APIResponse<Object> getByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        try {
            User user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));

            UserResponseDto userResponseDto = mapper.convertValue(user, UserResponseDto.class);
            return createResponse(userResponseDto, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            log.warn("User not found with email: {}", email, e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching user by email: {}", email, e);
            return createResponse(ERROR_ACCESSING_DATABASE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by email: {}", email, e);
            return createResponse(ERROR_FETCHING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public APIResponse<Object> getUsersByFilters(PaginationRequest paginationRequest) {
        try {
            log.info("Fetching users with filters: {}", paginationRequest);

            Pageable pageable = PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize(),
                    paginationRequest.getSort().toLowerCase().startsWith("a")
                            ? Sort.by(paginationRequest.getField()).ascending()
                            : Sort.by(paginationRequest.getField()).descending());

            Page<User> page = usersRepository.findByOptions(pageable,
                    paginationRequest.getName(),
                    paginationRequest.getEmail(),
                    paginationRequest.getContact());

            List<UserResponseDto> response = mapper.convertValue(page.getContent(),
                    mapper.getTypeFactory().constructCollectionType(List.class, UserResponseDto.class));

            PaginationResponse<UserResponseDto> paginationResponse = PaginationResponse.<UserResponseDto>builder()
                    .size(page.getSize())
                    .totalNoPages(page.getTotalPages())
                    .currentPage(page.getNumber() + 1)
                    .totalElements(page.getTotalElements())
                    .sortBy(paginationRequest.getField())
                    .sortByType(paginationRequest.getSort())
                    .content(response)
                    .build();
            return createResponse(paginationResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error fetching users with filters: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_FETCHING_USER, e);
        }
    }

    private APIResponse<Object> createResponse(Object content, HttpStatus status) {
        if (status.is4xxClientError() || status.is5xxServerError()) {
            if (content instanceof String) {
                throw new ResponseStatusException(status, (String) content);
            } else {
                throw new ResponseStatusException(status, ERROR);
            }
        }
        return APIResponse.builder()
                .statusCode(status.value())
                .response(content)
                .build();
    }
}
