package com.app.cc.dispute;


import com.app.cc.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
    @RequestMapping("/api/disputes")
    @RequiredArgsConstructor

public class DisputeController {
    @Autowired
    private final DisputeService disputeService;
    private final AuthenticationService userService;
    @PostMapping("/create")
    public ResponseEntity<Dispute> createDispute(@RequestBody DisputeRequest request) {
        try {
            Long offerId = request.getOfferId();
            Long userId = request.getUserId();
            String reason = request.getRaison();
            String details = request.getDetailsresolution();

            Dispute dispute = disputeService.createDispute(offerId, userId, reason, details);
            return new ResponseEntity<>(dispute, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/all")
    public ResponseEntity<List<Dispute>> getAllDisputes() {
        try {
            List<Dispute> disputes = disputeService.getAllDisputes();
            return new ResponseEntity<>(disputes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resolve/{disputeId}")
    public ResponseEntity<Dispute> resolveDispute(
            @PathVariable Long disputeId,
            @RequestParam Long adminId,
            @RequestBody DisputeResolutionRequest resolutionRequest) {
        try {
            Dispute resolvedDispute = disputeService.resolveDispute(
                    disputeId,
                    adminId,
                    resolutionRequest.getResolution(),
                    resolutionRequest.getAdminNotes()
            );
            return new ResponseEntity<>(resolvedDispute, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("find/{id}")
    public ResponseEntity<Dispute> getDisputeById(@PathVariable Long id) {
        try {
            Dispute dispute = disputeService.getDisputeById(id);
            return new ResponseEntity<>(dispute, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}



