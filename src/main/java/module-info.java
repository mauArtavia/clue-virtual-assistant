module dev.martavia.clue {
    requires javafx.controls;
    requires javafx.fxml;

    opens dev.martavia.clue to javafx.fxml;

    exports dev.martavia.clue;
    exports dev.martavia.clue.model;
    exports dev.martavia.clue.ui;
}