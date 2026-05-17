package dev.martavia.clue.ui;

import dev.martavia.clue.model.Partida;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vista principal del juego con matrices y menu de acciones.
 * 
 * @author Mauricio Artavia Monge.
 */
public class GameView extends BorderPane {

    private Partida partida;
    private VBox weaponsTable;
    private VBox suspectsTable;
    private VBox roomsTable;
    private Label envelopeLabel;

    /**
     * Constructor de la vista principal del juego.
     * 
     * @param partida Instancia del modelo del juego.
     */
    public GameView(Partida partida) {
        this.partida = partida;
        setStyle("-fx-background-color: #1a1a2e;");
        buildUI();
        refreshMatrices();
    }

    private void buildUI() {
        // Titulo
        Label title = new Label("Asistente Virtual CLUE");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: #e94560;");

        envelopeLabel = new Label();
        envelopeLabel.setStyle("-fx-text-fill: #a8dadc; -fx-font-size: 13px;");

        VBox top = new VBox(5, title, envelopeLabel);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(15));
        top.setStyle("-fx-background-color: #16213e;");
        setTop(top);

        // Matrices
        weaponsTable = new VBox(3);
        suspectsTable = new VBox(3);
        roomsTable = new VBox(3);

        VBox weaponsBox = tableContainer("ARMAS", weaponsTable);
        VBox suspectsBox = tableContainer("SOSPECHOSOS", suspectsTable);
        VBox roomsBox = tableContainer("HABITACIONES", roomsTable);

        HBox matrices = new HBox(10, weaponsBox, suspectsBox, roomsBox);
        matrices.setPadding(new Insets(10));
        matrices.setStyle("-fx-background-color: #1a1a2e;");

        ScrollPane scrollMatrices = new ScrollPane(matrices);
        scrollMatrices.setFitToHeight(true);
        scrollMatrices.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");
        setCenter(scrollMatrices);

        // Botones
        Button infoButton = styledButton("📋 Tengo Informacion", "#0f3460");
        Button turnButton = styledButton("🎯 Es mi turno", "#0f3460");
        Button exitButton = styledButton("❌ Salir", "#e94560");

        infoButton.setOnAction(e -> showInfoDialog(false));
        turnButton.setOnAction(e -> {
            TurnDialog dialog = new TurnDialog(partida, () -> refreshMatrices());
            dialog.showAndWait();
        });
        exitButton.setOnAction(e -> System.exit(0));

        HBox buttons = new HBox(10, infoButton, turnButton, exitButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15));
        buttons.setStyle("-fx-background-color: #16213e;");
        setBottom(buttons);
    }

    private VBox tableContainer(String title, VBox table) {
        Label label = new Label(title);
        label.setFont(Font.font("System", FontWeight.BOLD, 13));
        label.setStyle("-fx-text-fill: #e94560;");

        ScrollPane tableScroll = new ScrollPane(table);
        tableScroll.setFitToWidth(false);
        tableScroll.setStyle("-fx-background: #16213e; -fx-background-color: #16213e;");
        tableScroll.setPrefHeight(400);

        VBox box = new VBox(5, label, tableScroll);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        return box;
    }

    /**
     * Refresca las matrices con los datos actuales de la partida.
     */
    public void refreshMatrices() {
        partida.refreshMatriz();

        String[] players = partida.getPlayersList();
        String weapon = partida.getEnvelopeWeapon();
        String suspect = partida.getEnvelopeSuspect();
        String room = partida.getEnvelopeRoom();

        envelopeLabel.setText("Sobre → Arma: " + weapon
                + "  |  Sospechoso: " + suspect
                + "  |  Habitacion: " + room);

        buildTable(weaponsTable, partida.getWeaponsList(), players, partida.getWeaponsProbabilities());
        buildTable(suspectsTable, partida.getSuspectsList(), players, partida.getSuspectsProbabilities());
        buildTable(roomsTable, partida.getRoomsList(), players, partida.getRoomsProbabilities());
    }

    private void buildTable(VBox container, String[] cards, String[] players, double[][] probs) {
        container.getChildren().clear();

        // Encabezado
        HBox header = new HBox(5);
        header.getChildren().add(styledCell("", true, true));
        for (String player : players) {
            header.getChildren().add(styledCell(player, true, true));
        }
        header.getChildren().add(styledCell("Sobre", true, true));
        container.getChildren().add(header);

        // Filas
        for (int i = 0; i < cards.length; i++) {
            HBox row = new HBox(5);
            row.getChildren().add(styledCell(cards[i], false, true));
            for (int j = 0; j < players.length; j++) {
                String val = String.format("%.2f", probs[i][j]);
                boolean isOne = probs[i][j] == 1.0;
                boolean isZero = probs[i][j] == 0.0;
                Label cell = styledCell(val, false, false);
                if (isOne)
                    cell.setStyle(cell.getStyle() + "-fx-text-fill: #4ade80;");
                else if (isZero)
                    cell.setStyle(cell.getStyle() + "-fx-text-fill: #666;");
                row.getChildren().add(cell);
            }
            // Columna sobre
            String envelopeVal = String.format("%.2f", probs[i][players.length]);
            Label envelopeCell = styledCell(envelopeVal, false, false);
            if (probs[i][players.length] == 1.0)
                envelopeCell.setStyle(envelopeCell.getStyle() + "-fx-text-fill: #e94560;");
            row.getChildren().add(envelopeCell);
            container.getChildren().add(row);
        }
    }

    private Label styledCell(String text, boolean isHeader, boolean isName) {
        Label l = new Label(text);
        l.setAlignment(Pos.CENTER);
        l.setPadding(new Insets(3));

        if (isHeader) {
            l.setPrefWidth(70);
            l.setStyle("-fx-text-fill: #a8dadc; -fx-font-weight: bold; -fx-font-size: 11px;");
        } else if (isName) {
            l.setPrefWidth(110); // ← más ancho para nombres de cartas
            l.setAlignment(Pos.CENTER_LEFT);
            l.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 11px;");
        } else {
            l.setPrefWidth(55);
            l.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 11px;");
        }
        return l;
    }

    private void showInfoDialog(boolean isUserTurn) {
        InfoDialog dialog = new InfoDialog(partida, isUserTurn, () -> {
            refreshMatrices();
        });
        dialog.showAndWait();
    }

    private Button styledButton(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6;");
        return b;
    }
}