package com.github.tarkil.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.github.tarkil.domain.enumeration.Rarity;

/**
 * A DTO for the Card entity.
 */
public class CardDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String cost;

    @Size(max = 150)
    private String textFlavour;

    @Size(max = 500)
    private String description;

    private Integer attack;

    private Integer defense;

    private String trait;

    private Rarity rarity;

    private Long typeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getTextFlavour() {
        return textFlavour;
    }

    public void setTextFlavour(String textFlavour) {
        this.textFlavour = textFlavour;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }
    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }
    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }
    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long cardTypeId) {
        this.typeId = cardTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CardDTO cardDTO = (CardDTO) o;

        if ( ! Objects.equals(id, cardDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CardDTO{" +
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
