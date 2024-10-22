package com.app.cc.dispute;
import com.app.cc.Client.Client;

import com.app.cc.Createur.Createur;
import com.app.cc.email.EmailSender;
import com.app.cc.exeption.UserNotFoundException;
import com.app.cc.offre.Offre;
import com.app.cc.offre.OffreNotFoundException;
import com.app.cc.offre.OffreRepository;
import com.app.cc.user.Role;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class DisputeService {
    @Autowired
    private final DisputeRepository disputeRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OffreRepository offreRepository;

    public Dispute createDispute(Long offerId, Long userId, String reason, String details) throws Exception {
        Offre offer = offreRepository.findById(offerId)
                .orElseThrow(() -> new Exception("Offer not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        boolean isClient = user instanceof Client;
        boolean isCreator = user instanceof Createur;

        if (!isClient && !isCreator) {
            throw new Exception("User must be either a client or creator");
        }

        if (isClient && !offer.getUseridoffre().getId().equals(userId)) {
            throw new Exception("Client not associated with this offer");
        } else if (isCreator && !offer.getIdcreateur().getId().equals(userId)) {
            throw new Exception("Creator not associated with this offer");
        }

        Dispute dispute = Dispute.builder()
                .offer(offer)
                .client(isClient ? (Client) user : null)
                .creator(isCreator ? (Createur) user : null)
                .raison(reason)
                .Detailsresolution(details)
                .status(DisputeStatus.OUVERT)
                .dateSoumis(LocalDateTime.now())
                .build();

        return disputeRepository.save(dispute);
    }


    public Dispute resolveDispute(Long disputeId, Long adminId, String resolution, String adminNotes) throws Exception {
        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new Exception("Dispute not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new Exception("Admin not found"));

        if (!admin.getRole().equals(Role.ADMIN)) {
            throw new Exception("Only admins can resolve disputes");
        }

        dispute.setStatus(DisputeStatus.RESOLU);
        dispute.setNotesAdmin(adminNotes);
        dispute.setDateResolution(LocalDateTime.now());
        dispute.setAdmin(admin);
        notifyResolution(dispute);

        return disputeRepository.save(dispute);
    }

    private void notifyResolution(Dispute dispute) throws MessagingException {
        EmailSender emailSender = new EmailSender();

        if (dispute.getClient() != null) {
            emailSender.sendEmail(dispute.getClient().getEmail(),
                    "Conflit Résolu",
                    "Cher(e) " + dispute.getClient().getNom() + ",\n\n" +
                            "Nous avons le plaisir de vous informer que votre réclamation concernant l'offre ID : " + dispute.getOffer().getIdOffre() +
                            " a été résolu.\n\n" +
                            "Nous vous remercions de votre patience pendant le processus de résolution. " +
                            "Si vous avez des questions ou des préoccupations supplémentaires, n'hésitez pas à nous contacter.\n\n" +
                            "Cordialement,\nL'équipe d'administration");
        }

        if (dispute.getCreator() != null) {
            emailSender.sendEmail(dispute.getCreator().getEmail(),
                    "Conflit Résolu",
                    "Cher(e) " + dispute.getCreator().getNom() + ",\n\n" +
                            "Nous vous informons que la réclamation concernant votre travail sur l'offre ID : " + dispute.getOffer().getIdOffre() +
                            " a été résolu.\n\n" +
                            "Nous vous remercions pour votre collaboration et compréhension. " +
                            "N'hésitez pas à nous contacter si vous avez des questions ou des préoccupations.\n\n" +
                            "Cordialement,\nL'équipe d'administration");
        }
    }


    public List<Dispute> getAllDisputes() {
        return disputeRepository.findAll();
    }

    public Dispute getDisputeById(Long id) throws Exception {
        return disputeRepository.findById(id)
                .orElseThrow(() -> new Exception("Dispute not found"));
    }
}

