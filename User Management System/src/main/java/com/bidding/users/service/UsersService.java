package com.bidding.users.service;

import com.bidding.users.dto.request.PaginationRequest;
import com.bidding.users.dto.request.UserRequestDto;
import com.bidding.users.dto.response.APIResponse;

public interface UsersService {
    APIResponse<Object> createUser(UserRequestDto userRequestDto);

    APIResponse<Object> updateUser(Long id, UserRequestDto userRequestDto);

    APIResponse<Object> getUserById(Long id);

    APIResponse<Object> getUsersByFilters(PaginationRequest build);

    APIResponse<Object> deleteUser(Long id);

    APIResponse<Object> getByEmail(String email);
}
