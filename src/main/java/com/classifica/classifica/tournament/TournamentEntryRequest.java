package com.classifica.classifica.tournament;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TournamentEntryRequest(@NotNull Long teamId, @Min(1) Integer seed) {
}
