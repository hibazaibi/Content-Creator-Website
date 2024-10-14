package com.app.cc.offre;

import com.app.cc.Client.Client;

import com.app.cc.user.Role;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class OffreService {
    @Autowired
    private OffreRepository offreRepository;

    private final UserRepository userRepository;


            public OffreResponse addOffer(OffreRequest request) throws Exception {
                // Fetch the user based on the provided ID
                Optional<User> userOpt = userRepository.findById(request.getUseridoffre());
                if (userOpt.isEmpty()) {
                    throw new Exception("User not found");
                }

                User user = userOpt.get();

                // Check if the user is a client using the role
                if (user.getRole().equals(Role.CLIENT)) {
                    // Now, find the creator based on the email provided in the request
                    Optional<User> creatorOpt = userRepository.findByEmail(request.getCreatorEmail());
                    if (creatorOpt.isEmpty()) {
                        throw new Exception("Creator not found with the provided email");
                    }
                    User creator = creatorOpt.get(); // Get the creator

                    // Create a new offer
                    Offre offer = Offre.builder()
                            .description(request.getDescription())
                            .budget(request.getBudget())
                            .status(OffreStatus.EN_ATTENTE)  // Set initial status to PENDING
                            .dateSoumission(LocalDateTime.now())
                            .build();

                    // Save the offer in the database
                    offreRepository.save(offer);

                    // Return a response containing the offer details
                    return OffreResponse.builder()
                            .description(offer.getDescription())
                            .budget(request.getBudget())
                            .status(OffreStatus.EN_ATTENTE)  // Set initial status to PENDING
                            .dateSoumission(LocalDateTime.now())
                            .build();
                } else {
                    throw new Exception("User is not a client");
                }
        }}
