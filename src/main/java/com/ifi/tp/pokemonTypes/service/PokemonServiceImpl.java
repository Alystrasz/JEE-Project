package com.ifi.tp.pokemonTypes.service;

import java.util.Arrays;
import java.util.List;

import com.ifi.tp.pokemonTypes.bo.PokemonType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonServiceImpl implements PokemonService {

    private RestTemplate restTemplate;

    private String pokemonServiceUrl;

    @Autowired
    public PokemonServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<PokemonType> listPokemonsTypes() {
        var url = pokemonServiceUrl + "/pokemons";
        var pokemons = restTemplate.getForObject(url, PokemonType[].class);
        return Arrays.asList(pokemons);
    }

    @Override
    public PokemonType getPokemonType(int id) {
        var url = pokemonServiceUrl + "/pokemons/{id}";
        return restTemplate.getForObject(url, PokemonType.class, id);
    }

    @Value("${pokemon.service.url}")
    void setPokemonServiceUrl(String pokemonServiceUrl) {
        this.pokemonServiceUrl = pokemonServiceUrl;
    }
}
