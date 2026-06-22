package com.gymsync.repository;

import com.gymsync.model.PerfilNutricional;
import java.util.HashMap;
import java.util.Optional;

public class NutricionRepository {

    private final HashMap<String, PerfilNutricional> historialNutricional;

    public NutricionRepository () {
        this.historialNutricional = new HashMap<>();
    }

    public void guardarPerfil (String atletaId, PerfilNutricional perfil) {
        historialNutricional.put(atletaId, perfil);
    }

    public Optional<PerfilNutricional> obtenerPerfil (String atletaId) {
        return Optional.ofNullable(historialNutricional.get(atletaId));
    }
}
