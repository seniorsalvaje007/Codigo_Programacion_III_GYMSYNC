package com.gymsync.repository;

import com.gymsync.model.ClaseCrossFit;
import java.time.LocalDateTime;
import java.util.*;

public class ClaseRepository {

    private final TreeMap<LocalDateTime, ClaseCrossFit> agendaSemanal;

    public ClaseRepository () {
        this.agendaSemanal = new TreeMap<>();
    }

    public boolean programarClase (ClaseCrossFit nuevaClase) {
        if (this.agendaSemanal.containsKey(nuevaClase.getHorario())) {
            System.out.println("Conflicto de horario ya existe una clase programada en ese bloque de horario");
            return false;
        }

        this.agendaSemanal.put(nuevaClase.getHorario(), nuevaClase);
        return true;
    }

    public List<String> obtenerListaCompleta () {
        return agendaOrdenada().stream()
                .map(ClaseCrossFit::getCoachAsignado)
                .distinct()
                .toList();
    }

    public Collection<ClaseCrossFit> agendaOrdenada () {
        return this.agendaSemanal.values();
    }

}
