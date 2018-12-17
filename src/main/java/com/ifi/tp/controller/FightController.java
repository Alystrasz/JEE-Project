package com.ifi.tp.controller;

import com.ifi.tp.fight.service.FightService;
import com.ifi.tp.trainers.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class FightController {

    @Autowired
    private FightService fightService;

    @PostMapping("/fights/{trainer1}/{trainer2}")
    void launchFight(@PathVariable String trainer1, @PathVariable String trainer2) {
        List<String> logs = fightService.fight(trainer1, trainer2); //TODO : récupérer les trainers
        // TODO : enregistrer les logs en bdd
    }

}
