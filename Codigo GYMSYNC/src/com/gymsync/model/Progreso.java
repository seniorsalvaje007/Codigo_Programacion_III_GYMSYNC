package com.gymsync.model;

import java.time.LocalDate;

public record Progreso(String nombre,
                       TipoEjercicio tipo,
                       double peso,
                       LocalDate fecha) {
}
