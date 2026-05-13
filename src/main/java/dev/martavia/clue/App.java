package dev.martavia.clue;

/**
 * 
 * Clase App.
 * 
 * @author Mauricio Artavia Monge
 */
public class App {

    /**
     * Metodo main del programa Asistente Virtual CLUE.
     * 
     * @param args Argumentos del programa.
     */
    public static void main(String[] args) {
        Partida game = new Partida();
        game.game();
    }
}
