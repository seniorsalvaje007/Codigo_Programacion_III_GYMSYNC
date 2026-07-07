package com.gymsync.repository;

import java.util.*;

public class ProgresionTecnicaRepository {
    private final Map<String, List<String>> grafoMovimientos = new HashMap<>();

    public void registrarDependencia(String movimientoBase, String movimientoAvanzado) {
        grafoMovimientos.computeIfAbsent(movimientoBase, k -> new ArrayList<>()).add(movimientoAvanzado);
    }

    public List<String> obtenerRutaCritica(String inicio, String destino) {
        Queue<List<String>> colaCaminos = new LinkedList<>();
        Set<String> visitados = new HashSet<>();

        colaCaminos.add(Collections.singletonList(inicio));
        visitados.add(inicio);

        while (!colaCaminos.isEmpty()) {
            List<String> camino = colaCaminos.poll();
            String nodo = camino.get(camino.size() - 1);

            if (nodo.equalsIgnoreCase(destino)) return camino;

            List<String> dependientes = grafoMovimientos.getOrDefault(nodo, new ArrayList<>());
            for (String sig : dependientes) {
                if (!visitados.contains(sig)) {
                    visitados.add(sig);
                    List<String> nuevoCamino = new ArrayList<>(camino);
                    nuevoCamino.add(sig);
                    colaCaminos.add(nuevoCamino);
                }
            }
        }
        return Collections.emptyList();
    }

    public boolean verificarCiclos() {
        Set<String> visitados = new HashSet<>();
        Set<String> pilaRecorrido = new HashSet<>();

        for (String nodo : grafoMovimientos.keySet()) {
            if (detectarCicloDFS(nodo, visitados, pilaRecorrido)) return true;
        }
        return false;
    }

    private boolean detectarCicloDFS(String nodo, Set<String> visitados, Set<String> pilaRecorrido) {
        if (pilaRecorrido.contains(nodo)) return true;
        if (visitados.contains(nodo)) return false;

        pilaRecorrido.add(nodo);
        List<String> vecinos = grafoMovimientos.getOrDefault(nodo, new ArrayList<>());
        for (String vecino : vecinos) {
            if (detectarCicloDFS(vecino, visitados, pilaRecorrido)) return true;
        }

        pilaRecorrido.remove(nodo);
        visitados.add(nodo);
        return false;
    }
}