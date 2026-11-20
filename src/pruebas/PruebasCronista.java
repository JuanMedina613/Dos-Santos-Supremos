package pruebas;

import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import combatientes.MonjeDelCuerpoCelestial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import acontecimientos.*;
import torneo.Pareja;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PruebasCronista {

	private CronistaDeLaSecta cronista;
    private Pareja pareja1;
    private Pareja pareja2;
    private Pareja pareja3;
    private Pareja pareja4;

    @BeforeEach
    void setUp() {
        cronista = new CronistaDeLaSecta();

        pareja1 = new Pareja(1,
                new CultivadorDeLaEspada(1, "C1"),
                new HechiceroDelVacio(2, "C2"));
        pareja2 = new Pareja(2,
                new MonjeDelCuerpoCelestial(3, "C3"),
                new CultivadorDeLaEspada(4, "C4"));
        pareja3 = new Pareja(3,
                new HechiceroDelVacio(5, "C5"),
                new MonjeDelCuerpoCelestial(6, "C6"));
        pareja4 = new Pareja(4,
                new CultivadorDeLaEspada(7, "C7"),
                new HechiceroDelVacio(8, "C8"));
    }

    @Test
    void registrarHistorialEnOrden() {
        Acontecimiento inicio = new InicioCombate(1, pareja1, pareja2);
        Acontecimiento accion = new AccionRealizada(1, pareja1.getCombatiente1(), pareja2.getCombatiente1(), "Ataque básico", false);
        Acontecimiento resultado = new ResultadoCombate(1, pareja1, 90, pareja2, 0, pareja1);

        cronista.notificar(inicio);
        cronista.notificar(accion);
        cronista.notificar(resultado);

        List<Acontecimiento> historial = cronista.obtenerHistorial();
        assertEquals(3, historial.size());
        assertEquals(inicio, historial.get(0));
        assertEquals(accion, historial.get(1));
        assertEquals(resultado, historial.get(2));

        assertThrows(UnsupportedOperationException.class, () -> historial.add(inicio));
    }

    @Test
    //Ordena la tabla por última ronda jugada y luego por vida total final"
    void ordenarTablaPorRondaYVidaTotal() {
        // Semifinal 1 (Ronda 1): gana Pareja 1 con 150 de vida; pierde Pareja 2 con 0 de vida.
        cronista.notificar(new ResultadoCombate(1, pareja1, 150, pareja2, 0, pareja1));

        // Semifinal 2 (Ronda 1): gana Pareja 4 por límite de asaltos con 80 de vida;
        // pierde Pareja 3 por límite de asaltos conservando 45 de vida.
        cronista.notificar(new ResultadoCombate(1, pareja3, 45, pareja4, 80, pareja4));

        // Final (Ronda 2): Pareja 1 gana el torneo con 60 de vida; Pareja 4 cae con 0.
        // Nota: Pareja 1 tenía 150 en Ronda 1, pero debe prevalecer su marca de Ronda 2 (60).
        cronista.notificar(new ResultadoCombate(2, pareja1, 60, pareja4, 0, pareja1));
        cronista.notificar(new Proclamacion(pareja1));

        List<PosicionTabla> tabla = cronista.generarTablaPosiciones();

        assertEquals(4, tabla.size());

        // 1° puesto: Pareja 1 (Ronda 2, vida 60)
        assertEquals(pareja1, tabla.get(0).getPareja());
        assertEquals(2, tabla.get(0).getUltimaRonda());
        assertEquals(60, tabla.get(0).getVidaTotalFinal());

        // 2° puesto: Pareja 4 (Ronda 2, vida 0) -> supera a Pareja 3 aunque Pareja 3 terminó con 45 de vida en Ronda 1
        assertEquals(pareja4, tabla.get(1).getPareja());
        assertEquals(2, tabla.get(1).getUltimaRonda());
        assertEquals(0, tabla.get(1).getVidaTotalFinal());

        // 3° puesto: Pareja 3 (Ronda 1, vida 45)
        assertEquals(pareja3, tabla.get(2).getPareja());
        assertEquals(1, tabla.get(2).getUltimaRonda());
        assertEquals(45, tabla.get(2).getVidaTotalFinal());

        // 4° puesto: Pareja 2 (Ronda 1, vida 0)
        assertEquals(pareja2, tabla.get(3).getPareja());
        assertEquals(1, tabla.get(3).getUltimaRonda());
        assertEquals(0, tabla.get(3).getVidaTotalFinal());
    }

    @Test
    void desempatarPorIdentificadorDePareja() {
        // Dos parejas pierden en Ronda 1 con 0 de vida: debe quedar arriba la de menor ID (pareja2 antes que pareja4)
        cronista.notificar(new ResultadoCombate(1, pareja1, 100, pareja4, 0, pareja1));
        cronista.notificar(new ResultadoCombate(1, pareja3, 95, pareja2, 0, pareja3));

        // En la final empatan en vida al límite de asaltos (50 vs 50) y gana pareja1 por ID
        cronista.notificar(new ResultadoCombate(2, pareja3, 50, pareja1, 50, pareja1));

        List<PosicionTabla> tabla = cronista.generarTablaPosiciones();

        assertEquals(pareja1, tabla.get(0).getPareja());
        assertEquals(pareja3, tabla.get(1).getPareja());
        assertEquals(pareja2, tabla.get(2).getPareja());
        assertEquals(pareja4, tabla.get(3).getPareja());
    }

    @Test
    void rechazarAcontecimientoNulo() {
        assertThrows(IllegalArgumentException.class, () -> cronista.notificar(null));
    }
}
