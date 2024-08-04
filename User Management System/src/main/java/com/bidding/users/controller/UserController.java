package com.bidding.users.controller;

import com.bidding.users.dto.request.PaginationRequest;
import com.bidding.users.dto.request.UserRequestDto;
import com.bidding.users.dto.response.APIResponse;
import com.bidding.users.service.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    public APIResponse<Object> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return usersService.createUser(userRequestDto);
    }

    @PutMapping("/{id}")
    public APIResponse<Object> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRequestDto userRequestDto) {
        return usersService.updateUser(id, userRequestDto);
    }

    @GetMapping("/{id}")
    private APIResponse<Object> getUserById(@PathVariable("id") Long id) {
        return usersService.getUserById(id);
    }

    @GetMapping("/{email}")
    private APIResponse<Object> getByEmail(@PathVariable("email") String email) {
        return usersService.getByEmail(email);
    }

    @GetMapping("/{offSet}/{pageSize}/{field}/{sort}")
    private APIResponse<Object> getUsersByFilters(
                        @PathVariable("offSet") int offSet,
                        @PathVariable("pageSize") int pageSize,
                        @PathVariable("field") String field,
                        @PathVariable("sort") String sort,
                        @RequestParam("name") String name,
                        @RequestParam("email") String email,
                        @RequestParam("contact") String contact) {
        return usersService.getUsersByFilters(PaginationRequest.builder()
                .offset(offSet)
                .pageSize(pageSize)
                .field(field)
                .sort(sort)
                .name(name)
                .email(email)
                .contact(contact)
                .build());
    }

    @DeleteMapping("/{id}")
    private APIResponse<Object> deleteUsers(@PathVariable("id") Long id) {
        return usersService.deleteUser(id);
    }
}
