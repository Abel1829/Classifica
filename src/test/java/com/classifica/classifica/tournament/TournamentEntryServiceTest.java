package com.classifica.classifica.tournament;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.classifica.classifica.common.ConflictException;
import com.classifica.classifica.team.Team;
import com.classifica.classifica.team.TeamService;

@ExtendWith(MockitoExtension.class)
class TournamentEntryServiceTest {

    @Mock
    TournamentEntryRepository entries;

    @Mock
    TournamentRepository tournaments;

    @Mock
    TeamService teams;

    @InjectMocks
    TournamentEntryService service;

    @Test
    void add_toStartedTournament_isRejected() {
        Tournament started = tournament();
        started.changeStatus(TournamentStatus.REGISTRATION);
        started.changeStatus(TournamentStatus.IN_PROGRESS);
        when(tournaments.findById(1L)).thenReturn(Optional.of(started));

        assertThatThrownBy(() -> service.add(1L, new TournamentEntryRequest(5L, null)))
                .isInstanceOf(ConflictException.class);
        verify(entries, never()).save(any());
    }

    @Test
    void add_sameTeamTwice_isRejected() {
        when(tournaments.findById(1L)).thenReturn(Optional.of(tournament()));
        when(teams.requireTeam(5L)).thenReturn(team(5L));
        when(entries.existsByTournamentIdAndTeamId(1L, 5L)).thenReturn(true);

        assertThatThrownBy(() -> service.add(1L, new TournamentEntryRequest(5L, null)))
                .isInstanceOf(ConflictException.class);
        verify(entries, never()).save(any());
    }

    @Test
    void add_validTeam_savesTheEntry() {
        Tournament tournament = tournament();
        ReflectionTestUtils.setField(tournament, "id", 1L);
        when(tournaments.findById(1L)).thenReturn(Optional.of(tournament));
        when(teams.requireTeam(5L)).thenReturn(team(5L));
        when(entries.existsByTournamentIdAndTeamId(1L, 5L)).thenReturn(false);
        when(entries.save(any(TournamentEntry.class))).thenAnswer(invocation -> {
            TournamentEntry entry = invocation.getArgument(0);
            ReflectionTestUtils.setField(entry, "id", 7L);
            return entry;
        });

        TournamentEntryResponse response = service.add(1L, new TournamentEntryRequest(5L, 1));

        assertThat(response.id()).isEqualTo(7L);
        assertThat(response.teamId()).isEqualTo(5L);
        assertThat(response.seed()).isEqualTo(1);
    }

    @Test
    void remove_afterTournamentStarted_isRejected() {
        Tournament started = tournament();
        started.changeStatus(TournamentStatus.REGISTRATION);
        started.changeStatus(TournamentStatus.IN_PROGRESS);
        when(tournaments.findById(1L)).thenReturn(Optional.of(started));

        assertThatThrownBy(() -> service.remove(1L, 7L))
                .isInstanceOf(ConflictException.class);
        verify(entries, never()).delete(any());
    }

    @Test
    void add_teamInAnotherUnfinishedTournament_isRejected() {
        when(tournaments.findById(1L)).thenReturn(Optional.of(tournament()));
        when(teams.requireTeam(5L)).thenReturn(team(5L));
        when(entries.existsByTournamentIdAndTeamId(1L, 5L)).thenReturn(false);
        when(entries.existsActiveEntryInAnotherTournament(5L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.add(1L, new TournamentEntryRequest(5L, null)))
                .isInstanceOf(ConflictException.class);
        verify(entries, never()).save(any());
    }

    private Tournament tournament() {
        return new Tournament("City Cup", TournamentFormat.LEAGUE,
                LocalDate.of(2027, 1, 10), LocalDate.of(2027, 3, 10), 3, 1, 0);
    }

    private Team team(Long id) {
        Team team = new Team("Rovers FC", "ROV", null, null);
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }
}
