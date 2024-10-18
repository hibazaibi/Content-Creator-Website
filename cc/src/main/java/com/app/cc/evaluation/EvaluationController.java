package com.app.cc.evaluation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

}
