package de.janetschel.haushaltsplan.backend.controller;

import de.janetschel.haushaltsplan.backend.entity.Product;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
import de.janetschel.haushaltsplan.backend.exception.LoginExpiredException;
import de.janetschel.haushaltsplan.backend.exception.UserNotLoggedInException;
import de.janetschel.haushaltsplan.backend.service.BringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BringController {

    private final BringService bringService;

    public BringController(BringService bringService) {
        this.bringService = bringService;
    }

    @PutMapping("/bring/api/addProduct")
    public ResponseEntity<String> addProductToSharedBringList(@RequestBody Product product, @RequestHeader("Auth-Token") String authToken)
            throws UserNotLoggedInException, LoginExpiredException, InvalidAuthenticationTokenException {
        return bringService.addProductToList(product, authToken);
    }
}
