package com.app.cc.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class forgetpass2response {
    private String msg ;
    private String email;

    private String newPassword;
}


