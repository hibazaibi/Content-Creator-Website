package com.app.cc.evaluation;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class  EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final OffreRepository offreRepository;
    public EvaluationResponse addEvaluation(Long OfferId,EvaluationRequest request) throws Exception {
        Offre offer = offreRepository.findById(OfferId).orElseThrow(() -> new Exception("OFFER not found"));;
        if (offer.getStatus() != OffreStatus.TERMINEE) {
            throw new Exception("You can only evaluate after the offer is completed");
        }
        if (offer.useridoffre.getRole().equals(Role.CLIENT)) {

        Evaluation evaluation = Evaluation.builder()
                .offre(offer)
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .dateEvaluated(LocalDateTime.now())
                .build();


        evaluationRepository.save(evaluation);

            User creator = offer.getIdcreateur();
            if (creator == null) {
                throw new Exception("Creator not found for the offer");
            }
            offer.setIsev(true);
            offreRepository.save(offer);
            creator.setTotalRatings(creator.getTotalRatings() + request.getRating());
            creator.setNumberOfRatings(creator.getNumberOfRatings() + 1);
            userRepository.save(creator);
            return new EvaluationResponse(evaluation.getIdevaluation(), evaluation.getRating(), evaluation.getFeedback());
        } else { throw new Exception("Only Client can evaluate the offre"); }
    }
    public List<String> getFeedbackForOffer(Long idOffre) {
        List<Evaluation> evaluations = evaluationRepository.findByOffreIdOffre(idOffre);
        List<String> feedbacks = new ArrayList<>();

        // Collect the feedback from evaluations
        for (Evaluation evaluation : evaluations) {
            feedbacks.add(evaluation.getFeedback());
        }

        return feedbacks;
    }
}


