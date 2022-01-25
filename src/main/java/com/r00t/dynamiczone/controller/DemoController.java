package com.r00t.dynamiczone.controller;

import com.r00t.dynamiczone.model.RequestPayload;
import com.r00t.dynamiczone.model.ResponsePayload;
import com.r00t.dynamiczone.util.TranslateZonedVariables;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DemoController {

    @PostMapping
    @TranslateZonedVariables
    public ResponseEntity<?> demo(@RequestBody RequestPayload payload) {
        ResponsePayload response = new ResponsePayload();
        response.setOriginalStartTimestamp(payload.getStartTimestamp());
        response.setOriginalBirthDate(payload.getBirthDate());
        response.setTranslatedStartTimestamp(payload.getStartTimestamp());
        response.setTranslatedBirthDate(payload.getBirthDate());
        return ResponseEntity.ok(response);
    }
}