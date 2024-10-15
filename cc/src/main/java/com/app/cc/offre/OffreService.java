package com.app.cc.offre;

import com.app.cc.Client.Client;

import com.app.cc.exeption.UserNotFoundException;
import com.app.cc.user.Role;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class OffreService {
    @Autowired
    private OffreRepository offreRepository;

    private final UserRepository userRepository;


            public OffreResponse addOffer(OffreRequest request) throws Exception {
                Optional<User> userOpt = userRepository.findById(request.getUseridoffre());
                if (userOpt.isEmpty()) {
                    throw new Exception("User not found");
                }

                User user = userOpt.get();

                if (user.getRole().equals(Role.CLIENT)) {
                    Optional<User> creatorOpt = userRepository.findByEmail(request.getCreatorEmail());
                    if (creatorOpt.isEmpty()) {
                        throw new Exception("Creator not found with the provided email");
                    }
                    User creator = creatorOpt.get();
                    Offre offer = Offre.builder()
                            .description(request.getDescription())
                            .budget(request.getBudget())
                            .status(OffreStatus.EN_ATTENTE)  // Set initial status to PENDING
                            .dateSoumission(LocalDateTime.now())
                            .build();

                    offreRepository.save(offer);
                    return OffreResponse.builder()
                            .description(offer.getDescription())
                            .budget(request.getBudget())
                            .status(OffreStatus.EN_ATTENTE)  // Set initial status to PENDING
                            .dateSoumission(LocalDateTime.now())
                            .build();
                } else {
                    throw new Exception("User is not a client");
                }
        }
    public List<Offre> findAllOffres() {
        return offreRepository.findAll();
    }
    public Offre findOffreById(Long id){
        return  offreRepository.findById(id).orElseThrow(()-> new OffreNotFoundException("offre by id"+id+"notfound"));

    }
    public void deleteOffreById(Long id) throws Exception {
        Offre offre= offreRepository.findById(id)
                .orElseThrow(() -> new OffreNotFoundException("Offre not found with ID: " + id));
        offreRepository.deleteById(id);

    }
}
