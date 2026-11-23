package com.classifica.classifica.team;

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
import com.classifica.classifica.common.NotFoundException;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    PlayerRepository players;

    @Mock
    TeamRepository teams;

    @InjectMocks
    PlayerService service;

    @Test
    void create_unknownTeam_throwsNotFound() {
        when(teams.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(99L, request(9)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_duplicateShirtNumber_throwsConflictAndSavesNothing() {
        when(teams.findById(1L)).thenReturn(Optional.of(team(1L)));
        when(players.existsByTeamIdAndShirtNumber(1L, 9)).thenReturn(true);

        assertThatThrownBy(() -> service.create(1L, request(9)))
                .isInstanceOf(ConflictException.class);
        verify(players, never()).save(any());
    }

    @Test
    void create_validPlayer_savesAndReturnsIt() {
        when(teams.findById(1L)).thenReturn(Optional.of(team(1L)));
        when(players.existsByTeamIdAndShirtNumber(1L, 9)).thenReturn(false);
        when(players.save(any(Player.class))).thenAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            ReflectionTestUtils.setField(player, "id", 5L);
            return player;
        });

        PlayerResponse response = service.create(1L, request(9));

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.teamId()).isEqualTo(1L);
        assertThat(response.shirtNumber()).isEqualTo(9);
    }

    private Team team(Long id) {
        Team team = new Team("Rovers FC", "ROV", null, null);
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }

    private PlayerRequest request(int shirtNumber) {
        return new PlayerRequest("Marco", "Rossi", shirtNumber, Position.FORWARD,
                LocalDate.of(2000, 5, 1));
    }
}
