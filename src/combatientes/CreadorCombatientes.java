package combatientes;

import equipamiento.Equipamiento;
import tecnicas.Tecnica;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CreadorCombatientes {
    private int siguienteId = 1;
    private final Map<String, BiFunction<Integer, String, Combatiente>> constructoresPorTipo;

    public CreadorCombatientes() {
        this.constructoresPorTipo = new HashMap<>();
        // Registro de los 3 tipos iniciales pedidos en el TP
        registrarTipo("Cultivador de la Espada", CultivadorDeLaEspada::new);
        registrarTipo("Monje del Cuerpo Celestial", MonjeDelCuerpoCelestial::new);
        registrarTipo("Hechicero del Vacio", HechiceroDelVacio::new);
    }

    // Permite agregar nuevos tipos en el futuro sin modificar esta clase (Principio Abierto/Cerrado)
    public final void registrarTipo(String nombreTipo, BiFunction<Integer, String, Combatiente> constructor) {
        if (nombreTipo == null || nombreTipo.isBlank()) {
            throw new IllegalArgumentException("El nombre del tipo no puede estar vacio");
        }
        if (constructor == null) {
            throw new NullPointerException("El constructor no puede ser nulo");
        }
        constructoresPorTipo.put(nombreTipo.trim(), constructor);
    }

    public Combatiente crear(String tipo, String nombre, Escuela escuela) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de combatiente no puede estar vacio");
        }
        if (escuela == null) {
            throw new NullPointerException("La escuela no puede ser nula");
        }

        BiFunction<Integer, String, Combatiente> constructor = constructoresPorTipo.get(tipo.trim());
        if (constructor == null) {
            throw new IllegalArgumentException("Tipo de combatiente desconocido: " + tipo);
        }

        // 1. Instanciar el combatiente con su ID unico en orden de creacion
        int idAsignado = siguienteId++;
        Combatiente nuevo = constructor.apply(idAsignado, nombre);

        // 2. Prepararlo segun la filosofia de su Escuela (equipamiento y tecnicas iniciales)
        for (Equipamiento item : escuela.getEquipamientosIniciales()) {
            nuevo.equipar(item);
        }
        for (Tecnica tecnica : escuela.getTecnicasIniciales()) {
            nuevo.aprenderTecnica(tecnica);
        }

        return nuevo;
    }
}