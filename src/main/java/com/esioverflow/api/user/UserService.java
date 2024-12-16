package com.esioverflow.api.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.esioverflow.api.profile.Profile;
import com.esioverflow.api.profile.ProfileRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileRepository profileRepository;

    //constructors dependencies injections
    public UserService(UserRepository userRepository, UserMapper userMapper, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.profileRepository = profileRepository;
    }

    public User saveUser(User user){
        user.setProfile(
            profileRepository.save(new Profile())
        );
        
        return userRepository.save(user);
    }
    
    public User saveUser(UserDto dto){
        User user = new User(
            dto.nickname(),
            dto.email(),
            dto.password()
        );

        //first create a new empty profile that'll be persisted and then set it as profile of our new user
        user.setProfile(
            profileRepository.save(new Profile()) 
        );

        return userRepository.save(user);
    }

    public UserResponseDto getUserById(Integer id) {
        return userRepository.findById(id)
                            .map(userMapper::userToUserResponseDto)
                            .orElse(null);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                            .stream()
                            .map(userMapper::userToUserResponseDto)
                            .collect(Collectors.toList());
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Profile getProfile(Integer id) {
        return userRepository.findById(id)
                             .orElse(null)
                             .getProfile();
    }

    
}
