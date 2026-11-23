package com.classifica.classifica.tournament;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TournamentRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull TournamentFormat format,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @Min(0) int pointsForWin,
        @Min(0) int pointsForDraw,
        @Min(0) int pointsForLoss) {

    @AssertTrue(message = "endDate must not be before startDate")
    boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
