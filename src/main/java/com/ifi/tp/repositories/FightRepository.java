package com.ifi.tp.repositories;

import com.ifi.tp.bo.Fight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FightRepository extends CrudRepository<Fight, Integer> {
    List<Fight> findByTrainer1OrTrainer2(String t1, String t2);
}
