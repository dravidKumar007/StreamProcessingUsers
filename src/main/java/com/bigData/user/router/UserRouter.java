package com.bigData.user.router;

import com.bigData.user.controller.UserController;
import com.bigData.user.model.FailApiTemplate;
import com.bigData.user.model.RestapiTemp;
import com.bigData.user.model.SuccessApiTemplate;
import com.bigData.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/users")
public class UserRouter {

    @Autowired
    private UserController userController;

    private static final String SECRET_KEY = "PFRdRmt6DHRv3AbLf2rrXTpF6IVFFe0dX9kn2gK2Hks="; // Replace with a secure key management approach

    @PostMapping
    public ResponseEntity<RestapiTemp> setUser(@RequestBody User user) {
        try {
            if (userController.setUser(user)) {
                // Create JWT token
                String token = Jwts.builder()
                        .setSubject(user.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiry
                        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                        .compact();

                RestapiTemp result = new SuccessApiTemplate("User signup successful", token);
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new FailApiTemplate(HttpStatus.CONFLICT.value(), "User already exists"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailApiTemplate(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/login")
    public ResponseEntity<RestapiTemp> login(@RequestParam String email, @RequestParam String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        System.out.println(email+"=>"+password);
        return userController.checkUser(user);
    }

}

