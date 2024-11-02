package com.esioverflow.api.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDto userToUserDto(User user){
        return new UserDto(user.getNickname(), user.getEmail(), user.getPassword());
    }

    public UserResponseDto userToUserResponseDto(User user) {

        return new UserResponseDto(
            user.getId(), 
            user.getNickname(), 
            user.getEmail(), 
            user.getProfile()
        );
    }
}
