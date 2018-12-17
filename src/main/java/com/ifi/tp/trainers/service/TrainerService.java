package com.ifi.tp.trainers.service;

import java.util.List;

import com.ifi.tp.trainers.bo.Trainer;

public interface TrainerService {

    List<Trainer> getAllTrainers();

    Trainer getTrainer(String name);
}
