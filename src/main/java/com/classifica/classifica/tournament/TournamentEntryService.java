package com.classifica.classifica.tournament;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classifica.classifica.common.ConflictException;
import com.classifica.classifica.common.NotFoundException;
import com.classifica.classifica.team.Team;
import com.classifica.classifica.team.TeamService;

@Service
@Transactional(readOnly = true)
public class TournamentEntryService {

    private final TournamentEntryRepository entries;
    private final TournamentRepository tournaments;
    private final TeamService teams;

    public TournamentEntryService(TournamentEntryRepository entries, TournamentRepository tournaments,
                                  TeamService teams) {
        this.entries = entries;
        this.tournaments = tournaments;
        this.teams = teams;
    }

    public List<TournamentEntryResponse> findByTournament(Long tournamentId) {
        requireTournament(tournamentId);
        return entries.findByTournamentIdOrderBySeedAscIdAsc(tournamentId).stream()
                .map(TournamentEntryResponse::from).toList();
    }

    @Transactional
    public TournamentEntryResponse add(Long tournamentId, TournamentEntryRequest request) {
        Tournament tournament = requireTournament(tournamentId);
        if (tournament.getStatus().isLocked()) {
            throw new ConflictException("Teams can only be entered while the tournament is in draft or registration");
        }
        Team team = teams.requireTeam(request.teamId());
        if (entries.existsByTournamentIdAndTeamId(tournamentId, team.getId())) {
            throw new ConflictException("Team " + team.getId() + " is already entered in this tournament");
        }
        if (entries.existsActiveEntryInAnotherTournament(team.getId(), tournamentId)) {
            throw new ConflictException("Team " + team.getId()
                    + " is still in another tournament that has not finished");
        }
        TournamentEntry entry = new TournamentEntry(tournament, team, request.seed());
        return TournamentEntryResponse.from(entries.save(entry));
    }

    @Transactional
    public void remove(Long tournamentId, Long entryId) {
        Tournament tournament = requireTournament(tournamentId);
        if (tournament.getStatus().isLocked()) {
            throw new ConflictException("Teams cannot be removed once the tournament has started");
        }
        TournamentEntry entry = entries.findByIdAndTournamentId(entryId, tournamentId)
                .orElseThrow(() -> new NotFoundException(
                        "Entry " + entryId + " not found in tournament " + tournamentId));
        entries.delete(entry);
    }

    private Tournament requireTournament(Long tournamentId) {
        return tournaments.findById(tournamentId)
                .orElseThrow(() -> new NotFoundException("Tournament " + tournamentId + " not found"));
    }
}
