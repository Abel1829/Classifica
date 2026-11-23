package com.classifica.classifica.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "short_name", nullable = false, length = 10)
    private String shortName;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(length = 50)
    private String colors;

    protected Team() {
        // required by JPA
    }

    public Team(String name, String shortName, String logoUrl, String colors) {
        this.name = name;
        this.shortName = shortName;
        this.logoUrl = logoUrl;
        this.colors = colors;
    }

    public void update(String name, String shortName, String logoUrl, String colors) {
        this.name = name;
        this.shortName = shortName;
        this.logoUrl = logoUrl;
        this.colors = colors;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getShortName() { return shortName; }
    public String getLogoUrl() { return logoUrl; }
    public String getColors() { return colors; }
}
