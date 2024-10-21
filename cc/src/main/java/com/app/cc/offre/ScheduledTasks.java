package com.app.cc.offre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasks {
    @Autowired
    private OffreService offreService;

    @Scheduled(fixedRate = 3600000)  // Runs every hour
    public void expirePendingOffers() {
        offreService.checkExpiredOffers();
    }
}
