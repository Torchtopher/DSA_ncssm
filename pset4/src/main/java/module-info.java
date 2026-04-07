module edu.ncssm.bsea.pset4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires tools.jackson.databind;

    opens edu.ncssm.briansea.pset4 to javafx.fxml;
    exports edu.ncssm.briansea.pset4;
}