package com.ifi.tp.pokemonTypes.service;

import com.ifi.tp.pokemonTypes.bo.PokemonType;

import java.util.List;

public interface PokemonService {

    List<PokemonType> listPokemonsTypes();

    PokemonType getPokemonType(int id);

}
