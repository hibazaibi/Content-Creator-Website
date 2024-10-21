
package com.app.cc.offre;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Offre>> getAllOffers() {
        List<Offre> offre = offreService.findAllOffres();
        return new ResponseEntity<>(offre, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Offre> getOfferById(@PathVariable("id") Long id) {
        try {
            Offre offre = offreService.findOffreById(id);
            return new ResponseEntity<>(offre, HttpStatus.OK);
        } catch (OffreNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) throws Exception {
        try {
            offreService.deleteOffreById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OffreNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/accept/{id}")
    public ResponseEntity<String> acceptOffer(@PathVariable("id") Long id) {
        try {
            Offre acceptedOffer = offreService.acceptOffer(id);
            return new ResponseEntity<>("Offer accepted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/decline/{id}")
    public ResponseEntity<String> declineOffer(@PathVariable("id") Long id) {
        try {
            Offre declinedOffer = offreService.declineOffer(id);
            return new ResponseEntity<>("Offer declined successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/check-expired")
    public ResponseEntity<String> checkExpiredOffers() {
        offreService.checkExpiredOffers();
        return new ResponseEntity<>("Expired offers checked and updated", HttpStatus.OK);
    }
    @PutMapping("/complete/{id}")
    public ResponseEntity<Offre> completeOffer(@PathVariable Long id) {
        try {
            Offre completedOffer = offreService.completeOffer(id);
            return new ResponseEntity<>(completedOffer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}