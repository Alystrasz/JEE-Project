package com.ifi.tp.fight.service;

import com.ifi.tp.bo.Fight;
import com.ifi.tp.trainers.bo.Trainer;

public interface FightService {
    Fight fight(Trainer t1, Trainer t2);

    Iterable<Fight> getTrainerFights(Trainer t);

    Fight getFight(int id);
}
