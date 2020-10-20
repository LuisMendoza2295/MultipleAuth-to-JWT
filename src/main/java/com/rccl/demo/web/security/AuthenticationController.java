package com.rccl.demo.web.security;

import com.rccl.demo.web.security.util.JwtUtil;
import com.rccl.demo.web.service.CredentialIDAuthenticationService;
import com.rccl.demo.web.service.UsernamePasswordAuthenticationService;
import com.rccl.demo.web.service.type.ProviderType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final CredentialIDAuthenticationService fidelioAuthenticationService;
    private final UsernamePasswordAuthenticationService ldapAuthenticationService;

    private final JwtUtil jwtUtil;

    public AuthenticationController(CredentialIDAuthenticationService fidelioAuthenticationService, UsernamePasswordAuthenticationService ldapAuthenticationService, JwtUtil jwtUtil) {
        this.fidelioAuthenticationService = fidelioAuthenticationService;
        this.ldapAuthenticationService = ldapAuthenticationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/fidelio")
    public String authenticateUserWithFidelio(@RequestParam("fidelioID") String fidelioID) {
        UserDetails userDetails = this.fidelioAuthenticationService.authenticate(fidelioID);

        Authentication authentication = new AbstractAuthenticationToken(userDetails.getAuthorities()) {
            @Override
            public Object getCredentials() {
                return userDetails.getUsername();
            }

            @Override
            public Object getPrincipal() {
                return userDetails;
            }
        };

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.jwtUtil.generateJwtToken(authentication, ProviderType.FIDELIO);

        return token;
    }

    @GetMapping("/ldap")
    public String authenticateUserWithLDAP(@RequestParam("username") String username, @RequestParam("password") String password) {
        UserDetails userDetails = this.ldapAuthenticationService.authenticate(username, password);

        Authentication authentication = new AbstractAuthenticationToken(userDetails.getAuthorities()) {
            @Override
            public Object getCredentials() {
                return userDetails.getUsername();
            }

            @Override
            public Object getPrincipal() {
                return userDetails;
            }
        };

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.jwtUtil.generateJwtToken(authentication, ProviderType.LDAP);

        return token;
    }
}
