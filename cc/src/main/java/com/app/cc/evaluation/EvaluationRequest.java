package com.app.cc.evaluation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class EvaluationRequest {
    private Long clientId;
    private Long creatorId;
    private int rating;
    private String feedback;
}
