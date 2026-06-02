package com.gymsync.repository;

import com.gymsync.model.Atleta;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AtletaRepositorio {

    private final HashMap<String, Atleta> listaDeAtletas;

    public AtletaRepositorio () {
        this.listaDeAtletas = new HashMap<>();
    }

    public void guardarAtleta (Atleta atleta) {
        this.listaDeAtletas.put(atleta.id(), atleta);
    }

    public Optional<Atleta> buscarPorId (String id) {
        return Optional.ofNullable(listaDeAtletas.get(id));
    }

    public int obtenerTotalDeAtletas () {
        return this.listaDeAtletas.size();
    }
}
