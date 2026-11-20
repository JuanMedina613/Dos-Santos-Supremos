package pruebas;

import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import torneo.Pareja;
import acontecimientos.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PruebasHeraldoImperial {

	private HeraldoImperial heraldo;
    private Pareja pareja1;

    @BeforeEach
    void setUp() {
        heraldo = new HeraldoImperial();
        pareja1 = new Pareja(1,
                new CultivadorDeLaEspada(1, "Aurelio"),
                new HechiceroDelVacio(2, "Nébula"));
    }

    @Test
    void procesarAcontecimientosYTabla() {
        assertTrue(heraldo instanceof EscuchadorAcontecimientos);

        assertDoesNotThrow(() -> {
            heraldo.notificar(new Proclamacion(pareja1));
            heraldo.presentarTablaPosiciones(List.of(new PosicionTabla(pareja1, 2, 110)));
        });
    }

    @Test
    void rechazarNulos() {
        assertThrows(IllegalArgumentException.class, () -> heraldo.notificar(null));
        assertThrows(IllegalArgumentException.class, () -> heraldo.presentarTablaPosiciones(null));
    }

}
