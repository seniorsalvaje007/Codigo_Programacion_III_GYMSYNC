package com.gymsync.repository;

import com.gymsync.model.*;
import com.gymsync.repository.*;

import java.time.LocalDate;
    import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteAdministrativoService {

        private final AtletaRepositorio atletaRepo;
        private final ClaseRepository claseRepo;
        private final ReseñaRepository reseñaRepo;

        public ReporteAdministrativoService (AtletaRepositorio atletaRepo, ClaseRepository claseRepo, ReseñaRepository reseñaRepo) {
            this.atletaRepo = atletaRepo;
            this.claseRepo = claseRepo;
            this.reseñaRepo = reseñaRepo;
        }

        public void generarReporte () {
            System.out.println("\n======================================================================");
            System.out.println("---GYMSYNC SYSTEM - PANEL DE MONITOREO ADMINISTRATIVO---");
            System.out.println("Fecha de reporte: " + LocalDate.now() + " | Box Administrativo");
            System.out.println("======================================================================");

            int mensuales = 0, trimestrales = 0, anuales = 0;
            int totalAtletas = atletaRepo.obtenerTotalAtletas();
            LocalDate hoy = LocalDate.now();

            List<Atleta> atletasValidos = new ArrayList<>();

            for (int i = 1; i <= totalAtletas; i++) {
                String id = String.format("GS-%03d", i);
                var atletaOpt = atletaRepo.buscarPorId(id);
                atletaOpt.ifPresent(atletasValidos::add);
            }

            for (Atleta atleta : atletasValidos) {
                if (atleta.pagoActivo() && atleta.tipoMembresia().equalsIgnoreCase("MENSUAL")) {
                    mensuales++;
                } else if (atleta.pagoActivo() && atleta.tipoMembresia().equalsIgnoreCase("TRIMESTRAL")) {
                    trimestrales++;
                } else if (atleta.pagoActivo() && atleta.tipoMembresia().equalsIgnoreCase("ANUAL")) {
                    anuales++;
                }
            }

            int totalAtletasActivos = mensuales + trimestrales + anuales;
            int totalAtletasExpirados = totalAtletas - totalAtletasActivos;

            double facMensual = mensuales * 15.000;
            double facTrimestral = trimestrales * 40.00;
            double facAnual = anuales * 100.00;
            double totalFacturacion = facMensual + facTrimestral + facAnual;

            double porcMensual = totalAtletasActivos == 0 ? 0 : (((double) mensuales)/totalAtletasActivos * 100);
            double porcTrimestral = totalAtletasActivos == 0 ? 0 : (((double) trimestrales)/totalAtletasActivos * 100);
            double porcAnual = totalAtletasActivos == 0 ? 0 : (((double) anuales)/totalAtletasActivos * 100);

            System.out.println("\n[1] RESUMEN FINANCIERO Y MEMBRESÍAS");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Membresías Activas Totales en el Box: " + totalAtletasActivos + " atletas\n");
            System.out.println("Distribución de Planes y Recaudación Proyectada Activa:");
            System.out.printf("  • Plan MENSUAL    ( $15.00 ):  %d atletas  ->  $%,.2f  (%.1f%%)\n", mensuales, facMensual, porcMensual);
            System.out.printf("  • Plan TRIMESTRAL ( $40.00 ):  %d atletas  ->  $%,.2f  (%.1f%%)\n", trimestrales, facTrimestral, porcTrimestral);
            System.out.printf("  • Plan ANUAL     ($100.00 ):  %d atletas  ->  $%,.2f  (%.1f%%)\n", anuales, facAnual, porcAnual);
            System.out.println("----------------------------------------------------------------------");
            System.out.printf(" TOTAL FACTURACIÓN MENSUAL PROYECTADA: $%,.2f\n", totalFacturacion);
            System.out.println("\n• Alerta de Retención:");
            System.out.println("  • Atletas Activos con Pago al Día: " + totalAtletasActivos);
            System.out.println("  • Atletas con Membresía Expirada:  " + totalAtletasExpirados + " (Acceso denegado en login)");

            System.out.println("\n[2] CAPACIDAD OPERATIVA Y OCUPACIÓN DEL BOX");
            System.out.println("----------------------------------------------------------------------");
            List<ClaseCrossFit> todasLasClases = new ArrayList<>(claseRepo.agendaOrdenada());

            if (todasLasClases.isEmpty()) {
                System.out.println("DATOS INSUFICIENTES: No existen clases programadas en la agenda actualmente.");
            } else {
                int totalCuposOfertados = 0;
                int totalCuposOcupados = 0;

                Map<Integer, Integer> cuposPorHora = new HashMap<>();
                Map<Integer, Integer> reservadosPorHora = new HashMap<>();

                for (ClaseCrossFit clase : todasLasClases) {
                    int hora = clase.getHorario().getHour();
                    int capacidad = clase.getCupoMaximo();
                    int inscritos = clase.getAtletasInscritos().size();

                    totalCuposOfertados += capacidad;
                    totalCuposOcupados += inscritos;

                    cuposPorHora.put(hora, cuposPorHora.getOrDefault(hora, 0) + capacidad);
                    reservadosPorHora.put(hora, reservadosPorHora.getOrDefault(hora, 0) + inscritos);
                }

                double porcentajeOcupacion = ((double) totalCuposOcupados / totalCuposOfertados) * 100;
                System.out.printf("Tasa de Ocupación Física General del Mes: %.2f%%\n", porcentajeOcupacion);
                System.out.println("(Total de cupos reservados: " + totalCuposOcupados + " de " + totalCuposOfertados + " ofertados en la agenda)\n");
                System.out.println("Análisis Real de Franjas Horarias Calculadas:");

                for (int hora : cuposPorHora.keySet()) {
                    int max = cuposPorHora.get(hora);
                    int ocupados = reservadosPorHora.getOrDefault(hora, 0);
                    double porcFranja = ((double) ocupados / max) * 100;

                    String semaforo = "⚪";
                    if (porcFranja >= 85) semaforo = "🟢 (Crítico)";
                    else if (porcFranja >= 50) semaforo = "🟡 (Estable)";
                    else semaforo = "🔴 (Bajo)";

                    System.out.printf("  • [%02d:00 - %02d:00] -> %s %.2f%% Ocupación [%d/%d cupos]\n",
                            hora, hora + 1, semaforo, porcFranja, ocupados, max);
                }
            }


            System.out.println("\n[3] CONTROL DE CALIDAD Y DESEMPEÑO DE COACHES");
            System.out.println("----------------------------------------------------------------------");

            List<String> listaCoaches = claseRepo.obtenerListaCompleta();
            List<Reseña> todasLasReseñas = reseñaRepo.obtenerTodas();

            if (listaCoaches.isEmpty()) {
                System.out.println("[REGISTRO VACÍO]: No se encuentran coaches registrados en el sistema por el momento.");
            } else {
                System.out.println("Calificación Promedio del Staff (Calculada en Tiempo Real):\n");
                System.out.println("RANKING DE COACHES:");

                List<Reseña> alertasCriticas = new ArrayList<>();

                for (String coach : listaCoaches) {
                    int sumaEstrellas = 0;
                    int contadorReseñas = 0;

                    for (Reseña r : todasLasReseñas) {
                        if (r.nombreCoach().equalsIgnoreCase(coach)) {
                            sumaEstrellas += r.estrellas();
                            contadorReseñas++;

                            if (r.estrellas() < 3) {
                                alertasCriticas.add(r);
                            }
                        }
                    }

                    if (contadorReseñas == 0) {
                        System.out.println("Coach " + coach + " -> Sin calificaciones registradas aún.");
                    } else {
                        double promedio = (double) sumaEstrellas / contadorReseñas;
                        String rendimiento = "Estable";
                        if (promedio >= 4.5) rendimiento = "Excelente";
                        else if (promedio < 3.5) rendimiento = "Bajo Promedio";

                        System.out.printf("  • Coach %-20s ->  %.1f / 5.0  (%d reseñas)  [%s]\n",
                                coach, promedio, contadorReseñas, rendimiento);
                    }
                }

                System.out.println("\nALERTAS DE CALIDAD ADMINISTRATIVA (Reseñas < 3 estrellas encontradas):");
                if (alertasCriticas.isEmpty()) {
                    System.out.println("¡Todo al día! No se han registrado quejas o reseñas bajas de rendimiento.");
                } else {
                    for (Reseña alerta : alertasCriticas) {
                        System.out.printf("Atleta ID: %s | Coach: %s | Calificación: %s\n",
                                alerta.idAtleta(), alerta.nombreCoach(), "⭐".repeat(alerta.estrellas()));
                        System.out.println("     \"" + alerta.comentario() + "\"");
                    }
                }
            }
            System.out.println("======================================================================");
        }
    }
