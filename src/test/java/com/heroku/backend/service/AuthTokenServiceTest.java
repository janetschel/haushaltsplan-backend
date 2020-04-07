package com.heroku.backend.service;

import com.heroku.backend.exception.InvalidBase64StringException;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AuthTokenServiceTest {
    @Mock
    public AuthTokenService authTokenService;

    @SneakyThrows(value = InvalidBase64StringException.class)
    @Test
    public void givenValidCredentials_whenGetAuthToken_thenAuthToken_andNoException() {
        String base64 = "valid_credentials";
        Mockito.when(authTokenService.getAuthToken(base64)).thenReturn(ResponseEntity.ok("authtoken"));

        ResponseEntity<String> response = authTokenService.getAuthToken(base64);
        String body = response.getBody();
        int statusCodeValue = response.getStatusCodeValue();

        Assertions.assertThat(body).isEqualTo("authtoken");
        Assertions.assertThat(statusCodeValue).isEqualTo(200);

        Mockito.verify(authTokenService, Mockito.times(1)).getAuthToken(base64);
    }

    @SneakyThrows(value = InvalidBase64StringException.class)
    @Test(expected = InvalidBase64StringException.class)
    public void givenInvalidCredentials_whenGetAuthToken_thenInvalidBase64StringException() {
        String base64 = "valid_credentials";
        Mockito.when(authTokenService.getAuthToken(base64)).thenThrow(InvalidBase64StringException.class);

        authTokenService.getAuthToken(base64);
    }
}
