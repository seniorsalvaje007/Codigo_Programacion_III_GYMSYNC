import com.gymsync.model.*;
import com.gymsync.repository.AtletaRepositorio;
import com.gymsync.repository.ClaseRepository;
import com.gymsync.service.AuthService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AtletaRepositorio atletaRepo = new AtletaRepositorio();
        ClaseRepository claseRepo = new ClaseRepository();
        AuthService authService = new AuthService(atletaRepo);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n---GYMSYNC---");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Registrarse (Crear nueva cuenta)");
            System.out.println("3. Salir del sistema");
            System.out.print(">");
            String opc = scanner.nextLine();

            switch (opc) {
                case "1":
                    System.out.println("\n---Inicio de sesion---");
                    System.out.print("Correo: ");
                    String correoLogin = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String passLogin = scanner.nextLine();

                    if (authService.iniciarSesion(correoLogin, passLogin)) {
                        if (authService.tienePermiso(Rol.ATLETA)) {
                            menuAtletas(scanner, authService, claseRepo);
                        } else if (authService.tienePermiso(Rol.COACH)) {
                            menuCoach(scanner, authService, claseRepo);
                        }
                    }

                    break;

                case "2":
                    System.out.println("---Registro de cuenta---");
                    System.out.println("1. Soy atleta");
                    System.out.println("2. Soy coach");
                    System.out.print("Selecciona tu rol: ");
                    String rol = scanner.nextLine();

                    if (rol.equals("1")) {
                        System.out.print("Ingresa tu nombre completo: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Ingresa tu correo electronico: ");
                        String correo = scanner.nextLine();
                        System.out.print("Cree una contraseña segura: ");
                        String contra = scanner.nextLine();
                        System.out.println("Seleccione su nivel: ");
                        System.out.println("1. Principiante");
                        System.out.println("2. Intermedio");
                        System.out.println("3. Avanzado");
                        System.out.print(">");
                        String nivel = scanner.nextLine();

                        NivelAtleta nivelFinal = NivelAtleta.PRINCIPIANTE;
                        if (nivel.equals("2")) {nivelFinal =NivelAtleta.INTERMEDIO;}
                        if (nivel.equals("3")) {nivelFinal =NivelAtleta.AVANZADO;}

                        System.out.println("Escoga su plan de membresia: ");
                        System.out.println("1. Mensual");
                        System.out.println("2. Trimestral");
                        System.out.println("3. Anual");
                        System.out.print(">");
                        String tipoMembresia = scanner.nextLine();

                        String membresiaFinal = "MENSUAL";
                        if (tipoMembresia.equals("2")) {membresiaFinal = "TRIMESTRAL";}
                        if (tipoMembresia.equals("3")) {membresiaFinal = "ANUAL";}

                        Atleta nuevoAtleta = atletaRepo.registrarNuevoAtleta(nombre, correo, nivelFinal, membresiaFinal, true);
                        authService.registrarUsuario(correo, contra, Rol.ATLETA);

                        System.out.println("!Registro correctamente! Bienvenido a la comunidad de GYMSYNC");
                        System.out.println("Tu codigo de atleta es " + nuevoAtleta.id());
                    } else if (rol.equals("2")) {
                        System.out.print("Ingrese su correo de coach: ");
                        String correoCoach = scanner.nextLine();
                        System.out.print("Cree su contraseña: ");
                        String contraCoach = scanner.nextLine();

                        authService.registrarUsuario(correoCoach, contraCoach, Rol.COACH);
                        System.out.println("!Cuenta de coach rcreada exitosamente!");
                    } else {
                        System.out.println("Opcion de registro invalido");
                    }

                    break;

                case "3":
                    salir = true;
                    System.out.println("Saliendo del sistema....");
                    break;

                default:
                    System.out.println("Opcion invalida");

            }
        }
    }

    public static void menuAtletas (Scanner scanner, AuthService authService, ClaseRepository claseRepo) {
        boolean enMenu = true;
        Optional<Atleta> atletaActual = authService.obtenerAtletaLogueado();
        String nombreAtleta = atletaActual.map(Atleta::nombre).orElse("Atleta");

        java.time.format.DateTimeFormatter formatoVisual = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        while (enMenu) {
            System.out.println("\n---Menu Atleta---");
            System.out.println("1. Ver mis datos de perfil");
            System.out.println("2. Ver calendario de clases e Inscribirme");
            System.out.println("3. Cerrar sesion y volver al inicio de sesion");
            System.out.print(">");
            String opc = scanner.nextLine();

            if (opc.equals("1")) {
                atletaActual.ifPresent(atleta -> {
                    System.out.println("\n---INFORMACION DE ATLETA---");
                    System.out.println("ID " + atleta.id());
                    System.out.println("Nombre: " + atleta.nombre());
                    System.out.println("Correo: " + atleta.correo());
                    System.out.println("Nivel: " + atleta.nivel());
                    System.out.println("Membresia: " + atleta.tipoMembresia());
                    System.out.println("Estado de membresia: " + (atleta.pagoActivo() ? "ACTIVO":"NO ACTIVO"));
                });
            } else if (opc.equals("2")) {
                System.out.println("\n---CALENDARIO DE CLASES DISPONIBLES---");
                if (claseRepo.agendaOrdenada().isEmpty()) {
                    System.out.println("No existen clases programadas por el Box actualmente");
                } else {
                    java.util.List<ClaseCrossFit> listaClases = new java.util.ArrayList<>(claseRepo.agendaOrdenada());

                    for (int i = 0; i < listaClases.size(); i++) {
                        ClaseCrossFit c = listaClases.get(i);
                        int ocupados = c.getAtletasInscritos().size();

                        System.out.println((i + 1) + ". " + c.getHorario().format(formatoVisual) +
                                " | WOD: " + c.getNombreWOD() +
                                " | Coach: " + c.getCoachAsignado() +
                                " | Cupos: [" + ocupados + "/" + c.getCupoMaximo() + "]");
                    }

                    System.out.println("\nIngrese el numero de la clase a la que desea unirse o '0' para cancelar la incripcion: ");
                    int seleccion = Integer.parseInt(scanner.nextLine());

                    if (seleccion > 0 && seleccion <= listaClases.size()) {
                        ClaseCrossFit claseElegida = listaClases.get(seleccion - 1);
                        atletaActual.ifPresent(atleta -> {
                            claseElegida.inscribirAtleta(atleta);
                        });
                    } else if (seleccion != 0) {
                        System.out.println("Opcion invalida");
                    }
                }

            }else if (opc.equals("3")) {
                authService.cerrarSesion();
                enMenu = false;
            } else {
                System.out.println("Opcion no valida");
            }
        }
    }

    public static void menuCoach (Scanner scanner, AuthService authService, ClaseRepository claseRepo) {
        boolean enMenu = true;

        while (enMenu) {
            System.out.println("\n---Menu Coach---");
            System.out.println("1. Gestionar clases(Bajo contruccion)");
            System.out.println("2. Cerrar Sesion y volver al inicio de sesion");
            System.out.print("> ");
            String opc = scanner.nextLine();

            if (opc.equals("1")) {
                //En esta parte va la logica de treemap para organizar y proyectar horarios creados en el dia
                System.out.println("\n---ASIGNACION DE HORARIOS---");
                System.out.print("Para que dia de este mes le gustaria programar?");
                int dia = Integer.parseInt(scanner.nextLine());

                System.out.println("\nESTADO DEL DIA " + dia + "(05:00 a 22:00)");
                System.out.println("--------------------------------------------------");
                mostrarGrillaDiaria(dia, claseRepo, -1);

                System.out.print("\nEliga una hora de inicio(del 5 al 22)");
                int hora = Integer.parseInt(scanner.nextLine());

                if (hora < 5 || hora > 22) {
                    System.out.println("\nEl horario operativo del Box es de 05:00am a 22:00pm");
                } else {
                    System.out.print("\nNombre del WOD: ");
                    String nombreWOD = scanner.nextLine();

                    System.out.print("Ingrese el cupo maximo de atletas: ");
                    int cupo = Integer.parseInt(scanner.nextLine());

                    System.out.println("Nombre del coach asginado: ");
                    String nombreCoach = scanner.nextLine();

                    LocalDateTime horaExacta = LocalDateTime.of(2026, 6, dia, hora, 0);
                    ClaseCrossFit nuevaClase = new ClaseCrossFit("WOD-" + System.currentTimeMillis(), nombreWOD, horaExacta, cupo, nombreCoach);

                    if (claseRepo.programarClase(nuevaClase)) {
                        System.out.println("Clase programada con exito");
                        System.out.println("Asi quedo tu agenda");
                        mostrarGrillaDiaria(dia, claseRepo, hora);
                    } else {
                        System.out.println("Ese bloque de horario ya se encuentra ocupado");
                    }
                }

            } else if (opc.equals("2")) {
                authService.cerrarSesion();
                enMenu = false;
            } else {
                System.out.println("Opcion no valida");
            }

        }

    }

    private static void mostrarGrillaDiaria (int dia, ClaseRepository claseRepo, int horaRecienAgregada) {
        String RESET = "\u001B[0m";
        String VERDE = "\u001B[32m";
        String ROJO = "\u001B[31m";

        for (int i = 5; i <= 21; i++) {
            boolean ocupado = false;
            String nombreWOD = "";

            for (ClaseCrossFit c : claseRepo.agendaOrdenada()) {
                if (c.getHorario().getDayOfMonth() == dia && c.getHorario().getHour() == i) {
                    ocupado = true;
                    nombreWOD = c.getNombreWOD();
                    break;
                }
            }

            String formatoHora = String.format("[%02d:00 - %02d:00]", i, i + 1);

            if (ocupado) {
                if (i == horaRecienAgregada) {
                    System.out.println(VERDE + " 🟢 " + formatoHora + " -> NUEVO REGISTRO: " + nombreWOD + RESET);
                } else {
                    System.out.println(ROJO + " 🔴 " + formatoHora + " -> Ocupado (" + nombreWOD + ")" + RESET);
                }
            } else {
                System.out.println(" ⚪ " + formatoHora + " -> Disponible");
            }
        }
    }

}