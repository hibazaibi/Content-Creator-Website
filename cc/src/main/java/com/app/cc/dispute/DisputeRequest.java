package com.app.cc.dispute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisputeRequest {
    private Long clientId;
    private Long creatorId;
    private Long offerId;
    private String raison;
    private String Detailsresolution;

}
