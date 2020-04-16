package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.entity.Product;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
import de.janetschel.haushaltsplan.backend.exception.LoginExpiredException;
import de.janetschel.haushaltsplan.backend.exception.UserNotLoggedInException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BringService {

    @Value("${bring.api.uuid}")
    private String uuid;

    @Value("${bring.api.sharedlist.uuid}")
    private String listUuid;

    @Value("${bring.api.key}")
    private String apikey;

    @Value("${bring.api.instanceId}")
    private String instanceId;

    @Value("${authentication.user}")
    private String authtoken;

    public ResponseEntity<String> addProductToList(Product prodcut, String authToken)
            throws LoginExpiredException, UserNotLoggedInException, InvalidAuthenticationTokenException {

        LoginService.checkIfLoginIsExpired();

        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        final String bringApiBackendUri = "https://api.getbring.com/rest/bringlists/" + listUuid;
        String productName = prodcut.getProductName();
        String parameters = String.format("uuid=%s&purchase=%s", listUuid, prodcut.getProductName());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(parameters, getCustomHeaders());

        ResponseEntity<String> response = restTemplate.exchange(bringApiBackendUri, HttpMethod.PUT, httpEntity, String.class);

        if (HttpStatus.NO_CONTENT == HttpStatus.valueOf(response.getStatusCodeValue())) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Product '"+ productName +"' successfully added to list");
        }

        return response;
    }

    private HttpHeaders getCustomHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("X-BRING-API-KEY", apikey);
        httpHeaders.add("X-BRING-CLIENT-INSTANCE-ID", instanceId);
        httpHeaders.add("X-BRING-USER-UUID", uuid);
        httpHeaders.add("X-BRING-CLIENT", "webApp");
        httpHeaders.add("X-BRING-CLIENT-SOURCE", "webApp");
        httpHeaders.add("X-BRING-COUNTRY", "de");
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        httpHeaders.add("charset", "UTF-8");

        return httpHeaders;
    }
}
