package com.ifi.tp.trainers.service;

import com.ifi.tp.bo.HPNotification;
import com.ifi.tp.pokemonTypes.service.PokemonService;
import com.ifi.tp.trainers.bo.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private PokemonService pokemonService;

    private RestTemplate restTemplate;

    private String trainerServiceUrl;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public List<Trainer> getAllTrainers() {
        var url = trainerServiceUrl + "/trainers";
        var trainers = restTemplate.getForObject(url, Trainer[].class);

        return Arrays.asList(trainers);
    }

    @Override
    public Trainer getTrainer(String name) {
        var url = trainerServiceUrl + "/trainers/{name}";
        var trainer = restTemplate.getForObject(url, Trainer.class, name);
        return this.enrich(trainer);
    }

    private Trainer enrich(Trainer trainer) {
        trainer.getTeam()
                .stream()
                .forEach(pokemon -> pokemon.setType(pokemonService.getPokemonType(pokemon.getPokemonNumber())));
        return trainer;
    }

    @Value("${trainer.service.url}")
    void setTrainerServiceUrl(String pokemonServiceUrl) {
        this.trainerServiceUrl = pokemonServiceUrl;
    }

    @Autowired
    void setPokemonService(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @Autowired
    void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @JmsListener(destination = "notification")
    public void receiveNotification(HPNotification notification) {
        var pokemonType = pokemonService.getPokemonType(notification.getPokemon().getPokemonNumber());

        String message;
        if (notification.isFullHP()) {
            message = String.format("Your %s has recovered all of its HP !", pokemonType.getName());
        } else {
            message = String.format("Your %s has recovered one HP !", pokemonType.getName());
        }

        simpMessagingTemplate.convertAndSendToUser(notification.getTrainer().getName(), "/topic/notification", message);
    }

}
