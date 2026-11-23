package com.classifica.classifica.tournament;

import java.time.LocalDate;

public record TournamentResponse(Long id, String name, TournamentFormat format, TournamentStatus status,
                                 boolean published, LocalDate startDate, LocalDate endDate,
                                 int pointsForWin, int pointsForDraw, int pointsForLoss) {

    static TournamentResponse from(Tournament t) {
        return new TournamentResponse(t.getId(), t.getName(), t.getFormat(), t.getStatus(),
                t.isPublished(), t.getStartDate(), t.getEndDate(),
                t.getPointsForWin(), t.getPointsForDraw(), t.getPointsForLoss());
    }
}
