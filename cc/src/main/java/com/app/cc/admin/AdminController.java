package com.app.cc.admin;

import com.app.cc.auth.AuthenticationService;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService userService;

    @PutMapping("/activate/{userId}")
    public ResponseEntity<String> activateUser(@PathVariable Long userId) {
        try {
            userService.activateUser(userId);
            return new ResponseEntity<>("User activated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pending-accounts")
    public ResponseEntity<List<User>> listPendingUsers() {
        List<User> pendingUsers = userRepository.findByActiveFalse();
        return new ResponseEntity<>(pendingUsers, HttpStatus.OK);
    }
}

