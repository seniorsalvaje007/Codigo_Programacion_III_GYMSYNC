package com.gymsync.repository;

import com.gymsync.model.Progreso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgresoRepository {

    private final HashMap<String, List<Progreso>> historialProgreso;

    public ProgresoRepository() {
        this.historialProgreso = new HashMap<>();
    }

    public void registrarProgreso(String idAtleta, Progreso nuevoProgreso) {
        historialProgreso.computeIfAbsent(idAtleta, k -> new ArrayList<>()).add(nuevoProgreso);
    }

    public List<Progreso> obtenerHistorial(String idAtleta) {
        return historialProgreso.getOrDefault(idAtleta, new ArrayList<>());
    }
}