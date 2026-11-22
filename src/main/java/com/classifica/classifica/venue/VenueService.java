package com.classifica.classifica.venue;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classifica.classifica.common.NotFoundException;

@Service
@Transactional(readOnly = true)
public class VenueService {

    private final VenueRepository repository;

    public VenueService(VenueRepository repository) {
        this.repository = repository;
    }

    public List<VenueResponse> findAll() {
        return repository.findAll().stream().map(VenueResponse::from).toList();
    }

    public VenueResponse findById(Long id) {
        return VenueResponse.from(getOrThrow(id));
    }

    @Transactional
    public VenueResponse create(VenueRequest request) {
        Venue venue = new Venue(request.name(), request.address(), request.capacity());
        return VenueResponse.from(repository.save(venue));
    }

    @Transactional
    public VenueResponse update(Long id, VenueRequest request) {
        Venue venue = getOrThrow(id);
        venue.update(request.name(), request.address(), request.capacity());
        return VenueResponse.from(venue);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Venue " + id + " not found");
        }
        repository.deleteById(id);
    }

    private Venue getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venue " + id + " not found"));
    }
}
