package com.ifi.tp.pokemonTypes.service;

import com.ifi.tp.config.RestConfiguration;
import com.ifi.tp.pokemonTypes.bo.PokemonType;
import com.ifi.tp.trainers.bo.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonServiceImplTest {

    private PokemonServiceImpl pokemonServiceImpl;

    private RestTemplate restTemplate;

    private PokemonType pikachu;

    @BeforeEach
    void setup(){
        var url = "http://localhost:8080";
        restTemplate = mock(RestTemplate.class);

        pokemonServiceImpl = new PokemonServiceImpl(restTemplate);
        pokemonServiceImpl.setPokemonServiceUrl(url);

        pikachu = new PokemonType();
        pikachu.setName("pikachu");
        pikachu.setId(25);

    }

    @Test
    void listPokemonsTypes_shouldCallTheRemoteService() {
        var expectedUrl = "http://localhost:8080/pokemons";
        when(restTemplate.getForObject(expectedUrl, PokemonType[].class)).thenReturn(new PokemonType[]{pikachu});

        var pokemons = pokemonServiceImpl.listPokemonsTypes();

        assertNotNull(pokemons);
        assertEquals(1, pokemons.size());

        verify(restTemplate).getForObject(expectedUrl, PokemonType[].class);
    }

    @Test
    void getPokemonType_shouldCallTheRemoteService() {
        var expectedUrl = "http://localhost:8080/pokemons/{id}";
        when(restTemplate.getForObject(expectedUrl, PokemonType.class, 25)).thenReturn(pikachu);

        var pokemon = pokemonServiceImpl.getPokemonType(25);

        assertNotNull(pokemon);
        assertEquals("pikachu", pokemon.getName());

        verify(restTemplate).getForObject(expectedUrl, PokemonType.class, 25);
    }

    @Test
    void pokemonServiceImpl_shouldBeAnnotatedWithService(){
        assertNotNull(PokemonServiceImpl.class.getAnnotation(Service.class));
    }



    @Test
    void setPokemonServiceUrl_shouldBeAnnotatedWithValue() throws NoSuchMethodException {
        var setter = PokemonServiceImpl.class.getDeclaredMethod("setPokemonServiceUrl", String.class);
        var valueAnnotation = setter.getAnnotation(Value.class);
        assertNotNull(valueAnnotation);
        assertEquals("${pokemon.service.url}", valueAnnotation.value());
    }

    @Test
    void pokemonServiceImplConstructor_shouldBeAnnotatedWithAutowired() throws NoSuchMethodException {
        var constructor = PokemonServiceImpl.class.getConstructor(RestTemplate.class);
        assertNotNull(constructor.getAnnotation(Autowired.class));
    }

    @Test
    void restTemplate_shouldBeInjected(){
        var applicationContext = new AnnotationConfigApplicationContext(RestConfiguration.class, PokemonServiceImpl.class);
        assertNotNull(applicationContext.getBean(RestTemplate.class));
        assertNotNull(applicationContext.getBean(RestConfiguration.class));
        assertNotNull(applicationContext.getBean(PokemonServiceImpl.class));
    }
}