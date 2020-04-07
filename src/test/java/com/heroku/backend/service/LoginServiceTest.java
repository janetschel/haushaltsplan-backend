package com.heroku.backend.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class LoginServiceTest {
    @Mock
    public LoginService loginService;

    @Test
    public void givenValidCredentials_whenLoginUser_thenTrue() {
        String base64 = "valid_credentials";
        Mockito.when(loginService.loginUser(base64)).thenReturn(ResponseEntity.ok(true));

        int statusCodeValue = loginService.loginUser(base64).getStatusCodeValue();
        Assertions.assertThat(statusCodeValue).isEqualTo(200);

        Mockito.verify(loginService, Mockito.times(1)).loginUser(base64);
    }

    @Test
    public void givenInvalidCredentials_whenLoginUser_thenFalse() {
        String base64 = "invalid_credentials";
        Mockito.when(loginService.loginUser(base64)).thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).body(false));

        int statusCodeValue = loginService.loginUser(base64).getStatusCodeValue();
        Assertions.assertThat(statusCodeValue).isEqualTo(403);

        Mockito.verify(loginService, Mockito.times(1)).loginUser(base64);
    }
}
