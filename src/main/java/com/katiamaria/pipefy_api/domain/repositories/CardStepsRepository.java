package com.katiamaria.pipefy_api.domain.repositories;

import java.util.List;

import com.katiamaria.pipefy_api.domain.entities.Card;

public interface CardStepsRepository {

    List<Card> getCardsByPhase(String phaseId);

    void updateCardPhase(String cardId, String phaseId);
} 