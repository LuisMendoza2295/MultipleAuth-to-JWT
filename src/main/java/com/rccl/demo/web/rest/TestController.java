package com.rccl.demo.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @PreAuthorize("hasAnyAuthority('FIDELIO_ADMIN', 'LDAP_ADMIN')")
    @GetMapping("/protected")
    public String testProtected() {
        return "Hello from protected";
    }
}
