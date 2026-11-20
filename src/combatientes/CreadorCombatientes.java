package combatientes;

import equipamiento.Equipamiento;
import tecnicas.Tecnica;

public class CreadorCombatientes {
    private int siguienteId = 1;

    public CreadorCombatientes() {
        // Sin inicialización adicional
    }

    public Combatiente crear(String tipo, String nombre, Escuela escuela) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de combatiente no puede estar vacío.");
        }
        if (escuela == null) {
            throw new NullPointerException("La escuela no puede ser nula.");
        }

        int idAsignado = siguienteId++;
        Combatiente nuevo;

        switch (tipo.trim()) {
            case "Cultivador de la Espada":
                nuevo = new CultivadorDeLaEspada(idAsignado, nombre);
                break;

            case "Monje del Cuerpo Celestial":
                nuevo = new MonjeDelCuerpoCelestial(idAsignado, nombre);
                break;

            case "Hechicero del Vacío":
                nuevo = new HechiceroDelVacio(idAsignado, nombre);
                break;

            default:
                throw new IllegalArgumentException(
                        "Tipo de combatiente desconocido: " + tipo
                );
        }

        for (Equipamiento item : escuela.getEquipamientosIniciales()) {
            nuevo.equipar(item);
        }

        for (Tecnica tecnica : escuela.getTecnicasIniciales()) {
            nuevo.aprenderTecnica(tecnica);
        }

        return nuevo;
    }
}
