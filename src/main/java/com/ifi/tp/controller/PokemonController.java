package com.ifi.tp.controller;

import com.ifi.tp.pokemonTypes.service.PokemonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PokemonController {

    private PokemonService pokemonService;

    @Autowired
    PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemons")
    public ModelAndView pokemons(@RequestParam(defaultValue = "front") String view){
        var modelAndView = new ModelAndView("pokemons");

        modelAndView.addObject("pokemons", pokemonService.listPokemonsTypes());

        if("front".equals(view)){
            modelAndView.addObject("view_front", true);
        }
        else{
            modelAndView.addObject("view_back", true);
        }


        return modelAndView;
    }

}
