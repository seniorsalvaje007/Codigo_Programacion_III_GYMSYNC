package com.gymsync.model;

public record Atleta(String id,
                     String nombre,
                     String correo,
                     NivelAtleta nivel,
                     String tipoMembresia,
                     boolean pagoActivo) {

    public Atleta {
        if (id == null || id.isBlank() || correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El ID del atleta no puede estar vacío");
        }
    }
}
