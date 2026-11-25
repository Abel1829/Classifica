package com.classifica.classifica.tournament;

public record TournamentEntryResponse(Long id, Long tournamentId, Long teamId,
                                      String teamName, String teamShortName, Integer seed) {

    static TournamentEntryResponse from(TournamentEntry entry) {
        return new TournamentEntryResponse(entry.getId(), entry.getTournament().getId(),
                entry.getTeam().getId(), entry.getTeam().getName(),
                entry.getTeam().getShortName(), entry.getSeed());
    }
}
