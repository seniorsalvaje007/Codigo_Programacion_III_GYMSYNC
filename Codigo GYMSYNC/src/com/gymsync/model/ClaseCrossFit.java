package com.gymsync.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClaseCrossFit {

    private final String id;
    private final String nombreWOD;
    private final LocalDateTime horario;
    private final int cupoMaximo;
    private String coachAsignado;
    private final List<Atleta> atletasInscritos;

    public ClaseCrossFit (String id, String nombreWOD, LocalDateTime horario, int cupoMaximo, String coachAsignado) {
        this.id = id;
        this.nombreWOD = nombreWOD;
        this.horario = horario;
        this.cupoMaximo = cupoMaximo;
        this.coachAsignado = coachAsignado;
        this.atletasInscritos = new ArrayList<>();
    }

    public String getId () {return this.id;}
    public String getNombreWOD () {return this.nombreWOD;}
    public LocalDateTime getHorario () {return this.horario;}
    public int getCupoMaximo () {return this.cupoMaximo;}
    public String getCoachAsignado () {return this.coachAsignado;}
    public List<Atleta> getAtletasInscritos () {return this.atletasInscritos;}

    public void setCoachAsignado (String coachAsignado) {
        this.coachAsignado = coachAsignado;
    }

    public boolean inscribirAtleta (Atleta atleta) {
        if (atletasInscritos.size() < cupoMaximo){
            atletasInscritos.add(atleta);
            return true;
        }
        return false;
    }
}
