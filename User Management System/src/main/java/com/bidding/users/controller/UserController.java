package com.bidding.users.controller;

import com.bidding.users.dto.request.PaginationRequest;
import com.bidding.users.dto.request.UserRequestDto;
import com.bidding.users.dto.response.APIResponse;
import com.bidding.users.service.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
    public ResponseEntity<APIResponse<Object>> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRequestDto userRequestDto) {
        APIResponse<Object> resp = usersService.updateUser(id, userRequestDto);
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> getUserById(@PathVariable("id") Long id) {
        APIResponse<Object> resp = usersService.getUserById(id);
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<APIResponse<Object>> getByEmail(@PathVariable("email") String email) {
        APIResponse<Object> resp = usersService.getByEmail(email);
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }

    @GetMapping("/{offSet}/{pageSize}/{field}/{sort}")
    public ResponseEntity<APIResponse<Object>> getUsersByFilters(
                        @PathVariable("offSet") int offSet,
                        @PathVariable("pageSize") int pageSize,
                        @PathVariable("field") String field,
                        @PathVariable("sort") String sort,
                        @RequestParam("name") String name,
                        @RequestParam("email") String email,
                        @RequestParam("contact") String contact) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> deleteUsers(@PathVariable("id") Long id) {
        APIResponse<Object> resp = usersService.deleteUser(id);
        return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getStatusCode()));
    }
}
