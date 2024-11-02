package com.esioverflow.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
    @NotBlank(message = "The nickname must not be blank")
    String nickname,

    @Email(message = "This is not a correct email format")
    String email,

    
    String password
) {
    
}
