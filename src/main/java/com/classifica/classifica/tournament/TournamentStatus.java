
package com.classifica.classifica.tournament;

public enum TournamentStatus {
    DRAFT, REGISTRATION, IN_PROGRESS, COMPLETED;

    /** A tournament moves forward one step at a time. */
    public boolean canMoveTo(TournamentStatus target) {
        return target.ordinal() == this.ordinal() + 1;
    }

    /** Once a tournament has started, its format and scoring rules are locked. */
    public boolean isLocked() {
        return this == IN_PROGRESS || this == COMPLETED;
    }
}