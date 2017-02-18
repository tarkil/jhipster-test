package com.github.tarkil.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Reprint.
 */
@Entity
@Table(name = "reprint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reprint")
public class Reprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 250)
    @Column(name = "image", length = 250)
    private String image;

    @Size(max = 50)
    @Column(name = "artist", length = 50)
    private String artist;

    @Column(name = "number")
    private Integer number;

    @ManyToOne
    private Card card;

    @OneToOne
    @JoinColumn(unique = true)
    private Edition edition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public Reprint image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtist() {
        return artist;
    }

    public Reprint artist(String artist) {
        this.artist = artist;
        return this;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getNumber() {
        return number;
    }

    public Reprint number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Card getCard() {
        return card;
    }

    public Reprint card(Card card) {
        this.card = card;
        return this;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Edition getEdition() {
        return edition;
    }

    public Reprint edition(Edition edition) {
        this.edition = edition;
        return this;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reprint reprint = (Reprint) o;
        if (reprint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reprint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Reprint{" +
            "id=" + id +
            ", image='" + image + "'" +
            ", artist='" + artist + "'" +
            ", number='" + number + "'" +
            '}';
    }
}
