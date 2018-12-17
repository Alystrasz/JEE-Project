package com.ifi.tp.trainers.service;

import com.ifi.tp.bo.HPNotification;
import com.ifi.tp.pokemonTypes.bo.PokemonType;
import com.ifi.tp.pokemonTypes.service.PokemonService;
import com.ifi.tp.trainers.bo.Pokemon;
import com.ifi.tp.trainers.bo.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @Test
    void getAllTrainers_shouldCallTheRemoteService() {
        var restTemplate = mock(RestTemplate.class);
        var trainerServiceImpl = new TrainerServiceImpl();
        trainerServiceImpl.setRestTemplate(restTemplate);
        trainerServiceImpl.setTrainerServiceUrl("http://localhost:8081");

        var expectedUrl = "http://localhost:8081/trainers";
        var trainers = new Trainer[]{new Trainer(), new Trainer()};
        when(restTemplate.getForObject(expectedUrl, Trainer[].class)).thenReturn(trainers);

        var result = trainerServiceImpl.getAllTrainers();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(restTemplate).getForObject("http://localhost:8081/trainers", Trainer[].class);
    }

    @Test
    void getTrainer_shouldCallTheRemoteService() {
        var restTemplate = mock(RestTemplate.class);
        var trainerServiceImpl = new TrainerServiceImpl();
        trainerServiceImpl.setRestTemplate(restTemplate);
        trainerServiceImpl.setTrainerServiceUrl("http://localhost:8081");

        var pokemonService = mock(PokemonService.class);
        trainerServiceImpl.setPokemonService(pokemonService);

        var expectedUrl = "http://localhost:8081/trainers/{name}";

        var ash = new Trainer();
        var pikachu = new Pokemon();
        pikachu.setPokemonNumber(25);
        ash.setTeam(List.of(pikachu));

        when(restTemplate.getForObject(expectedUrl, Trainer.class, "Ash")).thenReturn(ash);

        var result = trainerServiceImpl.getTrainer("Ash");

        assertNotNull(result);

        verify(restTemplate).getForObject("http://localhost:8081/trainers/{name}", Trainer.class, "Ash");
    }

    @Test
    void getTrainer_shouldCallTheRemoteService_andEnrichPokemonData() {
        var restTemplate = mock(RestTemplate.class);
        var trainerServiceImpl = new TrainerServiceImpl();
        trainerServiceImpl.setRestTemplate(restTemplate);
        trainerServiceImpl.setTrainerServiceUrl("http://localhost:8081");

        var pokemonService = mock(PokemonService.class);
        trainerServiceImpl.setPokemonService(pokemonService);

        var expectedUrl = "http://localhost:8081/trainers/{name}";

        var ash = new Trainer();
        var pikachu = new Pokemon();
        pikachu.setPokemonNumber(25);
        ash.setTeam(List.of(pikachu));

        when(restTemplate.getForObject(expectedUrl, Trainer.class, "Ash")).thenReturn(ash);

        var result = trainerServiceImpl.getTrainer("Ash");

        assertNotNull(result);

        verify(restTemplate).getForObject("http://localhost:8081/trainers/{name}", Trainer.class, "Ash");
        verify(pokemonService).getPokemonType(25);
    }

    @Test
    void receiveNotification_shouldSendAMessageForFullHPRecovery_throughWebSocketToTheRightTrainer() {
        var simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        var pokemonService = mock(PokemonService.class);

        var pikachuType = new PokemonType();
        pikachuType.setName("pikachu");
        when(pokemonService.getPokemonType(25)).thenReturn(pikachuType);

        var trainerService = new TrainerServiceImpl();
        trainerService.setPokemonService(pokemonService);
        trainerService.setSimpMessagingTemplate(simpMessagingTemplate);

        var ash = new Trainer();
        ash.setName("Ash");
        var pikachu = new Pokemon();
        pikachu.setPokemonNumber(25);

        var hpNotification = new HPNotification();
        hpNotification.setFullHP(true);
        hpNotification.setTrainer(ash);
        hpNotification.setPokemon(pikachu);
        trainerService.receiveNotification(hpNotification);

        verify(simpMessagingTemplate).convertAndSendToUser("Ash", "/topic/notification", "Your pikachu has recovered all of its HP !");
    }

    @Test
    void receiveNotification_shouldSendAMessageForOneHPRecovery_throughWebSocketToTheRightTrainer() {
        var simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        var pokemonService = mock(PokemonService.class);

        var pikachuType = new PokemonType();
        pikachuType.setName("pikachu");
        when(pokemonService.getPokemonType(25)).thenReturn(pikachuType);

        var trainerService = new TrainerServiceImpl();
        trainerService.setPokemonService(pokemonService);
        trainerService.setSimpMessagingTemplate(simpMessagingTemplate);

        var ash = new Trainer();
        ash.setName("Ash");
        var pikachu = new Pokemon();
        pikachu.setPokemonNumber(25);

        var hpNotification = new HPNotification();
        hpNotification.setFullHP(false);
        hpNotification.setTrainer(ash);
        hpNotification.setPokemon(pikachu);
        trainerService.receiveNotification(hpNotification);

        verify(simpMessagingTemplate).convertAndSendToUser("Ash", "/topic/notification", "Your pikachu has recovered one HP !");
    }
}