package com.app.cc.offre;


import com.app.cc.Client.ClientRepository;
import com.app.cc.Createur.CreateurRepository;
import com.app.cc.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


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
                    .Deadline(request.getDeadline())
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
                    .Deadline(offer.getDeadline())
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

    public Offre findOffreById(Long id) {
        return offreRepository.findById(id)
                .orElseThrow(() -> new OffreNotFoundException("Offer with ID " + id + " not found"));
    }
    public Offre findOffreByUserId(Long id) {
        return offreRepository.findByUseridoffre(id)
                .orElseThrow(() -> new OffreNotFoundException("Offer with ID " + id + " not found"));
    }

    public void deleteOffreById(Long id) throws Exception {
        Offre offer = offreRepository.findById(id)
                .orElseThrow(() -> new OffreNotFoundException("Offer with ID: " + id + " not found"));
        offreRepository.deleteById(id);
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
            String subject = "Votre offre a été acceptée par " + offer.getIdcreateur().getNom();
            String text = "Cher(e) " + offer.getUseridoffre().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que votre offre a été acceptée par le créateur de contenu, " + offer.getIdcreateur().getNom() + ". Voici les détails de l'offre :\n\n" +
                    "Description de l'offre : " + offer.getDescription() + "\n" +
                    "Budget proposé : " + offer.getBudget() + " EUR\n" +
                    "Créateur de contenu : " + offer.getIdcreateur().getNom() + "\n\n" +
                    "Nous vous remercions de la confiance que vous nous accordez. Vous pouvez maintenant contacter le créateur pour poursuivre la collaboration. N’hésitez pas à nous contacter si vous avez des questions supplémentaires.\n\n" +
                    "Cordialement,\nL'équipe d'administration";
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
            String subject = "Votre offre a été refusée par " + offer.getIdcreateur().getNom();
            String text = "Cher(e) " + offer.getUseridoffre().getNom() + ",\n\n" +
                    "Nous regrettons de vous informer que votre offre a été refusée par le créateur de contenu, " + offer.getIdcreateur().getNom() + ". Voici les détails de l'offre refusée :\n\n" +
                    "Description de l'offre : " + offer.getDescription() + "\n" +
                    "Budget proposé : " + offer.getBudget() + " EUR\n" +
                    "Créateur de contenu : " + offer.getIdcreateur().getNom() + "\n\n" +
                    "Nous comprenons que cela puisse être décevant. N'hésitez pas à soumettre une nouvelle offre ou à explorer d'autres créateurs disponibles sur notre plateforme.\n\n" +
                    "Cordialement,\nL'équipe d'administration";

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
            String subject = "Votre offre a été complétée par " + offer.getIdcreateur().getNom();
            String text = "Cher(e) " + offer.getUseridoffre().getNom() + ",\n\n" +
                    "Le créateur de contenu " + offer.getIdcreateur().getNom() + " a complété votre offre.\n" +
                    "Vous pouvez maintenant évaluer sa performance.\n\n" +
                    "Cordialement,\nL'équipe d'administration";

            emailSender.sendEmail(clientEmail, subject, text);

        } else {
            throw new Exception("L'offre n'est pas dans un état accepté");
        }

        return offer;
    }


}

