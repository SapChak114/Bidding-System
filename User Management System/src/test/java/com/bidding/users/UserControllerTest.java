package com.bidding.users;

import com.bidding.users.controller.UserController;
import com.bidding.users.dto.request.PaginationRequest;
import com.bidding.users.dto.request.UserRequestDto;
import com.bidding.users.dto.response.APIResponse;
import com.bidding.users.dto.response.UserResponseDto;
import com.bidding.users.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserRequestDto requestDto = new UserRequestDto();
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .response(UserResponseDto.builder().build())
                .build();

        when(usersService.createUser(any(UserRequestDto.class))).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = userController.createUser(requestDto);

        assertEquals(HttpStatus.CREATED.value(), result.getStatusCodeValue());
        assertEquals(UserResponseDto.builder().build().toString(), result.getBody().getResponse().toString());
    }

    @Test
    void testUpdateUser() {
        UserRequestDto requestDto = new UserRequestDto();
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .response("User updated successfully")
                .build();

        when(usersService.updateUser(anyLong(), any(UserRequestDto.class))).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = userController.updateUser(1L, requestDto);

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals("User updated successfully", result.getBody().getResponse());
    }

    @Test
    void testGetUserById() {
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .response("User details")
                .build();

        when(usersService.getUserById(anyLong())).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = userController.getUserById(1L);

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals("User details", result.getBody().getResponse());
    }

    @Test
    void testGetByEmail() {
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .response("User by email")
                .build();

        when(usersService.getByEmail(anyString())).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = userController.getByEmail("test@example.com");

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals("User by email", result.getBody().getResponse());
    }

    @Test
    void testGetUsersByFilters() {
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .response("Filtered users")
                .build();

        PaginationRequest paginationRequest = PaginationRequest.builder()
                .offset(0)
                .pageSize(10)
                .field("name")
                .sort("asc")
                .name("John")
                .email("john@example.com")
                .contact("1234567890")
                .build();

        when(usersService.getUsersByFilters(any(PaginationRequest.class))).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = userController.getUsersByFilters(
                0, 10, "name", "asc", "John", "john@example.com", "1234567890");

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals("Filtered users", result.getBody().getResponse());
    }

    @Test
    void testDeleteUsers() {
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .response("User deleted successfully")
                .build();

        when(usersService.deleteUser(anyLong())).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = userController.deleteUsers(1L);

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getStatusCodeValue());
        assertEquals("User deleted successfully", result.getBody().getResponse());
    }
}
