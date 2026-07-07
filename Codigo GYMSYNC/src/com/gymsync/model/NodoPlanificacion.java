package com.gymsync.model;

import java.util.ArrayList;
import java.util.List;

public class NodoPlanificacion {
    private final String nombreElemento;
    private final List<NodoPlanificacion> hijos;

    public NodoPlanificacion(String nombreElemento) {
        this.nombreElemento = nombreElemento;
        this.hijos = new ArrayList<>();
    }

    public String getNombreElemento() { return nombreElemento; }
    public List<NodoPlanificacion> getHijos() { return hijos; }

    public void agregarHijo(NodoPlanificacion hijo) {
        this.hijos.add(hijo);
    }

    // Algoritmo de Búsqueda Jerárquica - Caso promedio O(log n)
    public boolean buscarEnPlan(String objetivo) {
        if (this.nombreElemento.equalsIgnoreCase(objetivo)) return true;
        for (NodoPlanificacion hijo : hijos) {
            if (hijo.buscarEnPlan(objetivo)) return true;
        }
        return false;
    }
}