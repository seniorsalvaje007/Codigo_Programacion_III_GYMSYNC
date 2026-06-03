package com.gymsync.repository;

import com.gymsync.model.Atleta;
import com.gymsync.model.NivelAtleta;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AtletaRepositorio {

    private final HashMap<String, Atleta> atletasPorId;
    private final HashMap<String, Atleta> atletasPorCorreo;
    private int contadorAtletas;

    public AtletaRepositorio () {
        this.atletasPorId = new HashMap<>();
        this.atletasPorCorreo = new HashMap<>();
    }

    public Atleta registrarNuevoAtleta (String nombre, String correo, NivelAtleta nivel, String tipoMembresia, boolean pagoActivo) {
        contadorAtletas ++;
        String idGenerado = String.format("GS-%03d", contadorAtletas);

        Atleta nuevoAtleta = new Atleta (idGenerado, nombre, correo, nivel, tipoMembresia, pagoActivo);

        atletasPorId.put(nuevoAtleta.id(), nuevoAtleta);
        atletasPorCorreo.put(nuevoAtleta.correo(), nuevoAtleta);

        return nuevoAtleta;

    }

    public Optional<Atleta> buscarPorId (String id) {return Optional.ofNullable(atletasPorId.get(id));}
    public Optional<Atleta> buscarPorCorreo (String correo) {return Optional.ofNullable(atletasPorCorreo.get(correo));}
    public int obtenerTotalAtletas () {return atletasPorId.size();}
}
