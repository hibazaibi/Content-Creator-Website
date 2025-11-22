package com.app.cc.offre;


import com.app.cc.Client.Client;
import com.app.cc.Client.ClientRepository;
import com.app.cc.Createur.Createur;
import com.app.cc.Createur.CreateurRepository;
import com.app.cc.email.EmailSender;
import com.app.cc.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor

public class OffreService {
    @Autowired
    private OffreRepository offreRepository;
    private final ClientRepository clientRepository;
    private final CreateurRepository createurRepository;



    public OffreResponse addOffer(OffreRequest request) throws Exception {
        var userOpt = clientRepository.findById(request.getUseridoffre());
        var userCreateur = createurRepository.findById(request.getIdcreateur());

        if (userOpt.isPresent() && userCreateur.isPresent()) {
            LocalDateTime expirationDate = LocalDateTime.now().plusDays(15);

            Offre offer = Offre.builder()
                    .description(request.getDescription())
                    .budget(request.getBudget())
                    .status(OffreStatus.EN_ATTENTE)
                    .dateSoumission(LocalDateTime.now())
                    .expirationDate(expirationDate)
                    .deadline(request.getDeadline())
                    .collaborationDetails(request.getCollaborationDetails())
                    .specialRequests(request.getSpecialRequests())
                    .useridoffre(userOpt.get())
                    .idcreateur(userCreateur.get())
                    .build();

            offreRepository.save(offer);

            return OffreResponse.builder()
                    .description(offer.getDescription())
                    .budget(offer.getBudget())
                    .status(offer.getStatus())
                    .dateSoumission(offer.getDateSoumission())
                    .expirationDate(offer.getExpirationDate())
                    .deadline(offer.getDeadline())
                    .collaborationDetails(offer.getCollaborationDetails())
                    .specialRequests(offer.getSpecialRequests())
                    .useridoffre(offer.getUseridoffre().getEmail())
                    .idcreateur(offer.getIdcreateur().getEmail())
                    .build();
        } else {
            throw new Exception("Client or Creator not found");
        }
    }

    public List<Offre> findAllOffres() {
        return offreRepository.findAll();
    }
    public List<OffreInfo> findAllOffresSorted() {
        List<Offre> offres = offreRepository.findAllSortedByDateSoumission();

        if (offres.isEmpty()) {
            throw new OffreNotFoundException("No offers found");
        }

        return offres.stream()
                .map(this::mapToOffreInfo)
                .toList();
    }

    public List<OffreInfo> findAllOffres2() {

        List<Offre> offres = offreRepository.findAll();

        if (offres.isEmpty()) {
            throw new OffreNotFoundException("No offers found ");
        }

        return offres.stream()
                .map(this::mapToOffreInfo)
                .toList();

    }

    public OffreInfo findOffreById2(Long id) {
        Offre off = offreRepository.findById(id).orElseThrow(() -> new OffreNotFoundException("Offer with ID " + id + " not found"));


        return mapToOffreInfo(off) ;

    }
    public Offre findOffreById(Long id) {
        return offreRepository.findById(id)
                .orElseThrow(() -> new OffreNotFoundException("Offer with ID " + id + " not found"));

    }
    public List<OffreInfo> findOffresByUserId(Long userId) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + userId + " not found"));
        List<Offre> offres = offreRepository.findByUseridoffre(client, Sort.by(Sort.Direction.DESC, "dateSoumission"));
        if (offres.isEmpty()) {
            throw new OffreNotFoundException("No offers found for user with ID " + userId);
        }

        return offres.stream()
                .map(this::mapToOffreInfo)
                .toList();
    }
    public List<OffreInfo> findOffresBycreatorid(Long userId) {
        Createur createur = createurRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + userId + " not found"));
        List<Offre> offres = offreRepository.findByIdcreateur(createur, Sort.by(Sort.Direction.DESC, "dateSoumission"));
        if (offres.isEmpty()) {
            throw new OffreNotFoundException("No offers found for user with ID " + userId);
        }

        return offres.stream()
                .map(this::mapToOffreInfo)
                .toList();
    }

    private OffreInfo mapToOffreInfo(Offre offre) {
        return OffreInfo.builder()
                .idOffre(offre.getIdOffre())
                .description(offre.getDescription())
                .budget(offre.getBudget())
                .status(offre.getStatus().toString())
                .dateSoumission(offre.getDateSoumission())
                .deadline(offre.getDeadline())
                .collaborationDetails(offre.getCollaborationDetails())
                .specialRequests(offre.getSpecialRequests())
                .useridoffre(offre.getUseridoffre().getId())
                .idcreateur(offre.getIdcreateur().getId())
                .expirationDate(offre.getExpirationDate())
                .nameclient(offre.getUseridoffre().getNom()+" "+offre.getUseridoffre().getPrenom() )
                .namecreateur(offre.getIdcreateur().getNom()+" "+offre.getIdcreateur().getPrenom())
                .isev(offre.isIsev())
                .build();
    }

    public void deleteOffreById(Long id) throws Exception {
        Offre offer = offreRepository.findById(id)
                .orElseThrow(() -> new OffreNotFoundException("Offer with ID: " + id + " not found"));
        offreRepository.deleteById(id);
    }
    public Offre updateOffer(Long offerId, OffreRequest updatedRequest) throws Exception {
        Offre offer = findOffreById(offerId);

        if (offer.getStatus() != OffreStatus.EN_ATTENTE) {
            throw new Exception("Offer cannot be updated as it is no longer in pending status.");
        }

        if (updatedRequest.getDescription() != null) {
            offer.setDescription(updatedRequest.getDescription());
            System.out.println("Updated description to: " + updatedRequest.getDescription());
        }

        if (updatedRequest.getBudget() != null) {
            offer.setBudget(updatedRequest.getBudget());
            System.out.println("Updated budget to: " + updatedRequest.getBudget());
        }

        if (updatedRequest.getDeadline() != null) {
            offer.setDeadline(updatedRequest.getDeadline());
            System.out.println("Updated deadline to: " + updatedRequest.getDeadline());
        }

        if (updatedRequest.getCollaborationDetails() != null) {
            offer.setCollaborationDetails(updatedRequest.getCollaborationDetails());
            System.out.println("Updated collaborationDetails to: " + updatedRequest.getCollaborationDetails());
        }

        if (updatedRequest.getSpecialRequests() != null) {
            offer.setSpecialRequests(updatedRequest.getSpecialRequests());
            System.out.println("Updated specialRequests to: " + updatedRequest.getSpecialRequests());
        }

        // Check for any other fields specific to the Offre model that might need updating
        // (similar to how you handled image updates in the user method, if applicable)

        Offre savedOffer = offreRepository.save(offer);
        System.out.println("Saved offer: " + savedOffer);
        return savedOffer;
    }


    public void checkExpiredOffers() {
        List<Offre> offers = offreRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Offre offer : offers) {
            if (offer.getStatus() == OffreStatus.EN_ATTENTE && offer.getExpirationDate().isBefore(now)) {
                offer.setStatus(OffreStatus.EXPIRE);
                offreRepository.save(offer);
            }
        }
    }

    public Offre acceptOffer(Long offerId) throws Exception {
        Offre offer = findOffreById(offerId);

        if (offer.getStatus() == OffreStatus.EN_ATTENTE) {
            offer.setStatus(OffreStatus.ACCEPTEE);
            offreRepository.save(offer);
            EmailSender emailSender = new EmailSender();

            String clientEmail = offer.getUseridoffre().getEmail();
            String subject = "Your offer has been accepted by " + offer.getIdcreateur().getNom();
            String text = "Dear " + offer.getUseridoffre().getNom() + ",\n\n" +
                    "We are pleased to inform you that your offer has been accepted by the content creator, " + offer.getIdcreateur().getNom() + ". Here are the details of the offer:\n\n" +
                    "Offer description: " + offer.getDescription() + "\n" +
                    "Proposed budget: " + offer.getBudget() + " EUR\n" +
                    "Content creator: " + offer.getIdcreateur().getNom() + "\n\n" +
                    "Thank you for placing your trust in us. You can now contact the creator to continue the collaboration. Feel free to reach out to us if you have any further questions.\n\n" +
                    "Best regards,\nThe Administration Team";
            emailSender.sendEmail(clientEmail, subject, text);

        } else {
            throw new Exception("Offer is not in pending status");
        }

        return offer;
    }

    public Offre declineOffer(Long offerId) throws Exception {
        Offre offer = findOffreById(offerId);

        if (offer.getStatus() == OffreStatus.EN_ATTENTE) {
            offer.setStatus(OffreStatus.REFUSEE);
            offreRepository.save(offer);

            EmailSender emailSender = new EmailSender();
            String clientEmail = offer.getUseridoffre().getEmail();
            String subject = "Your offer has been declined by " + offer.getIdcreateur().getNom();
            String text = "Dear " + offer.getUseridoffre().getNom() + ",\n\n" +
                    "We regret to inform you that your offer has been declined by the content creator, " + offer.getIdcreateur().getNom() + ". Here are the details of the declined offer:\n\n" +
                    "Offer description: " + offer.getDescription() + "\n" +
                    "Proposed budget: " + offer.getBudget() + " EUR\n" +
                    "Content creator: " + offer.getIdcreateur().getNom() + "\n\n" +
                    "We understand this may be disappointing. Feel free to submit a new offer or explore other available creators on our platform.\n\n" +
                    "Best regards,\nThe Administration Team";
            emailSender.sendEmail(clientEmail, subject, text);
        } else {
            throw new Exception("L'offre n'est pas dans un état en attente");
        }

        return offer;
    }
    public Offre completeOffer(Long offerId) throws Exception {
        Offre offer = findOffreById(offerId);

        if (offer.getStatus() == OffreStatus.ACCEPTEE) {
            offer.setStatus(OffreStatus.TERMINEE);
            offreRepository.save(offer);

            EmailSender emailSender = new EmailSender();
            String clientEmail = offer.getUseridoffre().getEmail();
            String subject = "Your offer has been completed by " + offer.getIdcreateur().getNom();
            String text = "Dear " + offer.getUseridoffre().getNom() + ",\n\n" +
                    "The content creator " + offer.getIdcreateur().getNom() + " has completed your offer.\n" +
                    "You can now evaluate their performance.\n\n" +
                    "Best regards,\nThe Administration Team";

            emailSender.sendEmail(clientEmail, subject, text);

        } else {
            throw new Exception("L'offre n'est pas dans un état accepté");
        }

        return offer;
    }
    public long countAllOffers() {
        return offreRepository.count();
    }
    public Map<String, Long> getOfferStatusCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("PENDING", offreRepository.countByStatus(OffreStatus.EN_ATTENTE));
        counts.put("ACCEPTED", offreRepository.countByStatus(OffreStatus.ACCEPTEE));
        counts.put("DECLINED", offreRepository.countByStatus(OffreStatus.REFUSEE));
        counts.put("COMPLETED", offreRepository.countByStatus(OffreStatus.TERMINEE));
        counts.put("EXPIRED", offreRepository.countByStatus(OffreStatus.EXPIRE));
        return counts;
    }
    public Double getAverageBudget() {
        return offreRepository.calculateAverageBudget();
    }
    public Map<String, Long> getOffersGroupedByMonth() {
        Map<String, Long> monthlyOfferCounts = new HashMap<>();
        List<Offre> allOffers = offreRepository.findAll();
        for (Offre offer : allOffers) {
            String monthYear = offer.getDateSoumission().getMonth().toString() + " " + offer.getDateSoumission().getYear();
            monthlyOfferCounts.put(monthYear, monthlyOfferCounts.getOrDefault(monthYear, 0L) + 1);
        }
        return monthlyOfferCounts;
    }
    public List<Offre> getOffersForClient(Long clientId) {
        return offreRepository.findByUseridoffreId(clientId);
    }

    public Double getTotalBudgetForClient(Long clientId) {
        return offreRepository.calculateTotalBudgetByClientId(clientId);
    }

    public Map<String, Long> getOfferStatusCountsForClient(Long clientId) {
        Map<String, Long> counts = new HashMap<>();
        counts.put("PENDING", offreRepository.countByUseridoffreIdAndStatus(clientId, OffreStatus.EN_ATTENTE));
        counts.put("ACCEPTED", offreRepository.countByUseridoffreIdAndStatus(clientId, OffreStatus.ACCEPTEE));
        counts.put("DECLINED", offreRepository.countByUseridoffreIdAndStatus(clientId, OffreStatus.REFUSEE));
        counts.put("COMPLETED", offreRepository.countByUseridoffreIdAndStatus(clientId, OffreStatus.TERMINEE));
        counts.put("EXPIRED", offreRepository.countByUseridoffreIdAndStatus(clientId, OffreStatus.EXPIRE));
        return counts;
    }
    public long getTotalOffersForCreator(Long creatorId) {
        return offreRepository.countByIdcreateurId(creatorId);
    }
    public Map<String, Long> getOfferStatusCountsForCreator(Long creatorId) {
        Map<String, Long> counts = new HashMap<>();
        counts.put("PENDING", offreRepository.countByIdcreateurIdAndStatus(creatorId, OffreStatus.EN_ATTENTE));
        counts.put("ACCEPTED", offreRepository.countByIdcreateurIdAndStatus(creatorId, OffreStatus.ACCEPTEE));
        counts.put("DECLINED", offreRepository.countByIdcreateurIdAndStatus(creatorId, OffreStatus.REFUSEE));
        counts.put("COMPLETED", offreRepository.countByIdcreateurIdAndStatus(creatorId, OffreStatus.TERMINEE));
        counts.put("EXPIRED", offreRepository.countByIdcreateurIdAndStatus(creatorId, OffreStatus.EXPIRE));
        return counts;
    }
    public Double getAverageBudgetForCreator(Long creatorId) {
        return offreRepository.calculateAverageBudgetForCreator(creatorId);
    }
    public Double getTotalEarningsForCreator(Long creatorId) {
        return offreRepository.calculateTotalBudgetByCreatorIdAndStatus(creatorId, OffreStatus.TERMINEE);
    }
    public double getCreatorAverageRating(Long creatorId) {
        Createur creator = createurRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));
        return creator.getAverageRating();
    }

    public String getImprovementSuggestionsForCreator(Long creatorId) {
        double averageRating = getCreatorAverageRating(creatorId);
        if (averageRating < 3) {
            return "Consider improving communication and adhering to deadlines.";
        } else if (averageRating < 4) {
            return "You are doing well! Focus on enhancing the client experience to achieve higher ratings.";
        }
        return "Excellent work! Keep maintaining your quality to sustain high ratings.";
    }
}

