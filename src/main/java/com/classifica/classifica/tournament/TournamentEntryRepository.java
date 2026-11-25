package com.classifica.classifica.tournament;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TournamentEntryRepository extends JpaRepository<TournamentEntry, Long> {

    List<TournamentEntry> findByTournamentIdOrderBySeedAscIdAsc(Long tournamentId);

    Optional<TournamentEntry> findByIdAndTournamentId(Long id, Long tournamentId);

    boolean existsByTournamentIdAndTeamId(Long tournamentId, Long teamId);

    /** True if the team is entered in a different tournament that has not finished yet. */
    @Query("""
            select count(e) > 0 from TournamentEntry e
            where e.team.id = :teamId
              and e.tournament.id <> :tournamentId
              and e.tournament.status <> com.classifica.classifica.tournament.TournamentStatus.COMPLETED
            """)
    boolean existsActiveEntryInAnotherTournament(@Param("teamId") Long teamId,
                                                 @Param("tournamentId") Long tournamentId);
}