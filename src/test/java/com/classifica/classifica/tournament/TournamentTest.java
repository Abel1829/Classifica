package com.classifica.classifica.tournament;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.classifica.classifica.common.ConflictException;

class TournamentTest {

    private Tournament newLeague() {
        return new Tournament("City Cup", TournamentFormat.LEAGUE,
                LocalDate.of(2027, 1, 10), LocalDate.of(2027, 3, 10), 3, 1, 0);
    }

    private Tournament startedLeague() {
        Tournament tournament = newLeague();
        tournament.changeStatus(TournamentStatus.REGISTRATION);
        tournament.changeStatus(TournamentStatus.IN_PROGRESS);
        return tournament;
    }

    @Test
    void newTournament_isDraftAndNotPublished() {
        Tournament tournament = newLeague();

        assertThat(tournament.getStatus()).isEqualTo(TournamentStatus.DRAFT);
        assertThat(tournament.isPublished()).isFalse();
    }

    @Test
    void changeStatus_movesForwardOneStepAtATime() {
        Tournament tournament = newLeague();

        tournament.changeStatus(TournamentStatus.REGISTRATION);
        tournament.changeStatus(TournamentStatus.IN_PROGRESS);
        tournament.changeStatus(TournamentStatus.COMPLETED);

        assertThat(tournament.getStatus()).isEqualTo(TournamentStatus.COMPLETED);
    }

    @Test
    void changeStatus_skippingAStep_isRejected() {
        Tournament tournament = newLeague();

        assertThatThrownBy(() -> tournament.changeStatus(TournamentStatus.IN_PROGRESS))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void changeStatus_goingBackwards_isRejected() {
        Tournament tournament = startedLeague();

        assertThatThrownBy(() -> tournament.changeStatus(TournamentStatus.REGISTRATION))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void publish_draftTournament_isRejected() {
        Tournament tournament = newLeague();

        assertThatThrownBy(tournament::publish).isInstanceOf(ConflictException.class);
    }

    @Test
    void publish_afterRegistrationOpens_works() {
        Tournament tournament = newLeague();
        tournament.changeStatus(TournamentStatus.REGISTRATION);

        tournament.publish();

        assertThat(tournament.isPublished()).isTrue();
    }

    @Test
    void update_formatAfterStart_isRejected() {
        Tournament tournament = startedLeague();

        assertThatThrownBy(() -> tournament.update("City Cup", TournamentFormat.KNOCKOUT,
                LocalDate.of(2027, 1, 10), LocalDate.of(2027, 3, 10), 3, 1, 0))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void update_nameAfterStart_isAllowed() {
        Tournament tournament = startedLeague();

        assertThatCode(() -> tournament.update("City Cup 2027", TournamentFormat.LEAGUE,
                LocalDate.of(2027, 1, 10), LocalDate.of(2027, 3, 10), 3, 1, 0))
                .doesNotThrowAnyException();
        assertThat(tournament.getName()).isEqualTo("City Cup 2027");
    }

    @Test
    void update_formatWhileDraft_isAllowed() {
        Tournament tournament = newLeague();

        tournament.update("City Cup", TournamentFormat.KNOCKOUT,
                LocalDate.of(2027, 1, 10), LocalDate.of(2027, 3, 10), 3, 1, 0);

        assertThat(tournament.getFormat()).isEqualTo(TournamentFormat.KNOCKOUT);
    }
}
