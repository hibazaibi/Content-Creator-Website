package com.app.cc.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationRequest {
    private Long clientId;
    private Long creatorId;
    private int rating;
    private String feedback;
}
