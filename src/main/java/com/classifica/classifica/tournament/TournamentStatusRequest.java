package com.classifica.classifica.tournament;

import jakarta.validation.constraints.NotNull;

public record TournamentStatusRequest(@NotNull TournamentStatus status) {
}
