package com.docibly.dms.service.facade.security;

import com.docibly.dms.ws.dto.auth.AuthenticationRequest;
import com.docibly.dms.ws.dto.auth.RefreshRequest;
import com.docibly.dms.ws.dto.auth.RegistrationRequest;
import com.docibly.dms.ws.dto.auth.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest req);

    void logout(String authorizationHeader);
}
