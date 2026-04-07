module edu.ncssm.chrisholley.pset3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.ncssm.chrisholley.pset3 to javafx.fxml;
    exports edu.ncssm.chrisholley.pset3;
}