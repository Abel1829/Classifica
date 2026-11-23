package com.classifica.classifica.team;

import java.time.LocalDate;

public record PlayerResponse(Long id, Long teamId, String firstName, String lastName,
                             int shirtNumber, Position position, LocalDate dateOfBirth) {

    static PlayerResponse from(Player player) {
        return new PlayerResponse(player.getId(), player.getTeam().getId(), player.getFirstName(),
                player.getLastName(), player.getShirtNumber(), player.getPosition(),
                player.getDateOfBirth());
    }
}
