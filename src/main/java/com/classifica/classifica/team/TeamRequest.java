package com.classifica.classifica.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 10) String shortName,
        @Size(max = 500) String logoUrl,
        @Size(max = 50) String colors) {
}
