package dev.martavia.clue;

import dev.martavia.clue.ui.ConsoleUI;

/**
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
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}