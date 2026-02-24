module edu.ncssm.chrisholley.pset2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.ncssm.chrisholley.pset2 to javafx.fxml;
    exports edu.ncssm.chrisholley.pset2;
}