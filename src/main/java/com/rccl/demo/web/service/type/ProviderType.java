package com.rccl.demo.web.service.type;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public enum ProviderType {

    FIDELIO,
    LDAP;

    //TODO: i don't recommend to do this, each ProviderType must be handled by a different endpoint
    /* public static ProviderType fromRequest(HttpServletRequest request) {
        if (request.getHeader("fidelioId") != null) {
            return FIDELIO;
        }
        return LDAP;
    } */

    public Map<String, Object> getJwtClaims() {
        Map<String, Object> claims = new HashMap<>();

        claims.put("auth_type", this.name());

        return claims;
    }
}
