    import com.gymsync.model.*;
    import com.gymsync.repository.*;
    import com.gymsync.service.AuthService;

    import java.time.LocalDate;
    import java.time.temporal.ChronoUnit;
    import java.util.List;
    import java.util.ArrayList;
    import java.time.LocalDateTime;
    import java.util.Optional;
    import java.util.Scanner;

    public class    Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            AtletaRepositorio atletaRepo = new AtletaRepositorio();
            ClaseRepository claseRepo = new ClaseRepository();
            AuthService authService = new AuthService(atletaRepo);
            EjercicioRepository ejercicioRepo = new EjercicioRepository();
            ProgresoRepository progresoRepo = new ProgresoRepository();
            ReseñaRepository reseñaRepo = new ReseñaRepository();
            NutricionRepository nutricionRepo = new NutricionRepository();

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
                                Optional<Atleta> atletaLogueado = authService.obtenerAtletaLogueado();
                                atletaLogueado.ifPresent(atleta -> {
                                    revisarNotificaciones(atleta, claseRepo);

                                    LocalDate hoy = LocalDate.now();
                                    long diasRestantes = ChronoUnit.DAYS.between(hoy, atleta.fechaVencimiento());

                                    if (diasRestantes <= 0) {
                                        pasarelaDePagos(scanner, atleta,atletaRepo, authService);
                                    } else {
                                        menuAtletas(scanner, authService, claseRepo, ejercicioRepo, progresoRepo, reseñaRepo, atletaRepo, nutricionRepo);
                                    }

                                });
                            } else if (authService.tienePermiso(Rol.COACH)) {
                                menuCoach(scanner, authService, claseRepo, atletaRepo, nutricionRepo);
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

                            LocalDate hoy = LocalDate.now();
                            LocalDate fechaExpiracion = hoy;

                            String membresiaFinal = "MENSUAL";
                            fechaExpiracion = hoy.plusMonths(1);
                            if (tipoMembresia.equals("2")) {membresiaFinal = "TRIMESTRAL"; fechaExpiracion = hoy.plusMonths(3);}
                            if (tipoMembresia.equals("3")) {membresiaFinal = "ANUAL";fechaExpiracion = hoy.plusMonths(12);}

                            Atleta nuevoAtleta = atletaRepo.registrarNuevoAtleta(nombre, correo, nivelFinal, membresiaFinal, true, fechaExpiracion);
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

        public static void menuAtletas (Scanner scanner, AuthService authService, ClaseRepository claseRepo, EjercicioRepository ejercicioRepo, ProgresoRepository progresoRepo, ReseñaRepository reseñaRepo, AtletaRepositorio atletaRepo, NutricionRepository nutricionRepo) {
            boolean enMenu = true;
            Optional<Atleta> atletaActual = authService.obtenerAtletaLogueado();
            String nombreAtleta = atletaActual.map(Atleta::nombre).orElse("Atleta");

            java.time.format.DateTimeFormatter formatoVisual = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            while (enMenu) {
                Atleta atleta = atletaRepo.buscarPorId(atletaActual.get().id()).get();
                System.out.println("\n---Menu Atleta---");
                System.out.println("RANGO: " + atleta.getRango() + " | PUNTOS: " + atleta.puntos() + " pts");
                System.out.println("1. Ver mis datos de perfil");
                System.out.println("2. Ver calendario de clases e Inscribirme");
                System.out.println("3. Registrar progreso");
                System.out.println("4. Ver mi historial de progreso");
                System.out.println("5. Calificar Coach");
                System.out.println("6. Calculadora Nutricional");
                System.out.println("7. Cerrar sesion y volver al inicio de sesion");
                System.out.print(">");
                String opc = scanner.nextLine();

                if (opc.equals("1")) {
                    System.out.println("\n---INFORMACION DE ATLETA---");
                    System.out.println("ID " + atleta.id());
                    System.out.println("Nombre: " + atleta.nombre());
                    System.out.println("Correo: " + atleta.correo());
                    System.out.println("Nivel: " + atleta.nivel());
                    System.out.println("Membresia: " + atleta.tipoMembresia());
                    System.out.println("Estado de membresia: " + (atleta.pagoActivo() ? "ACTIVO":"NO ACTIVO"));

                    LocalDate hoy = LocalDate.now();
                    long diasRestantes = ChronoUnit.DAYS.between(hoy,atleta.fechaVencimiento());

                    java.time.format.DateTimeFormatter formatoFecha = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String fechaBonita = atleta.fechaVencimiento().format(formatoFecha);

                    if (diasRestantes > 0) {
                        System.out.println("Vencimiento: " + fechaBonita + " (Te quedan " + diasRestantes + " dias)" );
                    } else {
                        System.out.println("Vencimiento: " + fechaBonita + " (Expirada hace " + Math.abs(diasRestantes) + " dias)");}
                } else if (opc.equals("2")) {
                    System.out.println("\n---CALENDARIO DE CLASES DISPONIBLES---");
                    if (claseRepo.agendaOrdenada().isEmpty()) {
                        System.out.println("No existen clases programadas por el Box actualmente");
                    } else {
                        List<ClaseCrossFit> listaClases = new ArrayList<>(claseRepo.agendaOrdenada());

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
                            claseElegida.inscribirAtleta(atleta);
                        } else if (seleccion != 0) {
                            System.out.println("Opcion invalida");
                        }
                    }

                }else if (opc.equals("3")) {
                    List<Ejercicio> catalogo = ejercicioRepo.obtenerTodos();

                    System.out.println("\n---SELECCIONE UN EJERCICIO---");
                    for (int i = 0; i < catalogo.size(); i++) {
                        System.out.println((i + 1) + ". " + catalogo.get(i).nombre());
                    }

                    System.out.println("Seleccione el numero del ejercicio: ");
                    try {
                        int seleccion = Integer.parseInt(scanner.nextLine()) - 1;

                        if (seleccion >=0 && seleccion < catalogo.size()) {
                            Ejercicio seleccionado = catalogo.get(seleccion);

                            System.out.print("Peso logrado(kg): ");
                            double peso = Double.parseDouble(scanner.nextLine());

                            Progreso nuevo = new Progreso(seleccionado.nombre(), seleccionado.tipo(), peso, LocalDate.now());
                            progresoRepo.registrarProgreso(atletaActual.get().id(), nuevo);
                            atletaRepo.sumarPuntos(atletaActual.get().id(), 50);
                            System.out.println("Nuevo record registrado en " + seleccionado.nombre() + "!");
                        } else {
                            System.out.println("Numero fuera de rango");
                        }
                    }catch (NumberFormatException e) {
                        System.out.println("Por favor ingrese un opcion valida");
                    }


                }else if (opc.equals("4")){
                    List<Progreso> historial = progresoRepo.obtenerHistorial(atletaActual.get().id());

                    if (historial.isEmpty()) {
                        System.out.println("Aun no tienes records registrados");
                    } else {
                        System.out.println("---TU HISTORIAL DE RECORDS---");
                        for (TipoEjercicio tipo: TipoEjercicio.values()) {
                            boolean hayDatos = false;

                            for (Progreso p:historial) {
                                if (p.tipo() == tipo) {
                                    if (!hayDatos) {
                                        System.out.println("\n>> " + tipo + ":");
                                        hayDatos = true;
                                    }
                                    System.out.println("   " + p.fecha() + " | " + p.nombre() + ": " + p.peso() + " kg");
                                }
                            }
                        }
                    }

                }else if (opc.equals("5")) {
                    List<String> listaCoaches = claseRepo.obtenerListaCompleta();

                    if (listaCoaches.isEmpty()) {
                        System.out.println("No se encontraron coaches registrados actualmente");
                    } else {
                        System.out.println("\n---Seleccione el coach a calificar---");
                        for(int i = 0; i < listaCoaches.size(); i++) {
                            System.out.println((i + 1) + ". " + listaCoaches.get(i));
                        }

                        System.out.print("Seleccione el numero del coach que desea calificar: ");
                        int sel = Integer.parseInt(scanner.nextLine()) - 1;

                        if (sel >= 0 && sel < listaCoaches.size()) {
                            String coachElegido = listaCoaches.get(sel);

                            System.out.print("Calificacion (1-5 estrellas): ");
                            int estrellas = Integer.parseInt(scanner.nextLine());

                            System.out.print("Tu comentario: ");
                            String comentario = scanner.nextLine();

                            Reseña nueva = new Reseña(atletaActual.get().id(), atletaActual.get().nombre(), coachElegido, estrellas, comentario, LocalDateTime.now());
                            reseñaRepo.guardarReseña(nueva);
                            System.out.println("Reseña para " + coachElegido + " guardada!!!!");
                        } else {
                            System.out.println("Opcion invalida");
                        }
                    }
                }else if (opc.equals("6")) {
                    System.out.println("\n ---CALCULADORA NUTRICIONAL---");
                    Optional<PerfilNutricional> perfilOpt = nutricionRepo.obtenerPerfil(atleta.id());

                    if (perfilOpt.isPresent()) {
                        PerfilNutricional p = perfilOpt.get();
                        System.out.println("Ya tiene un perfil guardado");
                        System.out.println("Objetivo: " + p.objetivo() + " | " + p.caloriasTotales() + "kcal");
                        System.out.println("Desea recalcular sus macros(S/N): ");
                        if (!scanner.nextLine().equalsIgnoreCase("S")) {
                            System.out.println("Proteinas: " + p.proteinas() + "g (" + p.caloriasTotales() + " kcal)");
                            System.out.println("Carbohidratos: " + p.carbohidratos() + "g (" + (p.carbohidratos() * 4) + "kcal)");
                            System.out.println("Grasas: " + p.grasas() + "g (" + (p.grasas() * 9) + " kcal)");
                            continue;
                        }
                    }

                    System.out.print("Peso Actual (kg): ");
                    double peso = Double.parseDouble(scanner.nextLine());
                    System.out.print("Altura (cm): ");
                    double altura = Double.parseDouble(scanner.nextLine());
                    System.out.print("Edad: ");
                    int edad = Integer.parseInt(scanner.nextLine());
                    System.out.print("Genero (M/F): ");
                    String genero = scanner.nextLine();

                    System.out.println("Nivel de Actividad en el Box");
                    System.out.println("1. Moderado (WODs 3-4 veces por semana)");
                    System.out.println("2. Intenso (WODs 5-6 veces por semana)");
                    System.out.println("3. Atleta Competitivo (Doble sesion / Rx pesado)");
                    System.out.print(">");
                    String actividad = scanner.nextLine();

                    System.out.println("Seleccioma tu objetivo:");
                    System.out.println("1. Definicion (Bajar grasa manteniendo musculo)");
                    System.out.println("2. Mantenimiento (Mejorar rendimiento en WODs)");
                    System.out.println("3. Volumen (Ganar masa muscular y fuerza)");
                    System.out.print(">");
                    String opcObj = scanner.nextLine();

                    String objetivo = "MANTEMIENTO";
                    if (opc.equals("1")) objetivo = "DEFINICION";
                    if (opc.equals("3")) objetivo = "VOlUMEN";

                    PerfilNutricional nuevoPerfil = PerfilNutricional.calcular(peso, altura, edad, genero, actividad, objetivo);
                    nutricionRepo.guardarPerfil(atleta.id(), nuevoPerfil);

                    System.out.println("\nPERFIL NUTRICIONAL ENERGETICO GENERADO");
                    System.out.println("Objetivo principal: " + nuevoPerfil.objetivo());
                    System.out.println("Calorias diarias recomendadas: " + nuevoPerfil.caloriasTotales());
                    System.out.println("Desglose de macronutrientes:");
                    System.out.println("    Proteinas: " + nuevoPerfil.proteinas() + "g");
                    System.out.println("    Carbohidratos" + nuevoPerfil.carbohidratos() + "g");
                    System.out.println("    Grasas" + nuevoPerfil.grasas() + "g");

                }else if(opc.equals("7")) {
                    authService.cerrarSesion();
                    enMenu = false;
                } else {
                    System.out.println("Opcion no valida");
                }
            }
        }

        public static void menuCoach (Scanner scanner, AuthService authService, ClaseRepository claseRepo, AtletaRepositorio atletaRepo, NutricionRepository nutricionRepo) {
            boolean enMenu = true;

            while (enMenu) {
                System.out.println("\n---Menu Coach---");
                System.out.println("1. Gestionar clases");
                System.out.println("2. Registrar asistencia");
                System.out.println("3. Consultar perfil profesional de atleta");
                System.out.println("4. Cerrar Sesion y volver al inicio de sesion");
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

                        System.out.print("Nombre del coach asginado: ");
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
                    System.out.print("Ingrese el dia de la clase:");
                    int dia = Integer.parseInt(scanner.nextLine());

                    List<ClaseCrossFit> clasesDelDia = claseRepo.obtenerClasesPorDia(dia);

                    if (clasesDelDia.isEmpty()) {
                        System.out.println("ADVERTENCIA: No tiene clases agenda para el dia " + dia);
                    } else {
                        System.out.println("\n---SELECCION DE CLASE---");
                        for (int i = 0; i < clasesDelDia.size(); i++) {
                            System.out.println((i + 1) + ". WOD: " + clasesDelDia.get(i).getNombreWOD() +
                                    " |Horario: " + clasesDelDia.get(i).getHorario().getHour() + ":00" +
                                    " |Coach: " + clasesDelDia.get(i).getCoachAsignado());
                        }
                        System.out.print("Seleccione la clase:");
                        int selClase = Integer.parseInt(scanner.nextLine()) - 1;

                        if (selClase >= 0 && selClase < clasesDelDia.size()) {
                            ClaseCrossFit clase = clasesDelDia.get(selClase );
                            List<Atleta> incritos = clase.getAtletasInscritos();

                            if (incritos.isEmpty()) {
                                System.out.println("No hay atletas incritos por el momento");
                            } else {
                                System.out.println("\n---REGISTRAR ASISTENCIA---");
                                for (int i = 0; i < incritos.size(); i++) {
                                    System.out.println((i + 1) + ". " + incritos.get(i).nombre() + "(ID: " + incritos.get(i).id() + ")");
                                }
                                System.out.print("Seleccione al atleta que asistio a la clase de hoy: ");
                                int selAtleta = Integer.parseInt(scanner.nextLine()) - 1;

                                if (selAtleta >= 0 && selAtleta < incritos.size()) {
                                    Atleta a = incritos.get(selAtleta);
                                    clase.registrarAsistencia(a);

                                    atletaRepo.sumarPuntos(a.id(), 100);
                                    System.out.println("Asitencia registrada exitosamente +100 para el atleta" + a.nombre() + ".");
                                }
                            }
                        }else {
                            System.out.println("Seleccion de clase invalida");
                        }

                    }

                }else if (opc.equals("3")){
                    System.out.println("Ingrese el ID del Atleta: ");
                    String idAtleta = scanner.nextLine();

                    Optional<Atleta> atletaOpt = atletaRepo.buscarPorId(idAtleta);

                    if (atletaOpt.isEmpty()) {
                        System.out.println("ERROR: No se encontro ningun atleta registrado con ese ID");
                    } else {
                        Atleta a = atletaOpt.get();
                        System.out.println("\n---SEGUIMIENTO NUTRICIONALDE " + a.nombre().toUpperCase() + "---");

                        Optional<PerfilNutricional> nutOpt = nutricionRepo.obtenerPerfil(a.id());

                        if (nutOpt.isEmpty()) {
                            System.out.println("El atleta no ah realizado su calculo nutricional");
                        } else {
                            PerfilNutricional n = nutOpt.get();
                            System.out.println("Objetivo: " + n.objetivo());
                            System.out.println("Meta calórica: " + n.caloriasTotales() + " kcal/día");
                            System.out.println("Distribución de Macronutrientes recomendada:");
                            System.out.println("   - Proteínas: " + n.proteinas() + "g");
                            System.out.println("   - Carbohidratos: " + n.carbohidratos() + "g");
                            System.out.println("   - Grasas: " + n.grasas() + "g");
                            System.out.println("Nota para el Coach: Asegura el consumo de carbohidratos en pre/post entrenamiento.");
                        }
                    }

                }else if (opc.equals("4")) {
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

        public static void revisarNotificaciones (Atleta atleta, ClaseRepository claseRepo) {
            System.out.println("\n---Centro de Notificaciones---");
            boolean hayNotificacion = false;
            LocalDate hoy = LocalDate.now();

            long diasParaVencer = ChronoUnit.DAYS.between(hoy, atleta.fechaVencimiento());

            if (diasParaVencer <= 0) {
                System.out.println("ADVERTENCIA!!! Su membresia a terminado. Por favor renueva su plan en recepcion");
                hayNotificacion = true;
            } else if (diasParaVencer == 1) {
                System.out.println("ADVERTENCIA!!! Su membreia termina el dia de mañana");
                hayNotificacion = true;
            } else if (diasParaVencer <= 5 && diasParaVencer > 1) {
                System.out.println("ADVERTENCIA!!! Su membresia termina en" + diasParaVencer + "dias");
                hayNotificacion = true;
            }

            for (ClaseCrossFit clase : claseRepo.agendaOrdenada()) {
                if (clase.getAtletasInscritos().contains(atleta)) {
                    long diasParaClase = ChronoUnit.DAYS.between(hoy, clase.getHorario().toLocalDate());

                    if (diasParaClase == 1) {
                        System.out.println("RECORDATORIO: Mañana su WOD es: " + clase.getNombreWOD() +
                                            " a las " + clase.getHorario().toLocalTime() + ".");

                        hayNotificacion = true;
                    }
                }
            }

            if (!hayNotificacion) {
                System.out.println("Tienes todo al dia!");
            }
            System.out.println("-----------------------------------");

        }

        public static boolean pasarelaDePagos (Scanner scanner, Atleta atletaViejo, AtletaRepositorio atletaRepo, AuthService authService) {
            System.out.println("ACCESO DENEGADO: Su membresia a expirado");
            System.out.println("Para continuar usando GYMSYNC, debe de renovar su plan");
            System.out.println("1. Mensual ($15.00)");
            System.out.println("2. Trimestal ($40.00)");
            System.out.println("3. Anual ($100.00)");
            System.out.println("4. Cancelar pago");
            System.out.print(">");
            String opc = scanner.nextLine();

            LocalDate hoy = LocalDate.now();
            LocalDate nuevaFecha = hoy;
            String nuevoPlan = "";

            if (opc.equals("1")) {
                nuevaFecha = hoy.plusMonths(1);
                nuevoPlan = "MENSUAL";
            } else if (opc.equals("2")) {
                nuevaFecha = hoy.plusMonths(3);
                nuevoPlan = "TRIMESTAL";
            } else if (opc.equals("3")) {
                nuevaFecha = hoy.plusMonths(12);
                nuevoPlan = "ANUAL";
            } else if (opc.equals("4")) {
                System.out.println("Operacion cancelada");
                authService.cerrarSesion();
                return false;
            }

            System.out.println("Procesando pago...PAGO EXITOSO!!!");

            Atleta atletaRenovado = new Atleta(atletaViejo.id(), atletaViejo.nombre(),
                                            atletaViejo.correo(), atletaViejo.nivel(),
                                            nuevoPlan, true, nuevaFecha, atletaViejo.puntos());

            atletaRepo.actualizarAtleta(atletaRenovado);

            System.out.println("Membresia " + nuevoPlan + "activada hasta " + nuevaFecha + ".");
            System.out.println("POr seguridad inicie sesion nuevamente para refrescar sus datos.");

            authService.cerrarSesion();

            return true;

        }

    }