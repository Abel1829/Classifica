package com.classifica.classifica.team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByTeamIdOrderByShirtNumber(Long teamId);

    Optional<Player> findByIdAndTeamId(Long id, Long teamId);

    boolean existsByTeamIdAndShirtNumber(Long teamId, int shirtNumber);

    boolean existsByTeamIdAndShirtNumberAndIdNot(Long teamId, int shirtNumber, Long id);

    void deleteByTeamId(Long teamId);
}
