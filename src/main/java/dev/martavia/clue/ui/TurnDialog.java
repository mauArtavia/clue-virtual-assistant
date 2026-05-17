package dev.martavia.clue.ui;

import dev.martavia.clue.model.Partida;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Dialogo para manejar el turno del usuario.
 * 
 * @author Mauricio Artavia Monge.
 */
public class TurnDialog extends Dialog<Void> {

    private Partida partida;
    private Runnable onComplete;
    private Label statusLabel;
    private String[] suggestedCards;

    /**
     * Constructor del dialogo de turno.
     *
     * @param partida    Instancia del modelo del juego.
     * @param onComplete Callback que se ejecuta al terminar.
     */
    public TurnDialog(Partida partida, Runnable onComplete) {
        this.partida = partida;
        this.onComplete = onComplete;
        setTitle("Es mi turno");
        buildUI();
    }

    private void buildUI() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");
        content.setPrefWidth(450);

        Label title = new Label("Elige una estrategia de pregunta:");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: #a8dadc;");

        Button btnA = strategyButton("🎲 Totalmente al azar", "A");
        Button btnB = strategyButton("🎯 Parcialmente al azar", "B");
        Button btnC = strategyButton("🧠 Estrategia avanzada", "C");
        Button btnD = strategyButton("⭐ Estrategia ideal", "D");

        Label suggestionLabel = new Label("");
        suggestionLabel.setStyle("-fx-text-fill: #4ade80; -fx-font-size: 13px;");
        suggestionLabel.setWrapText(true);

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #e94560;");

        VBox respondersBox = new VBox(8);
        respondersBox.setVisible(false);
        respondersBox.setManaged(false);

        Label cardsLabel = new Label("Combinacion a preguntar (puedes cambiarla):");
        cardsLabel.setStyle("-fx-text-fill: #e0e0e0;");

        TextField cardsField = new TextField();
        cardsField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-prompt-text-fill: #888;");

        Button askButton = new Button("Registrar respuestas");
        askButton.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8dadc;");

        for (Button btn : new Button[] { btnA, btnB, btnC, btnD }) {
            String strategy = btn.getUserData().toString();
            btn.setOnAction(e -> {
                suggestedCards = partida.getSuggestedQuestion(strategy);
                String suggestion = suggestedCards[0] + " - " + suggestedCards[1] + " - " + suggestedCards[2];
                suggestionLabel.setText("Sugerencia: " + suggestion);
                cardsField.setText(suggestion);
                respondersBox.setVisible(true);
                respondersBox.setManaged(true);
            });
        }

        askButton.setOnAction(e -> {
            String[] cards = cardsField.getText().trim().split(" - ");
            if (cards.length != 3) {
                statusLabel.setText("Error: ingresa exactamente 3 cartas separadas por ' - '");
                return;
            }
            if (!partida.validateCards(cards)) {
                statusLabel.setText("Error: alguna carta no existe en el juego.");
                return;
            }
            buildRespondersFlow(respondersBox, cards);
            askButton.setDisable(true);
        });

        respondersBox.getChildren().addAll(cardsLabel, cardsField, askButton, statusLabel);

        HBox strategies = new HBox(8, btnA, btnB, btnC, btnD);
        strategies.setAlignment(Pos.CENTER_LEFT);

        content.getChildren().addAll(title, strategies, suggestionLabel, respondersBox);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(450);
        scroll.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");

        getDialogPane().setContent(scroll);
        getDialogPane().setStyle("-fx-background-color: #1a1a2e;");
        getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Button cancelBtn = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.setStyle("-fx-background-color: #16213e; -fx-text-fill: #a8dadc;");
    }

    private void buildRespondersFlow(VBox container, String[] cards) {
        if (container.getChildren().size() > 3) {
            container.getChildren().remove(3, container.getChildren().size());
        }

        String[] players = partida.getPlayersList();
        int[] currentIndex = { 0 };

        Label responderLabel = new Label();
        responderLabel.setStyle("-fx-text-fill: #a8dadc; -fx-font-weight: bold;");

        Label hasInfoLabel = new Label("¿Tenia informacion?");
        hasInfoLabel.setStyle("-fx-text-fill: #e0e0e0;");

        Button yesButton = new Button("SI");
        Button noButton = new Button("NO");
        yesButton.setStyle("-fx-background-color: #4ade80; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");
        noButton.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox answerButtons = new HBox(10, yesButton, noButton);
        answerButtons.setAlignment(Pos.CENTER_LEFT);

        VBox knownCardBox = new VBox(5);
        knownCardBox.setVisible(false);
        knownCardBox.setManaged(false);

        Label knownLabel = new Label("¿Cual carta te mostro?");
        knownLabel.setStyle("-fx-text-fill: #e0e0e0;");

        TextField knownField = new TextField();
        knownField.setPromptText("Nombre de la carta");
        knownField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-prompt-text-fill: #888;");

        Button confirmKnown = new Button("Confirmar");
        confirmKnown.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8dadc;");
        confirmKnown.setOnAction(e -> {
            int responderID = partida.findUserID(players[currentIndex[0]]);
            partida.processInfo(cards, responderID, true, knownField.getText().trim());
            onComplete.run();
            close();
        });

        knownCardBox.getChildren().addAll(knownLabel, knownField, confirmKnown);

        // Actualiza el estado según el jugador actual
        Runnable updateState = () -> {
            if (currentIndex[0] >= players.length) {
                responderLabel.setText("Nadie tuvo informacion.");
                hasInfoLabel.setVisible(false);
                hasInfoLabel.setManaged(false);
                answerButtons.setVisible(false);
                answerButtons.setManaged(false);

                Button closeBtn = new Button("Cerrar");
                closeBtn.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8dadc;");
                closeBtn.setOnAction(ev -> {
                    onComplete.run();
                    close();
                });
                container.getChildren().add(closeBtn);
            } else {
                responderLabel.setText("¿" + players[currentIndex[0]] + " tenia informacion?");
            }
        };

        updateState.run();

        yesButton.setOnAction(e -> {
            knownCardBox.setVisible(true);
            knownCardBox.setManaged(true);
            yesButton.setDisable(true);
            noButton.setDisable(true);
        });

        noButton.setOnAction(e -> {
            int responderID = partida.findUserID(players[currentIndex[0]]);
            partida.processInfo(cards, responderID, false, null);
            currentIndex[0]++;
            updateState.run();
        });

        container.getChildren().addAll(
                responderLabel,
                hasInfoLabel,
                answerButtons,
                knownCardBox);
    }

    private Button strategyButton(String text, String strategy) {
        Button b = new Button(text);
        b.setUserData(strategy);
        b.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8dadc; -fx-font-size: 12px; -fx-padding: 8 12;");
        return b;
    }
}