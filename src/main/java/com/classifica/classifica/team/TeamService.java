package com.classifica.classifica.team;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classifica.classifica.common.NotFoundException;

@Service
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teams;
    private final PlayerRepository players;

    public TeamService(TeamRepository teams, PlayerRepository players) {
        this.teams = teams;
        this.players = players;
    }

    public List<TeamResponse> findAll() {
        return teams.findAll().stream().map(TeamResponse::from).toList();
    }

    public TeamResponse findById(Long id) {
        return TeamResponse.from(getOrThrow(id));
    }

    @Transactional
    public TeamResponse create(TeamRequest request) {
        Team team = new Team(request.name(), request.shortName(), request.logoUrl(), request.colors());
        return TeamResponse.from(teams.save(team));
    }

    @Transactional
    public TeamResponse update(Long id, TeamRequest request) {
        Team team = getOrThrow(id);
        team.update(request.name(), request.shortName(), request.logoUrl(), request.colors());
        return TeamResponse.from(team);
    }

    @Transactional
    public void delete(Long id) {
        if (!teams.existsById(id)) {
            throw new NotFoundException("Team " + id + " not found");
        }
        players.deleteByTeamId(id);
        teams.deleteById(id);
    }

    private Team getOrThrow(Long id) {
        return teams.findById(id)
                .orElseThrow(() -> new NotFoundException("Team " + id + " not found"));
    }
}
