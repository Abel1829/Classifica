package com.classifica.classifica.team;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classifica.classifica.common.ConflictException;
import com.classifica.classifica.common.NotFoundException;

@Service
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository players;
    private final TeamRepository teams;

    public PlayerService(PlayerRepository players, TeamRepository teams) {
        this.players = players;
        this.teams = teams;
    }

    public List<PlayerResponse> findByTeam(Long teamId) {
        requireTeam(teamId);
        return players.findByTeamIdOrderByShirtNumber(teamId).stream()
                .map(PlayerResponse::from).toList();
    }

    public PlayerResponse findById(Long teamId, Long playerId) {
        return PlayerResponse.from(getOrThrow(teamId, playerId));
    }

    @Transactional
    public PlayerResponse create(Long teamId, PlayerRequest request) {
        Team team = requireTeam(teamId);
        if (players.existsByTeamIdAndShirtNumber(teamId, request.shirtNumber())) {
            throw shirtNumberTaken(request.shirtNumber());
        }
        Player player = new Player(team, request.firstName(), request.lastName(),
                request.shirtNumber(), request.position(), request.dateOfBirth());
        return PlayerResponse.from(players.save(player));
    }

    @Transactional
    public PlayerResponse update(Long teamId, Long playerId, PlayerRequest request) {
        Player player = getOrThrow(teamId, playerId);
        if (players.existsByTeamIdAndShirtNumberAndIdNot(teamId, request.shirtNumber(), playerId)) {
            throw shirtNumberTaken(request.shirtNumber());
        }
        player.update(request.firstName(), request.lastName(), request.shirtNumber(),
                request.position(), request.dateOfBirth());
        return PlayerResponse.from(player);
    }

    @Transactional
    public void delete(Long teamId, Long playerId) {
        players.delete(getOrThrow(teamId, playerId));
    }

    private Team requireTeam(Long teamId) {
        return teams.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team " + teamId + " not found"));
    }

    private Player getOrThrow(Long teamId, Long playerId) {
        return players.findByIdAndTeamId(playerId, teamId)
                .orElseThrow(() -> new NotFoundException(
                        "Player " + playerId + " not found in team " + teamId));
    }

    private ConflictException shirtNumberTaken(int shirtNumber) {
        return new ConflictException("Shirt number " + shirtNumber + " is already used in this team");
    }
}
