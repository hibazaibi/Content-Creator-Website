package com.app.cc.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

public class EvaluationResponse {
    private Long id;
    private int rating;
    private String feedback;
}
