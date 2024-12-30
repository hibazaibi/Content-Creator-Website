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

        @PostMapping("/evaluate/{id}")
        public ResponseEntity<EvaluationResponse> addEvaluation(@PathVariable Long id,@RequestBody EvaluationRequest request) {
            try {
                EvaluationResponse response = evaluationService.addEvaluation(id,request);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }


}
