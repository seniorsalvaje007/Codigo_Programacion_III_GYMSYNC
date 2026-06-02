package com.gymsync.model;

public record Atleta(String id,
                     String nombre,
                     NivelAtleta nivel,
                     String tipoMembresia,
                     boolean pagoActivo) {

    public Atleta {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del atleta no puede estar vacío");
        }
    }
}
