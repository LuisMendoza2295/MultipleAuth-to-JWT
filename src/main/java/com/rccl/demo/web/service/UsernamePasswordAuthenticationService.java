package com.rccl.demo.web.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UsernamePasswordAuthenticationService {

    UserDetails authenticate(String username, String password);
}
