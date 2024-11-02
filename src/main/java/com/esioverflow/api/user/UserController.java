package com.esioverflow.api.user;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.esioverflow.api.profile.Profile;

import jakarta.validation.Valid;



@RestController
public class UserController {
    private final UserService userService;

    
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // endpoints
    @GetMapping("/users")
    public List<UserResponseDto> retrieveAllUsers() {
        return userService.getAllUsers();
    }
    
    @PostMapping("/users")
    public User create(
        @Valid @RequestBody UserDto dto
    ) {
        return userService.saveUser(dto);
    }

    @GetMapping("/users/{user-id}")
    public UserResponseDto retrieve(
        @PathVariable("user-id") Integer id
    ) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/users/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
        @PathVariable("user-id") Integer id
    ){
        userService.deleteUser(id);
    }
    
    
    // profile
    // Delete after
    //@Deprecated
    @GetMapping("/users/{user-id}/profile")
    @ResponseStatus(HttpStatus.OK)
    public Profile getProfile(
        @PathVariable("user-id") Integer id
    ) {
        return userService.getProfile(id);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodeArgumentNotValidException(
        MethodArgumentNotValidException e
    ){
        var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            var fieldname = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldname, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }   
}
