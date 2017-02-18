package com.github.tarkil.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.github.tarkil.domain.enumeration.Rarity;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "card")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cost")
    private String cost;

    @Size(max = 150)
    @Column(name = "text_flavour", length = 150)
    private String textFlavour;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "attack")
    private Integer attack;

    @Column(name = "defense")
    private Integer defense;

    @Column(name = "trait")
    private String trait;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity")
    private Rarity rarity;

    @OneToOne
    @JoinColumn(unique = true)
    private CardType type;

    @OneToMany(mappedBy = "card")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reprint> reprints = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Card name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public Card cost(String cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTextFlavour() {
        return textFlavour;
    }

    public Card textFlavour(String textFlavour) {
        this.textFlavour = textFlavour;
        return this;
    }

    public void setTextFlavour(String textFlavour) {
        this.textFlavour = textFlavour;
    }

    public String getDescription() {
        return description;
    }

    public Card description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAttack() {
        return attack;
    }

    public Card attack(Integer attack) {
        this.attack = attack;
        return this;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public Card defense(Integer defense) {
        this.defense = defense;
        return this;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public String getTrait() {
        return trait;
    }

    public Card trait(String trait) {
        this.trait = trait;
        return this;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Card rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public CardType getType() {
        return type;
    }

    public Card type(CardType cardType) {
        this.type = cardType;
        return this;
    }

    public void setType(CardType cardType) {
        this.type = cardType;
    }

    public Set<Reprint> getReprints() {
        return reprints;
    }

    public Card reprints(Set<Reprint> reprints) {
        this.reprints = reprints;
        return this;
    }

    public Card addReprints(Reprint reprint) {
        this.reprints.add(reprint);
        reprint.setCard(this);
        return this;
    }

    public Card removeReprints(Reprint reprint) {
        this.reprints.remove(reprint);
        reprint.setCard(null);
        return this;
    }

    public void setReprints(Set<Reprint> reprints) {
        this.reprints = reprints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        if (card.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Card{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", cost='" + cost + "'" +
            ", textFlavour='" + textFlavour + "'" +
            ", description='" + description + "'" +
            ", attack='" + attack + "'" +
            ", defense='" + defense + "'" +
            ", trait='" + trait + "'" +
            ", rarity='" + rarity + "'" +
            '}';
    }
}
