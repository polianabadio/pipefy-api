package com.katiamaria.pipefy_api.domain.entities;

import java.util.List;

public class Card {
    
    private String id;
    private List<ChildRelation> childRelations;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<ChildRelation> getChildRelations() {
        return childRelations;
    }
    public void setChildRelations(List<ChildRelation> childRelations) {
        this.childRelations = childRelations;
    }
}
