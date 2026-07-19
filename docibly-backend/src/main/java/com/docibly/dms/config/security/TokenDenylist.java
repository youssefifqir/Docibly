package com.docibly.dms.config.security;

public interface TokenDenylist {

    void add(String jti, long ttlSeconds);

    boolean isDenied(String jti);
}
