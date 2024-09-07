package com.example.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

public class userController {
    @FXML
    private TableColumn<Hewan, String> colNama;

    @FXML
    private TableView<Hewan> tableView;

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblNama;

    @FXML
    private Label lblJenis;

    @FXML
    private Label lblHabitat;

    @FXML
    private Label lblPopulasi;

    @FXML
    private Label lblTanggal;

    @FXML
    private ImageView imgBiota;

    private static Connection conn = dbconn.connect();
    private static PreparedStatement pstmt;
    private static ResultSet rs;

    private ObservableList<Hewan> hewanList;

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icon.png"))));
        alert.show();
    }

    private void updatePanel(Hewan hewan) {
        lblNama.setText("Nama : " + hewan.getNama());
        lblJenis.setText("Jenis : " + hewan.getJenis());
        lblHabitat.setText("Habitat : " + hewan.getHabitat());
        lblPopulasi.setText("Populasi : " + hewan.getPopulasi());
        lblTanggal.setText("Tanggal Pengamatan : " + hewan.getTanggal());
        imgBiota.setImage(new Image(hewan.getImage()));
    }

    public void initialize() {
        try {
            hewanList = FXCollections.observableArrayList();
            String query = "SELECT * FROM hewan";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                hewanList.add(
                        new Hewan(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getString(7)
                        ));
            }

            colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            tableView.setItems(hewanList);
            tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    updatePanel(newValue);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnLogout.setOnAction(ActionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icon.png"))));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    scene.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("/stylesheet/log.css")).toExternalForm());

                    Image img = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/assets/icon.png")));
                    Stage stage = (Stage) btnLogout.getScene().getWindow();
                    stage.getIcons().add(img);
                    stage.setResizable(false);
                    stage.setTitle("AquaLife");
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}