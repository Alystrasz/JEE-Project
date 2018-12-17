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
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private FightService fightService;

    @GetMapping("/trainers")
    ModelAndView index() {
        var modelAndView = new ModelAndView("trainers");

        modelAndView.addObject("trainers", trainerService.getAllTrainers());

        return modelAndView;
    }

    @GetMapping("/trainers/{trainer}/arena")
    ModelAndView fightWithTrainers(@PathVariable String trainer) {
        var modelAndView = new ModelAndView("arena");
        List<Trainer> trainers = trainerService.getAllTrainers();
        List<Trainer> trainersList = new ArrayList<>();
        for (Trainer t : trainers) {
            if (!t.getName().equals(trainer)) {
                trainersList.add(t);
            }
        }
        modelAndView.addObject("trainers", trainersList);

        return modelAndView;
    }

    @PostMapping("/trainers/{trainer1}/fight/{trainer2}")
    String fight(@PathVariable String trainer1, @PathVariable String trainer2) {
        Trainer t1 = trainerService.getTrainer(trainer1);
        Trainer t2 = trainerService.getTrainer(trainer2);
        if (t1 != null || t2 != null) {
            Fight f = fightService.fight(t1, t2);
            return "redirect:/fights/fight/" + f.getId();
        }
        return "One of trainer not found";
    }

    @GetMapping("/trainers/{name}")
    ModelAndView index(@PathVariable String name) {
        var modelAndView = new ModelAndView("trainer");

        modelAndView.addObject("trainer", trainerService.getTrainer(name));

        return modelAndView;
    }

    @GetMapping("/trainers/{trainer}/fights")
    ModelAndView listFights(@PathVariable String trainer) {
        var modelAndView = new ModelAndView("fights");

        Iterable<Fight> fights = fightService.getTrainerFights(trainerService.getTrainer(trainer));
        fights.forEach(fight -> {
            fight.isWin(trainer);
        });

        modelAndView.addObject("fights", fights);
        modelAndView.addObject("trainer", trainer);
        return modelAndView;

    }


}
