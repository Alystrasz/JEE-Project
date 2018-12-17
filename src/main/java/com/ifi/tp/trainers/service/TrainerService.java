package com.ifi.tp.trainers.service;

import com.ifi.tp.trainers.bo.Trainer;

import java.util.List;

public interface TrainerService {

    List<Trainer> getAllTrainers();

    Trainer getTrainer(String name);
}
