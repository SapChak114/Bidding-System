package com.bidding.users.controller;

import com.bidding.users.dto.request.PaginationRequest;
import com.bidding.users.dto.request.UserRequestDto;
import com.bidding.users.dto.request.UserUpdateRequestDto;
import com.bidding.users.dto.response.APIResponse;
import com.bidding.users.service.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/")
    public ResponseEntity<APIResponse<Object>> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        APIResponse<Object> resp = usersService.createUser(userRequestDto);
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateRequestDto userRequestDto) {
        APIResponse<Object> resp = usersService.updateUser(id, userRequestDto);
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<APIResponse<Object>> getByEmail(@PathVariable("email") String email) {
        APIResponse<Object> resp = usersService.getByEmail(email);
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }

    @GetMapping("/{offSet}/{pageSize}/{field}/{sort}")
    public ResponseEntity<APIResponse<Object>> getUsersByFilters(
                        @PathVariable(value = "offSet", required = false) int offSet,
                        @PathVariable(value = "pageSize", required = false) int pageSize,
                        @PathVariable(value = "field", required = false) String field,
                        @PathVariable(value = "sort", required = false) String sort,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "contact", required = false) String contact) {
        APIResponse<Object> resp = usersService.getUsersByFilters(PaginationRequest.builder()
                .offset(offSet)
                .pageSize(pageSize)
                .field(field)
                .sort(sort)
                .name(name)
                .email(email)
                .contact(contact)
                .build());
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }

}
