package com.gymsync.model;

public record PerfilNutricional (
        double peso,
        double altura,
        int edad,
        String genero,
        String objetivo,
        double caloriasTotales,
        double proteinas,
        double carbohidratos,
        double grasas
) {
    public static PerfilNutricional calcular(double peso, double altura, int edad, String genero, String actividad, String objetivo) {
        double tmb;
        if (genero.equalsIgnoreCase("M")) {
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) + 5;
        } else {
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) - 161;
        }

        double factor = switch (actividad) {
            case "1" -> 1.375;
            case "2" -> 1.55;
            case "3" -> 1.725;
            default -> 1.2;
        };

        double mantenimiento = tmb*factor;
        double caloriasFinales = mantenimiento;

        if (objetivo.equals("DEFINICION")) {
            caloriasFinales = mantenimiento - 400;
        } else if (objetivo.equalsIgnoreCase("VOLUMEN")) {
            caloriasFinales = mantenimiento + 350;
        }

        double protGramos = peso * 2.2;
        double grasasGramos = peso * 1.0;
        double caloriasRestantes = caloriasFinales -(protGramos * 4) - (grasasGramos * 9);
        double carbGramos = caloriasRestantes / 4;

        if (carbGramos < 0) carbGramos = 0;

        return new PerfilNutricional(peso, altura, edad, genero, objetivo,
                Math.round(caloriasFinales), Math.round(protGramos),
                Math.round(carbGramos), Math.round(grasasGramos));

    }
}
