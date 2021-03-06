package de.janetschel.haushaltsplan.backend.controller;

import de.janetschel.haushaltsplan.backend.exception.InvalidBase64StringException;
import de.janetschel.haushaltsplan.backend.service.AuthTokenService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthTokenController.class)
public class AuthTokenControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthTokenService authTokenService;

    @Test
    @DisplayName("/getAuthtoken -> 200 OK")
    public void givenValidHeader_whenGetAuthToken_thenValidAuthtoken_andStatus200() throws Exception {
        String authorization = "samplestring";
        Mockito.when(authTokenService.getAuthToken(authorization)).thenReturn(ResponseEntity.ok("authtoken"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/getAuthToken").header("Auth-String", authorization))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("authtoken")));

        Mockito.verify(authTokenService, Mockito.times(1)).getAuthToken(authorization);
    }

    @Test
    @DisplayName("/getAuthtoken invalid credentials -> 403 FORBIDDEN")
    public void givenInvalidHeader_whenGetAuthToken_thenStatus403() throws Exception {
        String authorization = "wrongsamplestring";
        Mockito.when(authTokenService.getAuthToken(authorization)).thenThrow(InvalidBase64StringException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/getAuthToken").header("Auth-String", authorization))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(authTokenService, Mockito.times(1)).getAuthToken(authorization);
    }
}
