package com.katiamaria.pipefy_api.infrastructure.persistence;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katiamaria.pipefy_api.domain.entities.Card;
import com.katiamaria.pipefy_api.domain.entities.ChildRelation;
import com.katiamaria.pipefy_api.domain.repositories.CardStepsRepository;

@Repository
public class CardStepsRepositoryImpl implements CardStepsRepository {
    
    private final RestTemplate restTemplate;

    @Value("${pipefy.api.url}")
    private String apiUrl;

    @Value("${pipefy.api.key.cardsteps}")
    private String apiKey;

    public CardStepsRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Card> getCardsByPhase(String phaseId) {
        List<Card> allCards = new ArrayList<>();
        String endCursor = null;
        boolean hasNextPage = true;

        while (hasNextPage) {
            String query = String.format(
        "{\"query\":\"{ phase(id: \\\"%s\\\") { cards(first: 50%s) { edges { node { id child_relations { cards { id } } } } pageInfo { hasNextPage endCursor } } } }\"}",
                phaseId,
                (endCursor != null) ? String.format(", after: \\\"%s\\\"", endCursor) : ""
                );


            HttpEntity<String> entity = new HttpEntity<>(query, getAuthHeaders());
            String response = restTemplate.postForObject(apiUrl, entity, String.class);

            List<Card> cardsFromResponse = parseCardsFromResponse(response);
            allCards.addAll(cardsFromResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode pageInfo = rootNode.path("data").path("phase").path("cards").path("pageInfo");

                hasNextPage = pageInfo.path("hasNextPage").asBoolean();
                endCursor = pageInfo.path("endCursor").asText();

            } catch (Exception e) {
                e.printStackTrace();
                hasNextPage = false;
            }
        }

        return allCards;
    }

    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private List<Card> parseCardsFromResponse(String response) {
    List<Card> cards = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    
    try {
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode edges = rootNode.path("data").path("phase").path("cards").path("edges");

        for (JsonNode edge : edges) {
            JsonNode node = edge.path("node");

            Card card = new Card();
            card.setId(node.path("id").asText()); 

            List<ChildRelation> childRelations = new ArrayList<>();
            JsonNode childRelationsNode = node.path("child_relations");
            
            for (JsonNode childRelationNode : childRelationsNode) {
                ChildRelation childRelation = new ChildRelation();
                
                List<Card> childCards = new ArrayList<>();
                JsonNode childCardsNode = childRelationNode.path("cards");

                for (JsonNode childCardNode : childCardsNode) {
                    Card childCard = new Card();
                    childCard.setId(childCardNode.path("id").asText());
                    childCards.add(childCard);
                }
                
                childRelation.setCards(childCards);
                childRelations.add(childRelation);
            }

            card.setChildRelations(childRelations);
            cards.add(card);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return cards;
}

    

    @Override
    public void updateCardPhase(String cardId, String phaseId) {
        String mutation = String.format(
            "{\"query\":\"mutation { moveCardToPhase(input: { card_id: %s, destination_phase_id: \\\"%s\\\" }) { card { id title } } }\"}",
            cardId, phaseId);

        HttpHeaders headers = getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(mutation, headers);

        restTemplate.postForObject(apiUrl, entity, String.class);
    }
}
