
package com.app.cc.offre;

import com.app.cc.evaluation.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/offers/")
@RequiredArgsConstructor
public class OffreController {
    private final OffreService offreService;
    private final EvaluationService evaluationService;
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

    @GetMapping("/all2")
    public ResponseEntity<List<Offre>> getAllOffers() {
        List<Offre> offre = offreService.findAllOffres();
        return new ResponseEntity<>(offre, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<OffreInfo>> getAllOffers2() {
        List<OffreInfo> offre = offreService.findAllOffresSorted();
        return new ResponseEntity<>(offre, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<OffreInfo> getOfferById(@PathVariable("id") Long id) {
        try {
            OffreInfo offre = offreService.findOffreById2(id);
            return new ResponseEntity<>(offre, HttpStatus.OK);
        } catch (OffreNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find2/{id}")
    public ResponseEntity<List<OffreInfo> >getOfferByuserid(@PathVariable("id") Long id) {
        try {
            System.out.println(id);
            List<OffreInfo> offre = offreService.findOffresByUserId(id);
            System.out.println(offre);
            return new ResponseEntity<>(offre, HttpStatus.OK);
        } catch (OffreNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Offre> updateOffer(@PathVariable Long id, @RequestBody OffreRequest updatedRequest) {
        try {
            Offre updatedOffer = offreService.updateOffer(id, updatedRequest);
            return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find3/{id}")
    public ResponseEntity<List<OffreInfo> >getOfferBycreatorid(@PathVariable("id") Long id) {
        try {
            System.out.println(id);
            List<OffreInfo> offre = offreService.findOffresBycreatorid(id);
            System.out.println(offre);
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


    @PutMapping("/accept/{id}")
    public ResponseEntity<String> acceptOffer(@PathVariable("id") Long id) {
        try {
            Offre acceptedOffer = offreService.acceptOffer(id);
            return new ResponseEntity<>("Offer accepted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/decline/{id}")
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
    @GetMapping("/count")
    public long countAllOffers() {
        return offreService.countAllOffers();
    }

    @GetMapping("/countByStatus")
    public ResponseEntity<Map<String, Long>> countOffersByStatus() {
        Map<String, Long> counts = offreService.getOfferStatusCounts();
        return new ResponseEntity<>(counts, HttpStatus.OK);    }

    @GetMapping("/averageBudget")
    public Double getAverageBudget() {
        return offreService.getAverageBudget();
    }
    @GetMapping("/{clientId}/offers")
    public ResponseEntity<List<Offre>> getOffersForClient(@PathVariable Long clientId) {
        List<Offre> offers = offreService.getOffersForClient(clientId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
    @GetMapping("/offersGroupedByMonth")
    public ResponseEntity<Map<String, Long>> getOffersGroupedByMonth() {
        Map<String, Long> offerCounts = offreService.getOffersGroupedByMonth();
        return new ResponseEntity<>(offerCounts, HttpStatus.OK);
    }
    @GetMapping("/{clientId}/totalBudget")
    public ResponseEntity<Double> getTotalBudgetForClient(@PathVariable Long clientId) {
        Double totalBudget = offreService.getTotalBudgetForClient(clientId);
        return new ResponseEntity<>(totalBudget, HttpStatus.OK);
    }

    // Endpoint to get offer status counts for a client
    @GetMapping("/{clientId}/offerStatusCounts")
    public ResponseEntity<Map<String, Long>> getOfferStatusCountsForClient(@PathVariable Long clientId) {
        Map<String, Long> counts = offreService.getOfferStatusCountsForClient(clientId);
        return new ResponseEntity<>(counts, HttpStatus.OK);
    }
    @GetMapping("/{creatorId}/total-offers")
    public ResponseEntity<Long> getTotalOffersForCreator(@PathVariable Long creatorId) {
        long totalOffers = offreService.getTotalOffersForCreator(creatorId);
        return ResponseEntity.ok(totalOffers);
    }

    @GetMapping("/{creatorId}/offer-status-counts")
    public ResponseEntity<Map<String, Long>> getOfferStatusCountsForCreator(@PathVariable Long creatorId) {
        Map<String, Long> statusCounts = offreService.getOfferStatusCountsForCreator(creatorId);
        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping("/{creatorId}/average-budget")
    public ResponseEntity<Double> getAverageBudgetForCreator(@PathVariable Long creatorId) {
        Double averageBudget = offreService.getAverageBudgetForCreator(creatorId);
        return ResponseEntity.ok(averageBudget);
    }

    @GetMapping("/{creatorId}/total-earnings")
    public ResponseEntity<Double> getTotalEarningsForCreator(@PathVariable Long creatorId) {
        Double totalEarnings = offreService.getTotalEarningsForCreator(creatorId);
        return ResponseEntity.ok(totalEarnings);
    }

    @GetMapping("/{creatorId}/average-rating")
    public ResponseEntity<Double> getCreatorAverageRating(@PathVariable Long creatorId) {
        double averageRating = offreService.getCreatorAverageRating(creatorId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/{creatorId}/feedbacks")
    public ResponseEntity<List<String>> getFeedbacksForCreator(@PathVariable Long creatorId) {
        List<String> feedbacks = evaluationService.getFeedbackForCreator(creatorId);
        return ResponseEntity.ok(feedbacks);
    }


    @GetMapping("/{creatorId}/improvement-suggestions")
    public ResponseEntity<Map<String, String>> getImprovementSuggestionsForCreator(@PathVariable Long creatorId) {
        String suggestions = offreService.getImprovementSuggestionsForCreator(creatorId);

        Map<String, String> response = new HashMap<>();
        response.put("suggestion", suggestions); // Wrap the string in a map

        return ResponseEntity.ok(response);
    }

}
