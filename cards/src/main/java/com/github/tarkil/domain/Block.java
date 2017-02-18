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
 * A Block.
 */
@Entity
@Table(name = "block")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "block")
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "launch_date", nullable = false)
    private LocalDate launchDate;

    @OneToMany(mappedBy = "block")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Edition> editions = new HashSet<>();

    @ManyToOne
    private Game game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Block name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public Block launchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
        return this;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Set<Edition> getEditions() {
        return editions;
    }

    public Block editions(Set<Edition> editions) {
        this.editions = editions;
        return this;
    }

    public Block addEditions(Edition edition) {
        this.editions.add(edition);
        edition.setBlock(this);
        return this;
    }

    public Block removeEditions(Edition edition) {
        this.editions.remove(edition);
        edition.setBlock(null);
        return this;
    }

    public void setEditions(Set<Edition> editions) {
        this.editions = editions;
    }

    public Game getGame() {
        return game;
    }

    public Block game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Block block = (Block) o;
        if (block.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, block.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Block{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", launchDate='" + launchDate + "'" +
            '}';
    }
}
