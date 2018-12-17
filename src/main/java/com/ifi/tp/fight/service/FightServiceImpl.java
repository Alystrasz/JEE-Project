package com.ifi.tp.fight.service;

import com.ifi.tp.bo.Fight;
import com.ifi.tp.repositories.FightRepository;
import com.ifi.tp.trainers.bo.Pokemon;
import com.ifi.tp.trainers.bo.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FightServiceImpl implements FightService {

    private final FightRepository fightRepository;

    @Autowired
    public FightServiceImpl(FightRepository fightRepository) {
        this.fightRepository = fightRepository;
    }

    @Override
    public Fight fight(Trainer t1, Trainer t2) {
        List<Pokemon> team1 = t1.getTeam();
        List<Pokemon> team2 = t2.getTeam();
        List<String> logs = new ArrayList<>();

        // tant que les 2 équipes contiennent au moins un pokémon
        while (!team1.isEmpty() && !team2.isEmpty()) {

            Pokemon p1 = team1.get(0);
            Pokemon p2 = team2.get(0);

            String p1name = p1.getType().getName(),
                    p2name = p2.getType().getName();

            // stats du pokémon 1
            int p1speed = p1.getType().getStats().getSpeed() + p1.getLevel(),
                    p1defence = p1.getType().getStats().getDefense() + p1.getLevel(),
                    p1attack = p1.getType().getStats().getAttack() + p1.getLevel(),
                    p1hp = p1.getType().getStats().getHp() + p1.getLevel();

            // stats du pokémon 2
            int p2speed = p2.getType().getStats().getSpeed() + p2.getLevel(),
                    p2defence = p2.getType().getStats().getDefense() + p2.getLevel(),
                    p2attack = p2.getType().getStats().getAttack() + p2.getLevel(),
                    p2hp = p2.getType().getStats().getHp() + p2.getLevel();

            p1.setHp(p1hp);
            p2.setHp(p2hp);

            boolean p2HitsFirst = (p2speed > p1speed);
            if (p2HitsFirst)
                logs.add(p2name + " hits first!");
            else
                logs.add(p1name + " hits first!");

            // combat entre les 2 pokémons
            while ((p1.getHp() > 0) && (p2.getHp() > 0)) {

                if (p2HitsFirst) {
                    int damage = p2attack - p1defence;
                    if (damage <= 0) damage = 1;

                    p1.setHp(p1.getHp() - damage);
                    logs.add(p2name + " attacks! " + p1name + " looses " + damage + " HP.");

                    damage = p1attack - p2defence;
                    if (damage <= 0) damage = 1;

                    p2.setHp(p2.getHp() - damage);
                    logs.add(p1name + " attacks! " + p2name + " looses " + damage + " HP.");
                } else {
                    int damage = p1attack - p2defence;
                    if (damage <= 0) damage = 1;

                    p2.setHp(p2.getHp() - damage);
                    logs.add(p1name + " attacks! " + p2name + " looses " + damage + " HP.");

                    damage = p2attack - p1defence;
                    if (damage <= 0) damage = 1;

                    p1.setHp(p1.getHp() - damage);
                    logs.add(p2name + " attacks! " + p1name + " looses " + damage + " HP.");
                }

            }

            if (p1.getHp() <= 0) {
                logs.add(p1name + " is KO!");
                team1.remove(p1);
            } else if (p2.getHp() <= 0) {
                logs.add(p2name + " is KO!");
                team2.remove(p2);
            } else {
                logs.add("bug");
            }

        }


        boolean t2Win = team1.isEmpty();
        boolean t1Win = !t2Win;

        if (t2Win) {
            logs.add(t1.getName() + "'s whole team is KO! " + t2.getName() + " wins the battle!");
        } else {
            logs.add(t2.getName() + "'s whole team is KO! " + t1.getName() + " wins the battle!");
        }

        Fight f = new Fight(t1.getName(), t2.getName(), t1Win ? t1.getName() : t2.getName(), t1Win ? t2.getName() : t1.getName(), logs);

        this.fightRepository.save(f);

        return f;
    }

    @Override
    public Iterable<Fight> getTrainerFights(Trainer t) {
        String tname = t.getName();
        return this.fightRepository.findByTrainer1OrTrainer2(tname, tname);
    }

    @Override
    public Fight getFight(int id) {
        return this.fightRepository.findById(id).orElse(null);
    }

}
