package dev.martavia.clue.ui;

import dev.martavia.clue.model.Partida;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Vista de configuracion inicial con pasos progresivos.
 * 
 * @author Mauricio Artavia Monge.
 */
public class SetupView extends VBox {

    private Partida partida;
    private Runnable onSetupComplete;

    // Campos paso 1
    private TextField playersField;
    private TextField userField;

    // Campos paso 2
    private CheckBox useDefaultCards;
    private TextField weaponsField;
    private TextField suspectsField;
    private TextField roomsField;

    // Campos paso 3
    private VBox publicCardsBox;

    // Campos paso 4
    private TextField userCardsField;

    // Contenedores de pasos
    private VBox step1, step2, step3, step4;
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

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #1a1a2e;");

        buildUI();
    }

    private void buildUI() {
        Label title = new Label("Asistente Virtual CLUE");
        title.setFont(Font.font("System", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #e94560;");

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 13px;");

        step1 = buildStep1();
        step2 = buildStep2();
        step3 = buildStep3();
        step4 = buildStep4();

        step2.setVisible(false);
        step2.setManaged(false);
        step3.setVisible(false);
        step3.setManaged(false);
        step4.setVisible(false);
        step4.setManaged(false);

        getChildren().addAll(title, statusLabel, step1, step2, step3, step4);
    }

    private VBox buildStep1() {
        VBox box = buildStepContainer("Paso 1 — Jugadores");

        playersField = styledTextField("Ej: Ana-Brie-Carl-Mau");
        userField = styledTextField("Ej: Mau");

        Button next = styledButton("Siguiente →");
        next.setOnAction(e -> handleStep1());

        box.getChildren().addAll(
                styledLabel("Jugadores (separados por -):"),
                playersField,
                styledLabel("Tu nombre:"),
                userField,
                next);
        return box;
    }

    private VBox buildStep2() {
        VBox box = buildStepContainer("Paso 2 — Cartas");

        useDefaultCards = new CheckBox("Usar cartas del CLUE clasico");
        useDefaultCards.setSelected(true);
        useDefaultCards.setStyle("-fx-text-fill: #a8dadc;");

        weaponsField = styledTextField("Ej: Llave-Daga-Pistola");
        suspectsField = styledTextField("Ej: Verdi-Blanco-Morad");
        roomsField = styledTextField("Ej: Cocina-Sala-Estudio");

        Label wLabel = styledLabel("Armas:");
        Label sLabel = styledLabel("Sospechosos:");
        Label rLabel = styledLabel("Habitaciones:");

        wLabel.setVisible(false);
        wLabel.setManaged(false);
        weaponsField.setVisible(false);
        weaponsField.setManaged(false);
        sLabel.setVisible(false);
        sLabel.setManaged(false);
        suspectsField.setVisible(false);
        suspectsField.setManaged(false);
        rLabel.setVisible(false);
        rLabel.setManaged(false);
        roomsField.setVisible(false);
        roomsField.setManaged(false);

        useDefaultCards.setOnAction(e -> {
            boolean custom = !useDefaultCards.isSelected();
            wLabel.setVisible(custom);
            wLabel.setManaged(custom);
            weaponsField.setVisible(custom);
            weaponsField.setManaged(custom);
            sLabel.setVisible(custom);
            sLabel.setManaged(custom);
            suspectsField.setVisible(custom);
            suspectsField.setManaged(custom);
            rLabel.setVisible(custom);
            rLabel.setManaged(custom);
            roomsField.setVisible(custom);
            roomsField.setManaged(custom);
        });

        Button next = styledButton("Siguiente →");
        next.setOnAction(e -> handleStep2());

        box.getChildren().addAll(
                useDefaultCards,
                wLabel, weaponsField,
                sLabel, suspectsField,
                rLabel, roomsField,
                next);
        return box;
    }

    private VBox buildStep3() {
        VBox box = buildStepContainer("Paso 3 — Cartas publicas");
        publicCardsBox = new VBox(8);
        Button next = styledButton("Siguiente →");
        next.setOnAction(e -> handleStep3());
        box.getChildren().addAll(publicCardsBox, next);
        return box;
    }

    private VBox buildStep4() {
        VBox box = buildStepContainer("Paso 4 — Tus cartas");
        userCardsField = styledTextField("");
        Button start = styledButton("Comenzar partida");
        start.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        start.setOnAction(e -> handleStep4());
        box.getChildren().addAll(
                styledLabel("Tus cartas (separadas por -):"),
                userCardsField,
                start);
        return box;
    }

    private void handleStep1() {
        String[] players = playersField.getText().trim().split("-");
        if (players.length < 2 || players.length > 6) {
            statusLabel.setText("Error: ingrese entre 2 y 6 jugadores.");
            return;
        }
        partida.setPlayers(players);
        int userID = partida.findUserID(userField.getText().trim());
        if (userID == -1) {
            statusLabel.setText("Error: tu nombre no esta en la lista.");
            return;
        }
        partida.setUserID(userID);
        statusLabel.setText("");
        showStep(step2);
    }

    private void handleStep2() {
        if (useDefaultCards.isSelected()) {
            String[][] dc = partida.getDefaultCards();
            partida.setCards(dc[0], dc[1], dc[2]);
        } else {
            if (weaponsField.getText().isBlank() || suspectsField.getText().isBlank()
                    || roomsField.getText().isBlank()) {
                statusLabel.setText("Error: ingrese todas las listas de cartas.");
                return;
            }
            partida.setCards(
                    weaponsField.getText().trim().split("-"),
                    suspectsField.getText().trim().split("-"),
                    roomsField.getText().trim().split("-"));
        }

        partida.initializeGame();
        partida.calculateCorrectAmountCards();

        if (partida.getCorrectAmountCards() == 0) {
            statusLabel.setText("Error: no hay suficientes cartas para todos los jugadores. "
                    + "Necesitas al menos " + (partida.getPlayersList().length + 3) + " cartas en total.");
            return;
        }

        int publicAmount = partida.getPublicCardsAmount();
        publicCardsBox.getChildren().clear();

        if (publicAmount == 0) {
            publicCardsBox.getChildren().add(styledLabel("No hay cartas publicas en esta partida."));
        } else {
            for (int i = 0; i < publicAmount; i++) {
                publicCardsBox.getChildren().add(styledLabel("Carta publica #" + (i + 1) + ":"));
                publicCardsBox.getChildren().add(styledTextField("Nombre de la carta"));
            }
        }

        statusLabel.setText("");
        showStep(step3);
    }

    private void handleStep3() {
        int publicAmount = partida.getPublicCardsAmount();

        if (publicAmount > 0) {
            String[] publicCards = new String[publicAmount];
            int fieldIndex = 1;
            for (int i = 0; i < publicAmount; i++) {
                TextField tf = (TextField) publicCardsBox.getChildren().get(fieldIndex);
                publicCards[i] = tf.getText().trim();
                fieldIndex += 2;
            }
            partida.setPublicCards(publicCards);
            partida.applyPublicCards();
        }

        int correct = partida.getCorrectAmountCards();
        userCardsField.setPromptText("Debes tener " + correct + " cartas");
        statusLabel.setText("");
        showStep(step4);
    }

    private void handleStep4() {
        String[] cards = userCardsField.getText().trim().split("-");
        int correct = partida.getCorrectAmountCards();

        if (cards.length != correct) {
            statusLabel.setText("Error: debes ingresar exactamente " + correct + " cartas.");
            return;
        }

        partida.setUserCards(cards);
        partida.applyUserCards();
        onSetupComplete.run();
    }

    private void showStep(VBox step) {
        step.setVisible(true);
        step.setManaged(true);
        FadeTransition ft = new FadeTransition(Duration.millis(400), step);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private VBox buildStepContainer(String title) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        box.setMaxWidth(450);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        titleLabel.setStyle("-fx-text-fill: #a8dadc;");
        box.getChildren().add(titleLabel);
        return box;
    }

    private Label styledLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #e0e0e0;");
        return l;
    }

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setMaxWidth(400);
        tf.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-prompt-text-fill: #888;");
        return tf;
    }

    private Button styledButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8dadc; -fx-font-size: 13px; -fx-padding: 8 16;");
        return b;
    }
}