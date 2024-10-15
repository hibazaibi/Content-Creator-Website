
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
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Offre>> getAllOffers() {
        List<Offre> offre= offreService.findAllOffres();
        return new ResponseEntity<>(offre, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Offre> getrByOffreId(@PathVariable("id") Long id){
        Offre offre= offreService.findOffreById(id);
        return new ResponseEntity<>(offre, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long id) throws Exception {
        offreService.deleteOffreById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}