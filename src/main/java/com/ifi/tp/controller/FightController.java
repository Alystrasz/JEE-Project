package com.ifi.tp.controller;

import com.ifi.tp.bo.Fight;
import com.ifi.tp.fight.service.FightService;
import com.ifi.tp.trainers.bo.Trainer;
import com.ifi.tp.trainers.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FightController {

    @Autowired
    private FightService fightService;

    @Autowired
    private TrainerService trainerService;

    @PostMapping("/fights/{trainer1}/{trainer2}")
    void launchFight(@PathVariable String trainer1, @PathVariable String trainer2) {
        Trainer t1 = trainerService.getTrainer(trainer1);
        Trainer t2 = trainerService.getTrainer(trainer2);
        if (t1 != null || t2 != null) {
            fightService.fight(t1, t2);
        }
    }

    @GetMapping(value = "/fights/{trainer}")
    @ResponseBody
    Iterable<Fight> listFights(@PathVariable String trainer) {
        Iterable<Fight> fights = fightService.getTrainerFights(trainerService.getTrainer(trainer));
        fights.forEach(fight -> {
            fight.isWin(trainer);
        });
        return fights;
    }

    @GetMapping(value = "/fights/fight/{id}")
    ModelAndView getFight(@PathVariable String id) {
        var modelAndView = new ModelAndView("fight");
        modelAndView.addObject("fight", fightService.getFight(Integer.valueOf(id)));
        return modelAndView;
    }

}
