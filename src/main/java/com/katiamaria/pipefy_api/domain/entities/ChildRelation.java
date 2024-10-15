package com.katiamaria.pipefy_api.domain.entities;
import java.util.List;

public class ChildRelation {
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}