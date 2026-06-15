package com.gymsync.repository;

import com.gymsync.model.Ejercicio;
import com.gymsync.model.TipoEjercicio;

import java.util.ArrayList;
import java.util.List;

public class EjercicioRepository {

    private final List<Ejercicio> catalogoEjercicios;

    public EjercicioRepository () {
        this.catalogoEjercicios = new ArrayList<>();
        cargarEjercicioMaestros();
    }

    public void cargarEjercicioMaestros () {
        catalogoEjercicios.add(new Ejercicio("Deadlift", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Back Squat", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Front Squat", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Overhead Squat", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Thruster", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Strict Press", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Push Press", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Push Jerk", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Clean and Jerk", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Power Clean", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Hang Clean", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Snatch", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Power Snatch", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Hang Snatch", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Kettlebell Swing", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Dumbbell Snatch", TipoEjercicio.PESAS));
        catalogoEjercicios.add(new Ejercicio("Medicine Ball Clean", TipoEjercicio.PESAS));

        catalogoEjercicios.add(new Ejercicio("Air Squat", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Push-up", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Handstand Push-up", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Pull-up", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Chest-to-Bar", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Bar Muscle-up", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Ring Muscle-up", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Toes-to-Bar", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Knees-to-Elbows", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Box Jump", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Burpee", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Burpee Box Jump Over", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Pistol Squat", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Rope Climb", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("Walking Lunge", TipoEjercicio.GIMNASIA));
        catalogoEjercicios.add(new Ejercicio("AbMat Sit-up", TipoEjercicio.GIMNASIA));

        catalogoEjercicios.add(new Ejercicio("Rowing", TipoEjercicio.CARDIO));
        catalogoEjercicios.add(new Ejercicio("Running", TipoEjercicio.CARDIO));
        catalogoEjercicios.add(new Ejercicio("Assault Bike", TipoEjercicio.CARDIO));
        catalogoEjercicios.add(new Ejercicio("SkiErg", TipoEjercicio.CARDIO));
        catalogoEjercicios.add(new Ejercicio("Single-Under", TipoEjercicio.CARDIO));
        catalogoEjercicios.add(new Ejercicio("Double-Under", TipoEjercicio.CARDIO));
        catalogoEjercicios.add(new Ejercicio("Wall Ball Shot", TipoEjercicio.CARDIO));
        catalogoEjercicios.add(new Ejercicio("Shuttle Run", TipoEjercicio.CARDIO));
    }

    public List<Ejercicio> obtenerTodos () {
        return this.catalogoEjercicios;
    }

    public List<Ejercicio> filtrarPorTipo (TipoEjercicio ejercicioBuscado) {
        List<Ejercicio> filtrados = new ArrayList<>();
        for (Ejercicio ejercicio : catalogoEjercicios) {
            if (ejercicio.tipo() == ejercicioBuscado) {
                filtrados.add(ejercicio);
            }
        }
        return filtrados;
    }

}
