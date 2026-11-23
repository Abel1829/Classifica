package com.classifica.classifica.team;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teams/{teamId}/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlayerResponse> list(@PathVariable Long teamId) {
        return service.findByTeam(teamId);
    }

    @GetMapping("/{playerId}")
    public PlayerResponse get(@PathVariable Long teamId, @PathVariable Long playerId) {
        return service.findById(teamId, playerId);
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> create(@PathVariable Long teamId,
                                                 @Valid @RequestBody PlayerRequest request) {
        PlayerResponse created = service.create(teamId, request);
        return ResponseEntity
                .created(URI.create("/api/teams/" + teamId + "/players/" + created.id()))
                .body(created);
    }

    @PutMapping("/{playerId}")
    public PlayerResponse update(@PathVariable Long teamId, @PathVariable Long playerId,
                                 @Valid @RequestBody PlayerRequest request) {
        return service.update(teamId, playerId, request);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> delete(@PathVariable Long teamId, @PathVariable Long playerId) {
        service.delete(teamId, playerId);
        return ResponseEntity.noContent().build();
    }
}
