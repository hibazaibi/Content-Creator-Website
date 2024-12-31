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

    public Dispute createDispute(Long offerId, Long requesterId, String reason, String details) throws Exception {
        Offre offer = offreRepository.findById(offerId)
                .orElseThrow(() -> new Exception("Offer not found"));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new Exception("User not found"));

        boolean isClient = offer.getUseridoffre().getId().equals(requesterId);
        boolean isCreator = offer.getIdcreateur().getId().equals(requesterId);

        if (!isClient && !isCreator) {
            throw new Exception("Requester is not associated with this offer");
        }

        Dispute dispute = Dispute.builder()
                .offer(offer)
                .client(isClient ? (Client) requester : null)
                .creator(isCreator ? (Createur) requester : null)
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
                    "Dispute Resolved",
                    "Dear " + dispute.getClient().getNom() + ",\n\n" +
                            "We are pleased to inform you that your complaint regarding the offer number: " + dispute.getOffer().getIdOffre() + " has been resolved.\n\n" +
                            "Thank you for your patience during the resolution process.\n\n" +
                            "If you have any further questions or concerns, please feel free to contact us.\n\n" +
                            "Sincerely,\nThe Admin Team");
        }

        if (dispute.getCreator() != null) {
            emailSender.sendEmail(dispute.getCreator().getEmail(),
                    "Dispute Resolved",
                    "Dear " + dispute.getCreator().getNom() + ",\n\n" +
                            "We would like to inform you that the complaint regarding your work on offer number: " + dispute.getOffer().getIdOffre() + " has been resolved.\n\n" +
                            "Thank you for your collaboration and understanding.\n\n" +
                            "Please don't hesitate to contact us if you have any questions or concerns.\n\n" +
                            "Sincerely,\nThe Admin Team");
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

