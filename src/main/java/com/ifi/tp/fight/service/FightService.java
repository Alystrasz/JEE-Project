package com.ifi.tp.fight.service;

import com.ifi.tp.trainers.bo.Trainer;

import java.util.List;

public interface FightService {

    List<String> fight(Trainer t1, Trainer t2);
    List<String> getTrainerFights(Trainer t);

}
