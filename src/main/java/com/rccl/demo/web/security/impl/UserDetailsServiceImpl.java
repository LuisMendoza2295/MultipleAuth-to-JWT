package com.rccl.demo.web.security.impl;

import com.rccl.demo.web.security.model.RcclUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // TODO: we could have a local DB to manage the logged users (avoiding doing external calls) and retrieve more info (not included in jwt)
        return new RcclUserDetails(s, new ArrayList<>());
    }
}
