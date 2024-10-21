package com.app.cc.evaluation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {


        private final EvaluationService evaluationService;

        @PostMapping("/evaluate")
        public ResponseEntity<EvaluationResponse> addEvaluation(@RequestBody EvaluationRequest request) {
            try {
                EvaluationResponse response = evaluationService.addEvaluation(request);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<EvaluationResponse>> getEvaluationsForCreator(@PathVariable Long creatorId) {
        try {
            List<Evaluation> evaluations = evaluationService.getEvaluationsByCreator(creatorId);

            List<EvaluationResponse> response = evaluations.stream()
                    .map(evaluation -> new EvaluationResponse(evaluation.getIdevaluation(),evaluation.getRating(), evaluation.getFeedback()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
