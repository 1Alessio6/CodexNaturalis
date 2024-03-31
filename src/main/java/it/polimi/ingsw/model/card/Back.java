package it.polimi.ingsw.model.card;

import java.util.Set;

public class Back extends Face{
    private Set<Symbol> resources;

    public Set<Symbol> getResources() {
        return resources;
    }

    public void setResources(Set<Symbol> resources) {
        this.resources = resources;
    }
}
