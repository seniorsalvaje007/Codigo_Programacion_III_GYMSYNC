package com.gymsync.repository;

import com.gymsync.model.Atleta;
import com.gymsync.model.NivelAtleta;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

public class AtletaRepositorio {

    private final HashMap<String, Atleta> atletasPorId;
    private final HashMap<String, Atleta> atletasPorCorreo;
    private int contadorAtletas;

    public AtletaRepositorio () {
        this.atletasPorId = new HashMap<>();
        this.atletasPorCorreo = new HashMap<>();
    }

    public Atleta registrarNuevoAtleta (String nombre, String correo, NivelAtleta nivel, String tipoMembresia, boolean pagoActivo, LocalDate fechaVencimiento) {
        contadorAtletas ++;
        String idGenerado = String.format("GS-%03d", contadorAtletas);

        Atleta nuevoAtleta = new Atleta (idGenerado, nombre, correo, nivel, tipoMembresia, pagoActivo, fechaVencimiento, 0);

        atletasPorId.put(nuevoAtleta.id(), nuevoAtleta);
        atletasPorCorreo.put(nuevoAtleta.correo(), nuevoAtleta);

        return nuevoAtleta;

    }

    public void actualizarAtleta (Atleta atletaActualizado) {
        atletasPorId.put(atletaActualizado.id(), atletaActualizado);
        atletasPorCorreo.put(atletaActualizado.correo(), atletaActualizado);
    }

    public void sumarPuntos (String id, int puntosExtra) {
        buscarPorId(id).ifPresent(atleta -> {
            Atleta atletaActualizado = new Atleta(
                    atleta.id(), atleta.nombre(), atleta.correo(), atleta.nivel(),
                    atleta.tipoMembresia(), atleta.pagoActivo(), atleta.fechaVencimiento(),
                    atleta.puntos() + puntosExtra
            );
            actualizarAtleta(atletaActualizado);
        });
    }

    public Optional<Atleta> buscarPorId (String id) {return Optional.ofNullable(atletasPorId.get(id));}
    public Optional<Atleta> buscarPorCorreo (String correo) {return Optional.ofNullable(atletasPorCorreo.get(correo));}
    public int obtenerTotalAtletas () {return atletasPorId.size();}
}
