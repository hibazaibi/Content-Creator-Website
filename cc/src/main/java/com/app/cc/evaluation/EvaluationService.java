package com.app.cc.evaluation;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.app.cc.offre.Offre;
import com.app.cc.offre.OffreRepository;
import com.app.cc.offre.OffreStatus;
import com.app.cc.user.Role;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final OffreRepository offreRepository;
    public EvaluationResponse addEvaluation(EvaluationRequest request) throws Exception {
        Optional<Offre> offerOpt = offreRepository.findById(request.getOfferId());
        if (offerOpt.isEmpty()) {
            throw new Exception("Offre not found");
        }

        Offre offer = offerOpt.get();
        if (offer.getStatus() != OffreStatus.TERMINEE) {
            throw new Exception("You can only evaluate after the offer is completed");
        }

        Optional<User> clientOpt = userRepository.findById(request.getClientId());
        if (clientOpt.isEmpty() || !clientOpt.get().getRole().equals(Role.CLIENT)) {
            throw new Exception("Client not found or invalid");
        }
        Client client = (Client) clientOpt.get();

        Optional<User> creatorOpt = userRepository.findById(request.getCreatorId());
        if (creatorOpt.isEmpty() || !creatorOpt.get().getRole().equals(Role.CREATOR)) {
            throw new Exception("Creator not found or invalid");
        }
        Createur createur = (Createur) creatorOpt.get();

        Evaluation evaluation = Evaluation.builder()
                .client(client)
                .createur(createur)
                .offre(offer)
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .dateEvaluated(LocalDateTime.now())
                .build();

        evaluationRepository.save(evaluation);

        return new EvaluationResponse(evaluation.getIdevaluation(), evaluation.getRating(), evaluation.getFeedback());
    }

    public List<Evaluation> getEvaluationsByCreator(Long creatorId) throws Exception {
        Optional<User> creatorOpt = userRepository.findById(creatorId);
        if (creatorOpt.isEmpty() || !(creatorOpt.get() instanceof Createur)) {
            throw new Exception("Creator not found or is not a valid creator");
        }

        Createur creator = (Createur) creatorOpt.get();

        return evaluationRepository.findByCreateur(creator);
    }
}


