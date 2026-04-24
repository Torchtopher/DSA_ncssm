module edu.ncssm.chris.pset5 {
    requires javafx.controls;
    requires javafx.fxml;

    requires tools.jackson.databind;

    opens edu.ncssm.chris.pset5 to javafx.fxml;
    exports edu.ncssm.chris.pset5;
}