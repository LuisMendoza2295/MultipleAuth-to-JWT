package com.rccl.demo.web.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CredentialIDAuthenticationService {

    UserDetails authenticate(String credentialID);
}
