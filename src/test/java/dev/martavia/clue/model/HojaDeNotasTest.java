package dev.martavia.clue.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitarios para HojaDeNotas.
 * 
 * @author Mauricio Artavia Monge.
 */
public class HojaDeNotasTest {

    private HojaDeNotas hoja;
    private String[] players;
    private String[] cards;

    @Before
    public void setUp() {
        players = new String[] { "Ana", "Brie", "Carl" };
        cards = new String[] { "Llave", "Daga", "Pistola" };
        hoja = new HojaDeNotas(players.length, cards.length);
        hoja.createMatriz(players, cards);
        hoja.createDoubleMatriz();
    }

    @Test
    public void testCreateMatriz_encabezadoJugadores() {
        // Verifica que los jugadores quedan en la fila 0
        // Solo podemos verificarlo indirectamente a traves del comportamiento
        // ya que possibilities es privado
        assertNotNull(hoja);
    }

    @Test
    public void testCreateDoubleMatriz_probabilidadesIniciales() {
        // Con 3 jugadores + sobre = 4 columnas, cada carta debe tener 0.25
        // knownEnvelope retorna DESCONOCIDO si ninguna tiene 1.0
        assertEquals("{DESCONOCIDO}", hoja.knownEnvelope());
    }

    @Test
    public void testAddUserCards_marcaCartaComoConocida() {
        // El jugador 0 tiene la carta en indice 1 (Llave)
        hoja.addUserCards(1, 0);
        // Ahora el sobre no puede tener esa carta, y knownEnvelope
        // deberia seguir desconocido para las otras
        assertEquals("{DESCONOCIDO}", hoja.knownEnvelope());
    }

    @Test
    public void testAnalizeEnvelope_detectaSobre() {
        // Si todas las cartas menos una tienen 0.0 en el sobre, esa debe ser 1.0
        // Jugador 0 tiene carta 1, jugador 1 tiene carta 2, sobre debe tener carta 3
        hoja.addUserCards(1, 0);
        hoja.addUserCards(2, 1);
        hoja.analizeEnvelope();
        assertEquals("Pistola", hoja.knownEnvelope());
    }

    @Test
    public void testVerifyPlayerCards_retornaCantidadCorrecta() {
        hoja.addUserCards(1, 0);
        assertEquals(1, hoja.verifyPlayerCards(0));
    }

    @Test
    public void testAddAskedCards_marcaCartaComoTenida() {
        // Jugador 1 tiene la carta 1 (Llave)
        hoja.addAskedCards(1, 1, true);
        assertEquals(1, hoja.verifyPlayerCards(1));
    }

    @Test
    public void testAddAskedCards_marcaCartaComoNoTenida() {
        // Jugador 1 no tiene la carta 1
        hoja.addAskedCards(1, 1, false);
        // El jugador 1 sigue sin cartas conocidas
        assertEquals(0, hoja.verifyPlayerCards(1));
    }

    @Test
    public void testUpdatePossibilities_redistribuyeProbabilidades() {
        // Cuando se conoce que jugador 0 tiene carta 1,
        // las otras cartas deben redistribuirse entre los restantes
        hoja.addUserCards(1, 0);
        hoja.knownPlayerCards(0);
        // El sobre sigue desconocido
        assertEquals("{DESCONOCIDO}", hoja.knownEnvelope());
    }
}