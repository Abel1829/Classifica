package com.classifica.classifica.tournament;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classifica.classifica.common.ConflictException;
import com.classifica.classifica.common.NotFoundException;

@Service
@Transactional(readOnly = true)
public class TournamentService {

    private final TournamentRepository repository;

    public TournamentService(TournamentRepository repository) {
        this.repository = repository;
    }

    public List<TournamentResponse> findAll() {
        return repository.findAll().stream().map(TournamentResponse::from).toList();
    }

    public TournamentResponse findById(Long id) {
        return TournamentResponse.from(getOrThrow(id));
    }

    @Transactional
    public TournamentResponse create(TournamentRequest request) {
        Tournament tournament = new Tournament(request.name(), request.format(), request.startDate(),
                request.endDate(), request.pointsForWin(), request.pointsForDraw(), request.pointsForLoss());
        return TournamentResponse.from(repository.save(tournament));
    }

    @Transactional
    public TournamentResponse update(Long id, TournamentRequest request) {
        Tournament tournament = getOrThrow(id);
        tournament.update(request.name(), request.format(), request.startDate(), request.endDate(),
                request.pointsForWin(), request.pointsForDraw(), request.pointsForLoss());
        return TournamentResponse.from(tournament);
    }

    @Transactional
    public TournamentResponse changeStatus(Long id, TournamentStatus target) {
        Tournament tournament = getOrThrow(id);
        tournament.changeStatus(target);
        return TournamentResponse.from(tournament);
    }

    @Transactional
    public TournamentResponse publish(Long id) {
        Tournament tournament = getOrThrow(id);
        tournament.publish();
        return TournamentResponse.from(tournament);
    }

    @Transactional
    public TournamentResponse unpublish(Long id) {
        Tournament tournament = getOrThrow(id);
        tournament.unpublish();
        return TournamentResponse.from(tournament);
    }

    @Transactional
    public void delete(Long id) {
        Tournament tournament = getOrThrow(id);
        if (!tournament.isDraft()) {
            throw new ConflictException("Only a draft tournament can be deleted");
        }
        repository.delete(tournament);
    }

    private Tournament getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tournament " + id + " not found"));
    }
}
