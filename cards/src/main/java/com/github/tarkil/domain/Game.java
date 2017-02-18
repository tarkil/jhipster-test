package com.github.tarkil.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Size(max = 250)
    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "launch_date")
    private LocalDate launchDate;

    @OneToMany(mappedBy = "game")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Block> blocks = new HashSet<>();

    @OneToMany(mappedBy = "game")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CardType> cardTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Game name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Game description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public Game launchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
        return this;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Set<Block> getBlocks() {
        return blocks;
    }

    public Game blocks(Set<Block> blocks) {
        this.blocks = blocks;
        return this;
    }

    public Game addBlocks(Block block) {
        this.blocks.add(block);
        block.setGame(this);
        return this;
    }

    public Game removeBlocks(Block block) {
        this.blocks.remove(block);
        block.setGame(null);
        return this;
    }

    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    public Set<CardType> getCardTypes() {
        return cardTypes;
    }

    public Game cardTypes(Set<CardType> cardTypes) {
        this.cardTypes = cardTypes;
        return this;
    }

    public Game addCardTypes(CardType cardType) {
        this.cardTypes.add(cardType);
        cardType.setGame(this);
        return this;
    }

    public Game removeCardTypes(CardType cardType) {
        this.cardTypes.remove(cardType);
        cardType.setGame(null);
        return this;
    }

    public void setCardTypes(Set<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        if (game.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", launchDate='" + launchDate + "'" +
            '}';
    }
}
