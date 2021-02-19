package com.oopclass.breadapp.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.input.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.models.Employee;
import com.oopclass.breadapp.services.impl.EmployeeService;
import com.oopclass.breadapp.views.FxmlView;
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
public class EmployeeController implements Initializable {

    @FXML
    private Label employeeId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;
    
    @FXML
    private TextField address;
    
    @FXML
    private TextField contactNumber;

    @FXML
    private DatePicker dob;
    
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
    private TextField salary;

    @FXML
    private RadioButton rbFemale;

    @FXML
    private RadioButton rbMale;

    @FXML
    private Button reset;
    
    @FXML
    private Button deleteEmployees;

    @FXML
    private Button saveEmployee;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Long> colEmployeeId;

    @FXML
    private TableColumn<Employee, String> colFirstName;

    @FXML
    private TableColumn<Employee, String> colLastName;

    @FXML
    private TableColumn<Employee, String> colAddress;
    
    @FXML
    private TableColumn<Employee, String> colContactNumber;
    
    @FXML
    private TableColumn<Employee, LocalDate> colDOB;
    
    @FXML
    private TableColumn<Employee, Timestamp> colCreatedAt;
    
    @FXML
    private TableColumn<Employee, Timestamp> colUpdatedAt;
    
    @FXML
    private TableColumn<Employee, String> colSalary;
    
    @FXML
    private TableColumn<Employee, String> colGender;

    @FXML
    private TableColumn<Employee, Boolean> colEdit;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private EmployeeService employeeService;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void saveEmployee(ActionEvent event) {


        if (validate("First Name", getFirstName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Last Name", getLastName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Address", getAddress(), "([a-zA-Z0-9,.-]{1,50}\\s*)+")
                && validate("Contact Number", getContactNumber(), "([0-9]{11}\\s*)+")
                && validate("Salary", getSalary(), "([0-9,.]\\s*)+")
                && emptyValidation("Date of Birth", dob.getEditor().getText().isEmpty())) {

            if (employeeId.getText() == null || "".equals(employeeId.getText())) {
                if (true) {

                    Employee employee = new Employee();
                    employee.setFirstName(getFirstName());
                    employee.setLastName(getLastName());
                    employee.setAddress(getAddress());
                    employee.setContactNumber(getContactNumber());
                    employee.setDob(getDob());
                    employee.setUpdatedAt(getUpdatedAt());
                    employee.setSalary(getSalary());
                    employee.setGender(getGender());
                    
                    Employee newEmployee = employeeService.save(employee);

                    saveAlert(newEmployee);
                }

            } else {
                Employee employee = employeeService.find(Long.parseLong(employeeId.getText()));
                employee.setFirstName(getFirstName());
                employee.setLastName(getLastName());
                employee.setAddress(getAddress());
                employee.setContactNumber(getContactNumber());
                employee.setDob(getDob());
                employee.setUpdatedAt(getUpdatedAt());
                employee.setSalary(getSalary() + ".00");
                employee.setGender(getGender());
                Employee updatedEmployee = employeeService.update(employee);
                updateAlert(updatedEmployee);
            }
            
            clearFields();
            loadEmployeeDetails();
        }

    }

    @FXML
    private void deleteEmployees(ActionEvent event) {
        List<Employee> employees = employeeTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Delete selected Employee?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            employeeService.deleteInBatch(employees);
        }

        loadEmployeeDetails();
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
    private void customerForm(ActionEvent event) throws IOException{
        stageManager.switchScene(FxmlView.CUSTOMER);
    }
    
    @FXML
    private void eventForm(ActionEvent event) throws IOException{
        stageManager.switchScene(FxmlView.EVEENT);
    }
    
    @FXML
    private void switchEmployee(ActionEvent event) throws IOException{
        tabPane.getSelectionModel().select(listTab);
    }
    
    @FXML
    private void switchAdd(ActionEvent event) throws IOException{
        tabPane.getSelectionModel().select(aeTab);
    }

    private void clearFields() {
        employeeId.setText(null);
        firstName.clear();
        lastName.clear();
        address.clear();
        contactNumber.clear();
        dob.getEditor().clear();
        salary.clear();
        rbMale.setSelected(true);
        rbFemale.setSelected(false);
    }

    private void saveAlert(Employee employee) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("Employee: " + employee.getFirstName() + " " + employee.getLastName() + " has been created successfully \nwith " + getGenderTitle(employee.getGender()) + " Employee ID: " + employee.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Employee employee) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Data updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("Employee: " + employee.getFirstName() + " " + employee.getLastName() + " has been updated successfully.");
        alert.showAndWait();
    }

    private String getGenderTitle(String gender) {
        return (gender.equals("Male")) ? "his" : "her";
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public String getAddress() {
        return address.getText();
    }

    public String getContactNumber() {
        return contactNumber.getText();
    }

    public LocalDate getDob() {
        return dob.getValue();
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public String getSalary() {
        return salary.getText();
    }

    public String getGender() {
        return rbMale.isSelected() ? "Male" : "Female";
    }


    private void setColumnProperties() {
        
		  colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
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

        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colEdit.setCellFactory(cellFactory);
        tabPane.setTabMinHeight(-10);
        tabPane.setTabMaxHeight(-10);
    }

    Callback<TableColumn<Employee, Boolean>, TableCell<Employee, Boolean>> cellFactory
            = new Callback<TableColumn<Employee, Boolean>, TableCell<Employee, Boolean>>() {
        @Override
        public TableCell<Employee, Boolean> call(final TableColumn<Employee, Boolean> param) {
            final TableCell<Employee, Boolean> cell = new TableCell<Employee, Boolean>() {
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
                            Employee employee = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("Data send for editing." );
                            alert.showAndWait();
                            updateEmployee(employee);
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

                private void updateEmployee(Employee employee) {
                    employeeId.setText(Long.toString(employee.getId()));
                    firstName.setText(employee.getFirstName());
                    lastName.setText(employee.getLastName());
                    address.setText(employee.getAddress());
                    contactNumber.setText(employee.getContactNumber());
                    salary.setText(employee.getSalary());
                    dob.setValue(employee.getDob());
                    if (employee.getGender().equals("Male")) {
                        rbMale.setSelected(true);
                    } else {
                        rbFemale.setSelected(true);
                    }
                }
            };
            return cell;
        }
    };

    private void loadEmployeeDetails() {
        employeeList.clear();
        employeeList.addAll(employeeService.findAll());
        employeeTable.setItems(employeeList);
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

        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        loadEmployeeDetails();
    }
}
