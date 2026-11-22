package com.classifica.classifica.venue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.classifica.classifica.common.NotFoundException;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock
    VenueRepository repository;

    @InjectMocks
    VenueService service;

    @Test
    void findById_unknownId_throwsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_savesVenueAndReturnsIt() {
        when(repository.save(any(Venue.class))).thenAnswer(invocation -> {
            Venue venue = invocation.getArgument(0);
            ReflectionTestUtils.setField(venue, "id", 1L);
            return venue;
        });

        VenueResponse response = service.create(new VenueRequest("Central Stadium", "Via Roma 1", 20000));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Central Stadium");
        assertThat(response.capacity()).isEqualTo(20000);
    }

    @Test
    void delete_unknownId_throwsNotFoundAndDeletesNothing() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class);
        verify(repository, never()).deleteById(any());
    }
}