package com.mazen.seucrity.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
//@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody DtoReg dtoReg) {
        return ResponseEntity.ok(service.register(dtoReg));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody DtoLogin dtoLogin) {
        return ResponseEntity.ok(service.authenticate(dtoLogin));
    }
}

//public class AuthenticationController {
//
//    private final AuthenticationService service;
//
//    @GetMapping("/login")
//    public String getLoginPage(Model model) {
//        model.addAttribute("dtoLogin", new DtoLogin());
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String getRegisterPage(Model model) {
//        model.addAttribute("dtoReg", new DtoReg());
//        return "register";
//    }
//
//    @PostMapping("/api/v1/auth/register")
//    public String register(@ModelAttribute DtoReg dtoReg, Model model, HttpServletResponse response) {
//        AuthenticationResponse authenticationResponse = service.register(dtoReg);
//        String token = authenticationResponse.getToken();
//        response.setHeader("Authorization", "Bearer " + token);
//        model.addAttribute("token", token);
//        return "redirect:/success"; // Redirect to the success page
//    }
//
//    @PostMapping("/authenticate")
//    public String authenticate(@ModelAttribute DtoLogin dtoLogin, Model model, HttpServletResponse response) {
//        AuthenticationResponse authenticationResponse = service.authenticate(dtoLogin);
//        String token = authenticationResponse.getToken();
//
//        // Assuming your authentication response returns a JwtAuthenticationToken
//        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
//
//        // Let Spring Security handle the token encoding
//        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
//
//        // Continue with your logic...
//
//        return "redirect:/success";
//    }
//    @GetMapping("/success")
//        public String getSuccessPage(Model model) {
//        var x=   model.getAttribute("token");
//        System.out.println("This is to token"+ x);
//        return "success";
//    }
//}


