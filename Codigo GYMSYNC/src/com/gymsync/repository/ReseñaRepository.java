package com.gymsync.repository;

import com.gymsync.model.Reseña;
import java.util.*;

public class ReseñaRepository {
    private final List<Reseña> baseReseñas = new ArrayList<>();

    public void guardarReseña (Reseña r) {
        baseReseñas.add(r);
    }

    public List<Reseña> obtenerReseñasPorCoach (String nombreCoach) {
        return baseReseñas.stream()
                .filter(r -> r.nombreCoach().equalsIgnoreCase(nombreCoach))
                .toList();
    }
}
