package com.app.cc.offre;


import com.app.cc.Client.ClientRepository;
import com.app.cc.Createur.CreateurRepository;
import com.app.cc.user.UserRepository;
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

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final CreateurRepository createurRepository;




    public OffreResponse addOffer(OffreRequest request) throws Exception {
                var  userOpt = clientRepository.findById(request.getUseridoffre()) ;
                var  usercreateur= createurRepository.findById(request.getIdcreateur()) ;

                if (userOpt.isPresent() && usercreateur.isPresent())  {


                    Offre offer = Offre.builder()
                            .description(request.getDescription())
                            .budget(request.getBudget())
                            .status(OffreStatus.EN_ATTENTE)
                            .dateSoumission(LocalDateTime.now())
                            .useridoffre(userOpt.get())
                            .idcreateur(usercreateur.get())
                            .build();

                    offreRepository.save(offer);
                    return OffreResponse.builder()
                            .description(offer.getDescription())
                            .budget(request.getBudget())
                            .status(OffreStatus.EN_ATTENTE)
                            .dateSoumission(LocalDateTime.now())
                            .useridoffre(offer.getUseridoffre().getEmail())
                            .idcreateur(offer.getIdcreateur().getEmail())
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
