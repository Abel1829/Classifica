package com.classifica.classifica.tournament;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService service;

    public TournamentController(TournamentService service) {
        this.service = service;
    }

    @GetMapping
    public List<TournamentResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TournamentResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> create(@Valid @RequestBody TournamentRequest request) {
        TournamentResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/api/tournaments/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public TournamentResponse update(@PathVariable Long id, @Valid @RequestBody TournamentRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public TournamentResponse changeStatus(@PathVariable Long id,
                                           @Valid @RequestBody TournamentStatusRequest request) {
        return service.changeStatus(id, request.status());
    }

    @PostMapping("/{id}/publish")
    public TournamentResponse publish(@PathVariable Long id) {
        return service.publish(id);
    }

    @PostMapping("/{id}/unpublish")
    public TournamentResponse unpublish(@PathVariable Long id) {
        return service.unpublish(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
