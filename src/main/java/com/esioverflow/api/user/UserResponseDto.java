package com.esioverflow.api.user;

import com.esioverflow.api.profile.Profile;

public record UserResponseDto(
    Integer id,
    String nickname,
    String email,
    Profile profile
) {
    
}
