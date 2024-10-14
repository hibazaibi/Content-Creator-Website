package com.app.cc.auth;

import com.app.cc.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor

    public class registerresponse {
        private String token;
        private String nom;
        private String prenom;
        private String email;
        private String password;


        private Role role ;
        private String msg ;
    }


