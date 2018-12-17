package com.ifi.tp.bo;

import com.ifi.tp.trainers.bo.Pokemon;
import com.ifi.tp.trainers.bo.Trainer;

public class HPNotification {

    private Trainer trainer;

    private Pokemon pokemon;

    private boolean fullHP;

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public boolean isFullHP() {
        return fullHP;
    }

    public void setFullHP(boolean fullHP) {
        this.fullHP = fullHP;
    }

}
