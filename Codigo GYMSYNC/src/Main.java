
import com.gymsync.model.Atleta;
import com.gymsync.model.ClaseCrossFit;
import com.gymsync.model.NivelAtleta;
import com.gymsync.repository.AtletaRepositorio;
import com.gymsync.repository.ClaseRepository;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        AtletaRepositorio atletaRepo = new AtletaRepositorio();
        ClaseRepository claseRepo = new ClaseRepository();

        Atleta elias = new Atleta("1723456789", "Elias Armas", NivelAtleta.INTERMEDIO, "ANUAL", true);
        Atleta demian = new Atleta("1798765432", "Demian Durand", NivelAtleta.AVANZADO, "MENSUAL", true);

        atletaRepo.guardarAtleta(elias);
        atletaRepo.guardarAtleta(demian);
        System.out.println("Atletas registrados con éxito. Total en memoria: " + atletaRepo.obtenerTotalDeAtletas());

        LocalDateTime horaLunes7AM = LocalDateTime.of(2026, 6, 1, 7, 0);
        LocalDateTime horaLunes8AM = LocalDateTime.of(2026, 6, 1, 8, 0);

        ClaseCrossFit clase1 = new ClaseCrossFit("C1", "Máximo de Snatch", horaLunes8AM, 2, "Coach Isaac");
        ClaseCrossFit clase2 = new ClaseCrossFit("C2", "WOD de Resistencia", horaLunes7AM, 15, "Coach Anthony");

        claseRepo.programarClase(clase1);
        claseRepo.programarClase(clase2);

        ClaseCrossFit claseDuplicada = new ClaseCrossFit("C3", "WOD Clon", horaLunes8AM, 10, "Coach Garcés");
        claseRepo.programarClase(claseDuplicada); // Debería pintar el mensaje de error en consola

        System.out.println("\n=== Agenda Cronológica Automatizada ===");
        for (ClaseCrossFit c : claseRepo.agendaOrdenada()) {
            System.out.println("[" + c.getHorario() + "] - " + c.getNombreWOD() + " con " + c.getCoachAsignado());
        }

    }
}