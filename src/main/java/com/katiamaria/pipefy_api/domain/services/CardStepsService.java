package com.katiamaria.pipefy_api.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.katiamaria.pipefy_api.domain.entities.Card;
import com.katiamaria.pipefy_api.domain.repositories.CardStepsRepository;
import com.katiamaria.pipefy_api.domain.entities.ChildRelation;

@Service
public class CardStepsService {
    
    private final CardStepsRepository cardStepsRepository;

    public CardStepsService(CardStepsRepository cardRepository) {
        this.cardStepsRepository = cardRepository;
    }

     public void processCardsInPhase(String phaseId) {
        List<Card> cards = cardStepsRepository.getCardsByPhase(phaseId);

        for (Card card : cards) {

            if (hasChildRelations(card)) {
                moveCardToPhase(card, "325341388"); 
            } else {
                moveCardToPhase(card, "325145812");
            }
        }
    }

    private boolean hasChildRelations(Card card) {
        if (card.getChildRelations() != null && !card.getChildRelations().isEmpty()) {
            for (ChildRelation childRelation : card.getChildRelations()) {
                if (childRelation.getCards() != null && !childRelation.getCards().isEmpty()) {
                    for (Card childCard : childRelation.getCards()) {
                        if (childCard.getId() != null && !childCard.getId().trim().isEmpty()) {
                            return true; 
                        }
                    }
                }
            }
        }
        return false;
    }
    
    
    

    private void moveCardToPhase(Card card, String phaseId) {
        
        cardStepsRepository.updateCardPhase(card.getId(), phaseId);
    }

}
