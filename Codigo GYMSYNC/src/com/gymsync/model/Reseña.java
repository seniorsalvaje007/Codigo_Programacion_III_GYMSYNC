package com.gymsync.model;
import java.time.LocalDateTime;

public record Reseña(
        String idAtleta,
        String nombreAtleta,
        String nombreCoach,
        int estrellas,
        String comentario,
        LocalDateTime fecha
) {}