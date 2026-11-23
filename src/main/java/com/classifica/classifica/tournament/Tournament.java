package com.classifica.classifica.tournament;

import java.time.LocalDate;

import com.classifica.classifica.common.ConflictException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TournamentFormat format;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TournamentStatus status = TournamentStatus.DRAFT;

    @Column(nullable = false)
    private boolean published = false;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "points_for_win", nullable = false)
    private int pointsForWin;

    @Column(name = "points_for_draw", nullable = false)
    private int pointsForDraw;

    @Column(name = "points_for_loss", nullable = false)
    private int pointsForLoss;

    protected Tournament() {
        // required by JPA
    }

    public Tournament(String name, TournamentFormat format, LocalDate startDate, LocalDate endDate,
                      int pointsForWin, int pointsForDraw, int pointsForLoss) {
        this.name = name;
        this.format = format;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pointsForWin = pointsForWin;
        this.pointsForDraw = pointsForDraw;
        this.pointsForLoss = pointsForLoss;
    }

    public void update(String name, TournamentFormat format, LocalDate startDate, LocalDate endDate,
                       int pointsForWin, int pointsForDraw, int pointsForLoss) {
        boolean rulesChanged = format != this.format
                || pointsForWin != this.pointsForWin
                || pointsForDraw != this.pointsForDraw
                || pointsForLoss != this.pointsForLoss;
        if (rulesChanged && status.isLocked()) {
            throw new ConflictException("Format and points cannot change once the tournament has started");
        }
        this.name = name;
        this.format = format;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pointsForWin = pointsForWin;
        this.pointsForDraw = pointsForDraw;
        this.pointsForLoss = pointsForLoss;
    }

    public void changeStatus(TournamentStatus target) {
        if (!status.canMoveTo(target)) {
            throw new ConflictException("A tournament cannot move from " + status + " to " + target);
        }
        this.status = target;
    }

    public void publish() {
        if (status == TournamentStatus.DRAFT) {
            throw new ConflictException("A draft tournament cannot be published");
        }
        this.published = true;
    }

    public void unpublish() {
        this.published = false;
    }

    public boolean isDraft() {
        return status == TournamentStatus.DRAFT;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public TournamentFormat getFormat() { return format; }
    public TournamentStatus getStatus() { return status; }
    public boolean isPublished() { return published; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getPointsForWin() { return pointsForWin; }
    public int getPointsForDraw() { return pointsForDraw; }
    public int getPointsForLoss() { return pointsForLoss; }
}
