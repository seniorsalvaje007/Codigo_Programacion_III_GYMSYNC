package com.gymsync.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ClaseCrossFit {

    private final String id;
    private final String nombreWOD;
    private final LocalDateTime horario;
    private final int cupoMaximo;
    private String coachAsignado;
    private final List<Atleta> atletasInscritos;
    private final PriorityQueue<Atleta> listaEspera;
    private final List<Atleta> asistentes = new ArrayList<>();

    public ClaseCrossFit (String id, String nombreWOD, LocalDateTime horario, int cupoMaximo, String coachAsignado) {
        this.id = id;
        this.nombreWOD = nombreWOD;
        this.horario = horario;
        this.cupoMaximo = cupoMaximo;
        this.coachAsignado = coachAsignado;
        this.atletasInscritos = new ArrayList<>();
        this.listaEspera = new PriorityQueue<>((atleta1, atleta2) -> {
            int peso1 = atleta1.tipoMembresia().equalsIgnoreCase("ANUAL") ? 2 :
                    (atleta1.tipoMembresia().equalsIgnoreCase("TRIMESTRAL") ? 1 : 0);

            int peso2 = atleta2.tipoMembresia().equalsIgnoreCase("ANUAL") ? 2 :
                    (atleta2.tipoMembresia().equalsIgnoreCase("TRIMESTRAL") ? 1 : 0);

            return Integer.compare(peso2, peso1);
        });
    }

    public String getId () {return this.id;}
    public String getNombreWOD () {return this.nombreWOD;}
    public LocalDateTime getHorario () {return this.horario;}
    public int getCupoMaximo () {return this.cupoMaximo;}
    public String getCoachAsignado () {return this.coachAsignado;}
    public List<Atleta> getAtletasInscritos () {return this.atletasInscritos;}
    public PriorityQueue<Atleta> getListaEspera () {return this.listaEspera;}

    public void setCoachAsignado (String coachAsignado) {
        this.coachAsignado = coachAsignado;
    }

    public boolean inscribirAtleta (Atleta atleta) {
        if (atletasInscritos.contains(atleta)){
            System.out.println("El atleta ya se encuentra inscrito en esta clase");
            return false;
        }

        if (atletasInscritos.size() < cupoMaximo) {
            atletasInscritos.add(atleta);
            System.out.println("Inscripcion en la clase confirmada para el atleta: " + atleta.nombre());
            return true;
        } else {
            listaEspera.offer(atleta);
            System.out.println("Clase llena. El atleta " + atleta.nombre() + " a entrado en lista de espera");
            return false;
        }
    }

    public void registrarAsistencia (Atleta atleta) {
        if (atletasInscritos.contains(atleta)) {
            if (!asistentes.contains(atleta)) {
                asistentes.add(atleta);
                System.out.println("Asistencia registrada exitosamente para el atleta " + atleta.nombre());
            } else {
                System.out.println("El atleta actual ya tiene su asistencia");
            }
        } else {
            System.out.println("ERORR: El atleta no esta incrito en la clase");
        }
    }

    public List<Atleta> getAsistencia () {return this.asistentes;}
}
