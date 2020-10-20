package com.rccl.demo.web.service.impl;

import com.rccl.demo.web.service.CredentialIDAuthenticationService;
import com.rccl.demo.web.security.model.RcclUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class FidelioAuthenticationService implements CredentialIDAuthenticationService {

    @Override
    public UserDetails authenticate(String credentialID) {
        return new RcclUserDetails(credentialID, Arrays.asList(new SimpleGrantedAuthority("FIDELIO_ADMIN")));
    }
}
