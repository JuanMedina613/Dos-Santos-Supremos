package combatientes;

import equipamiento.Equipamiento;
import tecnicas.Tecnica;

public class CreadorCombatientes {
    // Constante con el valor inicial (final) y variable estática que se incrementa
    private static final int ID_INICIAL = 1;
    private static int ID_COMBATIENTE = ID_INICIAL;

    public Combatiente crear(String tipo, String nombre, Escuela escuela) {
        if (tipo == null || nombre == null || escuela == null) {
            throw new IllegalArgumentException("El tipo, el nombre y la escuela no pueden ser nulos.");
        }

        Combatiente c;
        switch (tipo) {
            case "Cultivador de la Espada":
                c = new CultivadorDeLaEspada(ID_COMBATIENTE, nombre);
                break;
            case "Monje del Cuerpo Celestial":
                c = new MonjeDelCuerpoCelestial(ID_COMBATIENTE, nombre);
                break;
            case "Hechicero del Vacío":
                c = new HechiceroDelVacio(ID_COMBATIENTE, nombre);
                break;
            default:
                throw new IllegalArgumentException("Tipo de combatiente desconocido: " + tipo);
        }

        // Se incrementa en 1 después de haber creado al combatiente
        ID_COMBATIENTE = ID_COMBATIENTE + 1;

        for (Equipamiento e : escuela.getEquipamientoInicial()) {
            c.equipar(e);
        }

        for (Tecnica t : escuela.getTecnicasIniciales()) {
            c.aprenderTecnica(t);
        }

        return c;
    }

    // Método útil para reiniciar el contador entre distintos tests de JUnit 5
    public static void reiniciarContadorId() {
        ID_COMBATIENTE = ID_INICIAL;
    }
}