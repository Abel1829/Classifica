package com.classifica.classifica.team;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "player",
        uniqueConstraints = @UniqueConstraint(columnNames = { "team_id", "shirt_number" }))
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "first_name", nullable = false, length = 60)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 60)
    private String lastName;

    @Column(name = "shirt_number", nullable = false)
    private int shirtNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Position position;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    protected Player() {
        // required by JPA
    }

    public Player(Team team, String firstName, String lastName, int shirtNumber,
                  Position position, LocalDate dateOfBirth) {
        this.team = team;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shirtNumber = shirtNumber;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
    }

    public void update(String firstName, String lastName, int shirtNumber,
                       Position position, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shirtNumber = shirtNumber;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() { return id; }
    public Team getTeam() { return team; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getShirtNumber() { return shirtNumber; }
    public Position getPosition() { return position; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
}
