package com.bidding.users.controller;

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

    }

    @PutMapping("/{id}")
    public APIResponse<Object> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRequestDto userRequestDto) {

    }

    @GetMapping("/{id}")
    private APIResponse<Object> getUserById(@PathVariable("id") Long id) {

    }

    @GetMapping("/{offSet}/{pageSize}/{field}/{sort}")
    private APIResponse<Object> getUsersByFilters() {

    }

    @DeleteMapping("/{id}")
    private APIResponse<Object> deleteUsers() {

    }
}
