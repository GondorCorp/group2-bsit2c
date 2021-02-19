package com.oopclass.breadapp.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.models.Eveent;
import com.oopclass.breadapp.services.impl.EveentService;
import com.oopclass.breadapp.views.FxmlView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Controller
public class EveentController implements Initializable {

    @FXML
    private Label eveentId;

    @FXML
    private TextField eveentName;

    @FXML
    private TextField clientId;
    
    @FXML
    private DatePicker dor;
    
    @FXML
    private Timestamp createdAt;
     
    @FXML
    private Timestamp updatedAt;
    
    @FXML
    private ToggleGroup gender;
    
    @FXML
    private Tab aeTab;
    
    @FXML
    private Tab listTab;
    
    @FXML
    private TabPane tabPane;

    @FXML
    private RadioButton rbnotPaid;

    @FXML
    private RadioButton rbPaid;
    
    @FXML
    private Button reset;
    
    @FXML
    private Button deleteEveents;
    
    @FXML
    private Button switchAdd;
    
    @FXML
    private Button saveEveent;

    @FXML
    private TableView<Eveent> eveentTable;

    @FXML
    private TableColumn<Eveent, Long> colEveentId;

    @FXML
    private TableColumn<Eveent, String> colEventName;

    @FXML
    private TableColumn<Eveent, String> colClientId;

    @FXML
    private TableColumn<Eveent, LocalDate> colDOR;
    
    @FXML
    private TableColumn<Eveent, Timestamp> colCreatedAt;
    
    @FXML
    private TableColumn<Eveent, Timestamp> colUpdatedAt;
    
    @FXML
    private TableColumn<Eveent, String> colGender;

    @FXML
    private TableColumn<Eveent, Boolean> colEdit;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private EveentService eveentService;

    private ObservableList<Eveent> eveentList = FXCollections.observableArrayList();

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void saveEveent(ActionEvent event) {

        if (validate("Event Name", getEveentName(), "([a-zA-Z0-9.',?#]{3,30}\\s*)+")
                && validate("Client ID", getClientId(), "([0-9]{1,3}\\s*)+")
                && emptyValidation("Date of Reservation", dor.getEditor().getText().isEmpty())) {

            if (eveentId.getText() == null || "".equals(eveentId.getText())) {
                if (true) {

                    Eveent eveent = new Eveent();
                    eveent.setEveentName(getEveentName());
                    eveent.setClientId(getClientId());
                    eveent.setDor(getDor());
                    eveent.setUpdatedAt(getUpdatedAt());
                    eveent.setGender(getGender());

                    Eveent newEveent = eveentService.save(eveent);

                    saveAlert(newEveent);
                }

            } else {
                Eveent eveent = eveentService.find(Long.parseLong(eveentId.getText()));
                eveent.setEveentName(getEveentName());
                eveent.setClientId(getClientId());
                eveent.setDor(getDor());
                eveent.setUpdatedAt(getUpdatedAt());
                eveent.setGender(getGender());
                Eveent updatedEveent = eveentService.update(eveent);
                updateAlert(updatedEveent);
            }
            
            clearFields();
            loadEveentDetails();
        }

    }

    @FXML
    private void deleteEveents(ActionEvent event) {
        List<Eveent> eveents = eveentTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Delete selected Data?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            eveentService.deleteInBatch(eveents);
        }

        loadEveentDetails();
    }
    double x, y;
    @FXML
    private void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();    
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
    @FXML
    private void pressed(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();    
        x = event.getSceneX();
        y = event.getSceneY();
    }
    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();    
        stage.close();
    }
    
    @FXML
    private void min(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();    
        stage.setIconified(true);
    }
    
    @FXML
    private void employeeForm(ActionEvent event) throws IOException{
        stageManager.switchScene(FxmlView.EMPLOYEE);
    }
    
    @FXML
    private void customerForm(ActionEvent event) throws IOException{
        stageManager.switchScene(FxmlView.CUSTOMER);
    }
    
    @FXML
    private void switchEvent(ActionEvent event) throws IOException{
        tabPane.getSelectionModel().select(listTab);
    }
    
    @FXML
    private void switchAdd(ActionEvent event) throws IOException{
        tabPane.getSelectionModel().select(aeTab);
    }

    private void clearFields() {
        eveentId.setText(null);
        eveentName.clear();
        clientId.clear();
        dor.getEditor().clear();
        rbPaid.setSelected(true);
        rbnotPaid.setSelected(false);
    }

    private void saveAlert(Eveent eveent) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("Event: " + eveent.getEveentName() + " Inquired by Client ID " + eveent.getClientId() + " has been created successfully. \nEvent ID: " + eveent.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Eveent eveent) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Data updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("Event: " + eveent.getEveentName() + " with Event ID " + eveent.getId() + " has been updated successfully.");
        alert.showAndWait();
    }

    public String getEveentName() {
        return eveentName.getText();
    }

    public String getClientId() {
        return clientId.getText();
    }

    public LocalDate getDor() {
        return dor.getValue();
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public String getGender() {
        return rbPaid.isSelected() ? "Paid" : "Not Paid";
    }


    private void setColumnProperties() {
        
		 colDOR.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd-MM-yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));

        colEveentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEventName.setCellValueFactory(new PropertyValueFactory<>("eveentName"));
        colClientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        colDOR.setCellValueFactory(new PropertyValueFactory<>("dor"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colEdit.setCellFactory(cellFactory);
        tabPane.setTabMinHeight(-10);
        tabPane.setTabMaxHeight(-10);
    }

    Callback<TableColumn<Eveent, Boolean>, TableCell<Eveent, Boolean>> cellFactory
            = new Callback<TableColumn<Eveent, Boolean>, TableCell<Eveent, Boolean>>() {
        @Override
        public TableCell<Eveent, Boolean> call(final TableColumn<Eveent, Boolean> param) {
            final TableCell<Eveent, Boolean> cell = new TableCell<Eveent, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            Eveent eveent = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("Data send for editing." );
                            alert.showAndWait();
                            updateEveent(eveent);
                            tabPane.getSelectionModel().select(aeTab);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }

                private void updateEveent(Eveent eveent) {
                    eveentId.setText(Long.toString(eveent.getId()));
                    eveentName.setText(eveent.getEveentName());
                    clientId.setText(eveent.getClientId());
                    dor.setValue(eveent.getDor());
                    if (eveent.getGender().equals("Male")) {
                        rbPaid.setSelected(true);
                    } else {
                        rbnotPaid.setSelected(true);
                    }
                }
            };
            return cell;
        }
    };

    private void loadEveentDetails() {
        eveentList.clear();
        eveentList.addAll(eveentService.findAll());
        eveentTable.setItems(eveentList);
    }

    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please Select " + field);
        } else {
            if (empty) {
                alert.setContentText("Please Enter " + field);
            } else {
                alert.setContentText("Please Enter Valid " + field);
            }
        }
        alert.showAndWait();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        eveentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        loadEveentDetails();
    }
}
