package com.github.tarkil.domain;

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
 * A Edition.
 */
@Entity
@Table(name = "edition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "edition")
public class Edition implements Serializable {

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

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "edition_cards",
               joinColumns = @JoinColumn(name="editions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="cards_id", referencedColumnName="id"))
    private Set<Card> cards = new HashSet<>();

    @ManyToOne
    private Block block;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Edition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public Edition launchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
        return this;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public Edition cards(Set<Card> cards) {
        this.cards = cards;
        return this;
    }

    public Edition addCards(Card card) {
        this.cards.add(card);
        return this;
    }

    public Edition removeCards(Card card) {
        this.cards.remove(card);
        return this;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Block getBlock() {
        return block;
    }

    public Edition block(Block block) {
        this.block = block;
        return this;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edition edition = (Edition) o;
        if (edition.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, edition.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Edition{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", launchDate='" + launchDate + "'" +
            '}';
    }
}
