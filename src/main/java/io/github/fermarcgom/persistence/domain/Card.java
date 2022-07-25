package io.github.fermarcgom.persistence.domain;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Introspected
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer id;
    @Column(name = "pokemon_name")
    private String pokemonName;
    @Column(name = "pokemon_type")
    private String pokemonType;
    @Column(name = "pokemon_hp")
    private Integer pokemonHp;
    @Column(name = "evolved_from")
    private String evolvedFrom;
    @Column(name = "image_link")
    private String imageLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public String getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(String pokemonType) {
        this.pokemonType = pokemonType;
    }

    public Integer getPokemonHp() {
        return pokemonHp;
    }

    public void setPokemonHp(Integer pokemonHp) {
        this.pokemonHp = pokemonHp;
    }

    public String getEvolvedFrom() {
        return evolvedFrom;
    }

    public void setEvolvedFrom(String evolvedFrom) {
        this.evolvedFrom = evolvedFrom;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
