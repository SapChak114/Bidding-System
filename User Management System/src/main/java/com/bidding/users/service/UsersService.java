package com.bidding.users.service;

import com.bidding.users.dto.request.PaginationRequest;
import com.bidding.users.dto.request.UserRequestDto;
import com.bidding.users.dto.request.UserUpdateRequestDto;
import com.bidding.users.dto.response.APIResponse;

public interface UsersService {
    APIResponse<Object> createUser(UserRequestDto userRequestDto);

    APIResponse<Object> updateUser(Long id, UserUpdateRequestDto userRequestDto);

    APIResponse<Object> getUsersByFilters(PaginationRequest build);

    APIResponse<Object> getByEmail(String email);
}
