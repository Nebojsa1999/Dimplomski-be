package com.isa.domain.api;

import com.isa.config.Principal;
import com.isa.domain.dto.ChangePasswordDTO;
import com.isa.domain.dto.UserDTO;
import com.isa.domain.model.User;
import com.isa.exception.NotFoundException;
import com.isa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@PreAuthorize("isAuthenticated()")
public class UserApi {

    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
        final User user = userService.register(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        final User user = userService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path = "/change-password")
    public ResponseEntity<User> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, @AuthenticationPrincipal Principal principal) {
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(userService.changePassword(changePasswordDTO, user), HttpStatus.OK);
    }

    @PutMapping(path = "/update-profile")
    public ResponseEntity<User> updateProfile(@RequestBody UserDTO userDTO, @AuthenticationPrincipal Principal principal) {
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(userService.updateProfile(userDTO, user), HttpStatus.OK);
    }
}

