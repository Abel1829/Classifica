package com.classifica.classifica.tournament;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/teams")
public class TournamentEntryController {

    private final TournamentEntryService service;

    public TournamentEntryController(TournamentEntryService service) {
        this.service = service;
    }

    @GetMapping
    public List<TournamentEntryResponse> list(@PathVariable Long tournamentId) {
        return service.findByTournament(tournamentId);
    }

    @PostMapping
    public ResponseEntity<TournamentEntryResponse> add(@PathVariable Long tournamentId,
                                                       @Valid @RequestBody TournamentEntryRequest request) {
        TournamentEntryResponse created = service.add(tournamentId, request);
        return ResponseEntity
                .created(URI.create("/api/tournaments/" + tournamentId + "/teams/" + created.id()))
                .body(created);
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> remove(@PathVariable Long tournamentId, @PathVariable Long entryId) {
        service.remove(tournamentId, entryId);
        return ResponseEntity.noContent().build();
    }
}
