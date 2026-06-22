package com.gymsync.model;

import java.time.LocalDate;

public record Atleta(String id,
                     String nombre,
                     String correo,
                     NivelAtleta nivel,
                     String tipoMembresia,
                     boolean pagoActivo,
                     LocalDate fechaVencimiento,
                     int puntos) {

    public Atleta {
        if (id == null || id.isBlank() || correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El ID del atleta no puede estar vacío");
        }
    }

    public Rango getRango () {
        if (puntos >= 100000) return Rango.GIGACHAD;
        if (puntos>=20000) return Rango.DIAMANTE;
        if (puntos>=10000) return Rango.PLATINO;
        if (puntos>=5000) return Rango.PLATA;
        if (puntos>=1000) return Rango.BRONCE;
        return Rango.NEUTRO;
    }
}
