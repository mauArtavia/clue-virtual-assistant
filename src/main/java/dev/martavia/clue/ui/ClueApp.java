package dev.martavia.clue.ui;

import dev.martavia.clue.model.Partida;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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

        ScrollPane scroll = new ScrollPane(setupView);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");

        Scene scene = new Scene(scroll, 500, 650);
        stage.setTitle("Asistente Virtual CLUE");
        stage.setScene(scene);
        stage.show();
    }
}