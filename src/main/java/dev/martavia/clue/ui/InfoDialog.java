package dev.martavia.clue.ui;

import dev.martavia.clue.model.Partida;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Dialogo para registrar informacion de una ronda.
 * 
 * @author Mauricio Artavia Monge.
 */
public class InfoDialog extends Dialog<Void> {

    private Partida partida;
    private boolean isUserTurn;
    private Runnable onComplete;

    private TextField cardsField;
    private VBox respondersBox;
    private Label statusLabel;
    private String[] askedCards;

    /**
     * Constructor del dialogo de informacion.
     * 
     * @param partida    Instancia del modelo del juego.
     * @param isUserTurn true si es el turno del usuario, false si es de otro.
     * @param onComplete Callback que se ejecuta al terminar.
     */
    public InfoDialog(Partida partida, boolean isUserTurn, Runnable onComplete) {
        this.partida = partida;
        this.isUserTurn = isUserTurn;
        this.onComplete = onComplete;

        setTitle(isUserTurn ? "Es mi turno" : "Tengo Informacion");
        buildUI();
    }

    private void buildUI() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");
        content.setPrefWidth(450);

        Label title = new Label(isUserTurn ? "¿Que combinacion preguntaste?" : "¿Por que combinacion preguntaron?");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: #a8dadc;");

        cardsField = new TextField();
        cardsField.setPromptText("Arma-Sospechoso-Habitacion");
        cardsField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-prompt-text-fill: #888;");

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #e94560;");

        Button validateButton = new Button("Validar combinacion");
        validateButton.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8dadc;");
        validateButton.setOnAction(e -> handleValidate());

        respondersBox = new VBox(8);
        respondersBox.setVisible(false);
        respondersBox.setManaged(false);

        content.getChildren().addAll(title, cardsField, validateButton, statusLabel, respondersBox);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(400);
        scroll.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");

        getDialogPane().setContent(scroll);
        getDialogPane().setStyle("-fx-background-color: #1a1a2e;");
        getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Button cancelBtn = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.setStyle("-fx-background-color: #16213e; -fx-text-fill: #a8dadc;");
    }

    private void handleValidate() {
        String[] cards = cardsField.getText().trim().split("-");
        if (cards.length != 3) {
            statusLabel.setText("Error: ingresa exactamente 3 cartas.");
            return;
        }

        if (!partida.validateCards(cards)) {
            statusLabel.setText("Error: alguna carta no existe en el juego.");
            return;
        }

        askedCards = cards;
        statusLabel.setText("");
        buildRespondersFlow();
    }

    private void buildRespondersFlow() {
        respondersBox.getChildren().clear();
        respondersBox.setVisible(true);
        respondersBox.setManaged(true);

        String[] players = partida.getPlayersList();

        Label responderLabel = new Label("¿Quien responde primero?");
        responderLabel.setStyle("-fx-text-fill: #e0e0e0;");

        ComboBox<String> responderCombo = new ComboBox<>();
        for (String player : players) {
            responderCombo.getItems().add(player);
        }
        responderCombo.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
        responderCombo.getSelectionModel().selectFirst();

        Label hasInfoLabel = new Label("¿Tenia informacion?");
        hasInfoLabel.setStyle("-fx-text-fill: #e0e0e0;");

        Button yesButton = new Button("SI");
        Button noButton = new Button("NO");
        yesButton.setStyle("-fx-background-color: #4ade80; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");
        noButton.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold;");

        VBox knownCardBox = new VBox(5);
        knownCardBox.setVisible(false);
        knownCardBox.setManaged(false);

        if (isUserTurn) {
            Label knownLabel = new Label("¿Cual carta te mostro?");
            knownLabel.setStyle("-fx-text-fill: #e0e0e0;");
            TextField knownField = new TextField();
            knownField.setPromptText("Nombre de la carta");
            knownField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-prompt-text-fill: #888;");

            Button confirmKnown = new Button("Confirmar");
            confirmKnown.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8dadc;");
            confirmKnown.setOnAction(e -> {
                String responder = responderCombo.getValue();
                int responderID = partida.findUserID(responder);
                partida.processInfo(askedCards, responderID, true, knownField.getText().trim());
                onComplete.run();
                close();
            });

            knownCardBox.getChildren().addAll(knownLabel, knownField, confirmKnown);
        }

        yesButton.setOnAction(e -> {
            if (isUserTurn) {
                knownCardBox.setVisible(true);
                knownCardBox.setManaged(true);
            } else {
                String responder = responderCombo.getValue();
                int responderID = partida.findUserID(responder);
                partida.processInfo(askedCards, responderID, true, null);
                onComplete.run();
                close();
            }
        });

        noButton.setOnAction(e -> {
            String responder = responderCombo.getValue();
            int responderID = partida.findUserID(responder);
            partida.processInfo(askedCards, responderID, false, null);
            onComplete.run();
            close();
        });

        HBox answerButtons = new HBox(10, yesButton, noButton);
        answerButtons.setAlignment(Pos.CENTER_LEFT);

        respondersBox.getChildren().addAll(
                responderLabel, responderCombo,
                hasInfoLabel, answerButtons,
                knownCardBox);
    }
}