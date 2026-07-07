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

    public List<ClaseCrossFit> obtenerClasesPorDia (int dia) {
        List<ClaseCrossFit> clasesDelDia = new ArrayList<>();

        for (ClaseCrossFit clase : agendaOrdenada()) {
            clasesDelDia.add(clase);
        }

        return clasesDelDia;

    }

    private final java.util.Stack<ClaseCrossFit> historialCambios = new java.util.Stack<>();

    private final com.gymsync.model.NodoPlanificacion raizPlanificacion =
            new com.gymsync.model.NodoPlanificacion("Box Principal - Planificación General");

    public boolean programarClaseDinamica(ClaseCrossFit nuevaClase) {
        if (this.agendaSemanal.containsKey(nuevaClase.getHorario())) {
            System.out.println("Conflicto de horario ya existe una clase programada en ese bloque de horario");
            return false;
        }

        this.agendaSemanal.put(nuevaClase.getHorario(), nuevaClase);
        this.historialCambios.push(nuevaClase);

        com.gymsync.model.NodoPlanificacion nuevoWOD =
                new com.gymsync.model.NodoPlanificacion(nuevaClase.getNombreWOD());
        this.raizPlanificacion.agregarHijo(nuevoWOD);

        return true;
    }

    public void deshacerUltimaClase() {
        if (!historialCambios.isEmpty()) {
            ClaseCrossFit eliminada = historialCambios.pop();
            this.agendaSemanal.remove(eliminada.getHorario());
            System.out.println("LOG: Acción revertida mediante Stack. Se eliminó de la agenda: " + eliminada.getNombreWOD());
        } else {
            System.out.println("No hay modificaciones operativas que revertir en el Stack.");
        }
    }

    public com.gymsync.model.NodoPlanificacion getRaizPlanificacion() {
        return this.raizPlanificacion;
    }

}
