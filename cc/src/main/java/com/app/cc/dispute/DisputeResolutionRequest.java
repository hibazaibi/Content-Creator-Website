package com.app.cc.dispute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisputeResolutionRequest {
    private String resolution;
    private Long adminId;
    private String adminNotes;
}
