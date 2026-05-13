package dev.martavia.clue.ui;

import dev.martavia.clue.model.Partida;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vista de configuracion inicial de la partida.
 * 
 * @author Mauricio Artavia Monge.
 */
public class SetupView extends VBox {

    private Partida partida;
    private Runnable onSetupComplete;

    private TextField playersField;
    private TextField userField;
    private CheckBox useDefaultCards;
    private TextField weaponsField;
    private TextField suspectsField;
    private TextField roomsField;
    private Label statusLabel;

    /**
     * Constructor de la vista de setup.
     * 
     * @param partida         Instancia del modelo del juego.
     * @param onSetupComplete Callback que se ejecuta cuando el setup termina.
     */
    public SetupView(Partida partida, Runnable onSetupComplete) {
        this.partida = partida;
        this.onSetupComplete = onSetupComplete;

        setSpacing(15);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);

        buildUI();
    }

    private void buildUI() {
        Label title = new Label("Asistente Virtual CLUE");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        Label subtitle = new Label("Configuracion de la partida");
        subtitle.setFont(Font.font("System", 14));

        // Jugadores
        Label playersLabel = new Label("Jugadores (separados por -):");
        playersField = new TextField();
        playersField.setPromptText("Ej: Ana-Brie-Carl");
        playersField.setMaxWidth(400);

        // Usuario
        Label userLabel = new Label("Tu nombre (debe estar en la lista):");
        userField = new TextField();
        userField.setPromptText("Ej: Ana");
        userField.setMaxWidth(400);

        // Cartas estandar
        useDefaultCards = new CheckBox("Usar cartas estandar del CLUE clasico");
        useDefaultCards.setSelected(true);

        // Cartas personalizadas
        Label weaponsLabel = new Label("Armas (separadas por -):");
        weaponsField = new TextField();
        weaponsField.setPromptText("Ej: Llave-Daga-Pistola");
        weaponsField.setMaxWidth(400);

        Label suspectsLabel = new Label("Sospechosos (separados por -):");
        suspectsField = new TextField();
        suspectsField.setPromptText("Ej: Verdi-Blanco-Morad");
        suspectsField.setMaxWidth(400);

        Label roomsLabel = new Label("Habitaciones (separadas por -):");
        roomsField = new TextField();
        roomsField.setPromptText("Ej: Cocina-Sala-Estudio");
        roomsField.setMaxWidth(400);

        // Ocultar campos de cartas si se usan las estandar
        weaponsLabel.setVisible(false);
        weaponsField.setVisible(false);
        suspectsLabel.setVisible(false);
        suspectsField.setVisible(false);
        roomsLabel.setVisible(false);
        roomsField.setVisible(false);

        useDefaultCards.setOnAction(e -> {
            boolean custom = !useDefaultCards.isSelected();
            weaponsLabel.setVisible(custom);
            weaponsField.setVisible(custom);
            suspectsLabel.setVisible(custom);
            suspectsField.setVisible(custom);
            roomsLabel.setVisible(custom);
            roomsField.setVisible(custom);
        });

        // Status
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: red;");

        // Boton
        Button startButton = new Button("Comenzar partida");
        startButton.setOnAction(e -> handleStart());

        getChildren().addAll(
                title, subtitle,
                playersLabel, playersField,
                userLabel, userField,
                useDefaultCards,
                weaponsLabel, weaponsField,
                suspectsLabel, suspectsField,
                roomsLabel, roomsField,
                statusLabel,
                startButton);
    }

    private void handleStart() {
        // Validar jugadores
        String[] players = playersField.getText().split("-");
        if (players.length < 2 || players.length > 6) {
            statusLabel.setText("Error: ingrese entre 2 y 6 jugadores.");
            return;
        }
        partida.setPlayers(players);

        // Validar usuario
        int userID = partida.findUserID(userField.getText().trim());
        if (userID == -1) {
            statusLabel.setText("Error: tu nombre no esta en la lista de jugadores.");
            return;
        }
        partida.setUserID(userID);

        // Cartas
        if (useDefaultCards.isSelected()) {
            String[][] defaultCards = partida.getDefaultCards();
            partida.setCards(defaultCards[0], defaultCards[1], defaultCards[2]);
        } else {
            String[] weapons = weaponsField.getText().split("-");
            String[] suspects = suspectsField.getText().split("-");
            String[] rooms = roomsField.getText().split("-");
            partida.setCards(weapons, suspects, rooms);
        }

        partida.initializeGame();
        partida.calculateCorrectAmountCards();

        onSetupComplete.run();
    }
}