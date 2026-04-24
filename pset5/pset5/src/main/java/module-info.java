module edu.ncssm.briansea.pset5 {
    requires javafx.controls;
    requires javafx.fxml;

    requires tools.jackson.databind;

    opens edu.ncssm.briansea.pset5 to javafx.fxml;
    exports edu.ncssm.briansea.pset5;
}