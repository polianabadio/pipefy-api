package com.katiamaria.pipefy_api.interfaces.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.katiamaria.pipefy_api.domain.services.CardStepsService;


@RestController
@RequestMapping("/api/cardsteps")
public class CardStepsController {

    private final CardStepsService cardStepsService;

    public CardStepsController(CardStepsService cardStepsService) {
        this.cardStepsService = cardStepsService;
    }

    @PostMapping("/process/{phaseId}")
    public void processCardsInPhase(@PathVariable String phaseId) {
        cardStepsService.processCardsInPhase(phaseId);
    }

    @GetMapping("/ping")
    public String ping (){
        return "pong";
    }
}