package dev.martavia.clue.ui;

import dev.martavia.clue.model.Partida;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la interfaz grafica JavaFX.
 * 
 * @author Mauricio Artavia Monge.
 */
public class ClueApp extends Application {

    @Override
    public void start(Stage stage) {
        Partida partida = new Partida();

        SetupView setupView = new SetupView(partida, () -> {
            System.out.println("Setup completo!");
        });

        Scene scene = new Scene(setupView, 500, 600);
        stage.setTitle("Asistente Virtual CLUE");
        stage.setScene(scene);
        stage.show();
    }
}