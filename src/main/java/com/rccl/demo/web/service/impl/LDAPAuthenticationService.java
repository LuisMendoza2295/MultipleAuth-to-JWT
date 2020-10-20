package com.rccl.demo.web.service.impl;

import com.rccl.demo.web.service.UsernamePasswordAuthenticationService;
import com.rccl.demo.web.security.model.RcclUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class LDAPAuthenticationService implements UsernamePasswordAuthenticationService {

    @Override
    public UserDetails authenticate(String username, String password) {
        return new RcclUserDetails(username, Arrays.asList(new SimpleGrantedAuthority("LDAP_ADMIN")));
    }
}
