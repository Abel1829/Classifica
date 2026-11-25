package com.classifica.classifica.tournament;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentEntryRepository extends JpaRepository<TournamentEntry, Long> {

    List<TournamentEntry> findByTournamentIdOrderBySeedAscIdAsc(Long tournamentId);

    Optional<TournamentEntry> findByIdAndTournamentId(Long id, Long tournamentId);

    boolean existsByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}