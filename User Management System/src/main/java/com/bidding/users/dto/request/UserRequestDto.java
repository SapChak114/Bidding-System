package com.bidding.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Contact cannot be blank")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Contact number must be between 10 and 15 digits and may start with a '+'")
    private String contact;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}