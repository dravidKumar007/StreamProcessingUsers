package com.bigData.user.controller;

import com.bigData.user.model.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Optional;
@Controller
public class UserController {
@Autowired
private  UserRepository userRepository;

   public  boolean setUser(User user) {
       Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
       if (existingUser.isPresent()) {
           return false;
       }
       userRepository.save(user);
       return true;
   }
   public ResponseEntity<RestapiTemp> checkUser(User user) {
       Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
       if(existingUser.isPresent()){
            if(existingUser.get().getPassword().equals(user.getPassword())){// Replace with a secure key management approach

                String token = Jwts.builder()
                        .setSubject(user.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiry
                        .signWith(SignatureAlgorithm.HS256, "PFRdRmt6DHRv3AbLf2rrXTpF6IVFFe0dX9kn2gK2Hks=")
                        .compact();
                return ResponseEntity.ok(new SuccessApiTemplate("successful login",token));
            }
            return ResponseEntity.status(401).body(new FailApiTemplate(401,"Wrong Password"));
       }
       return ResponseEntity.status(404).body(new FailApiTemplate(404,"user not found"));
   }

}
