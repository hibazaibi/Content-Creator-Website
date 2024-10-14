
package com.app.cc.offre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/offers/")
@RequiredArgsConstructor
public class OffreController {
    private final OffreService offreService;

    @PostMapping("/create")
    public ResponseEntity<OffreResponse> createOffer(@RequestBody OffreRequest request) {
        try {
            OffreResponse response = offreService.addOffer(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e) {
            e.printStackTrace(); // or use a logger
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}