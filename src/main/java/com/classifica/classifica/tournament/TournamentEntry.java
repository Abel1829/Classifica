package com.classifica.classifica.tournament;

import com.classifica.classifica.team.Team;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tournament_team",
        uniqueConstraints = @UniqueConstraint(columnNames = { "tournament_id", "team_id" }))
public class TournamentEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    private Integer seed;

    protected TournamentEntry() {
        // required by JPA
    }

    public TournamentEntry(Tournament tournament, Team team, Integer seed) {
        this.tournament = tournament;
        this.team = team;
        this.seed = seed;
    }

    public Long getId() { return id; }
    public Tournament getTournament() { return tournament; }
    public Team getTeam() { return team; }
    public Integer getSeed() { return seed; }
}