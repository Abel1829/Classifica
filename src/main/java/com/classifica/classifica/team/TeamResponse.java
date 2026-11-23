package com.classifica.classifica.team;

public record TeamResponse(Long id, String name, String shortName, String logoUrl, String colors) {

    static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(), team.getName(), team.getShortName(),
                team.getLogoUrl(), team.getColors());
    }
}