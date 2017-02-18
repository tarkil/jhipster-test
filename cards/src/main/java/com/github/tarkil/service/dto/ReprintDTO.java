package com.github.tarkil.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Reprint entity.
 */
public class ReprintDTO implements Serializable {

    private Long id;

    @Size(max = 250)
    private String image;

    @Size(max = 50)
    private String artist;

    private Integer number;

    private Long cardId;

    private Long editionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getEditionId() {
        return editionId;
    }

    public void setEditionId(Long editionId) {
        this.editionId = editionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReprintDTO reprintDTO = (ReprintDTO) o;

        if ( ! Objects.equals(id, reprintDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReprintDTO{" +
            "id=" + id +
            ", image='" + image + "'" +
            ", artist='" + artist + "'" +
            ", number='" + number + "'" +
            '}';
    }
}
