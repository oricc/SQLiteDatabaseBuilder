package application;

import java.io.File;

/**
 * Sample Skeleton for 'TableBuilder.fxml' Controller Class
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import codeGeneration.CodeGenerator;
import codeGeneration.CodeGeneratorCallback;
import customUnits.ColumnListCell;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import models.Database;
import models.SQLTableColumn;
import models.Table;
import utils.ColType;
import utils.SQLNameValidator;

public class TableBuilderController implements CodeGeneratorCallback {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML
	private TextField tfDatabaseName;

	@FXML // fx:id="tableListView"
	private ListView<String> tableListView; // Value injected by FXMLLoader

	@FXML
	private Button btnDeleteTable;

	@FXML
	private TextField userValue;

	@FXML
	private ContextMenu userValueMenu;

	@FXML
	private MenuItem userValueMenuClear;

	@FXML
	private Accordion tablesAccordion;

	protected ArrayList<String> tableNames;

	protected ListProperty<String> listProperty = new SimpleListProperty<>();

	// MenuBar items
	@FXML
	MenuItem miGenerateCode;

	@FXML
	// This method is called by the FXMLLoader when initialisation is complete
	void initialize() {
		try {
			setListValues();

			setListeners();
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("An unkown error");
			a.setHeaderText("An unknown error accurred, please send a report.");
			a.setContentText(e.getStackTrace().toString());
		}

	}

	private void setListValues() {
		tableNames = new ArrayList<String>();

		tableListView.itemsProperty().bind(listProperty);

		// This does not work, you can not directly add to a ListProperty
		// listProperty.addAll( asianCurrencyList );
		listProperty.set(FXCollections.observableArrayList(tableNames));

	}

	private void setListeners() {

		tableListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// Your action here
				System.out.println("Selected item: " + newValue);
				if (newValue == null)
					return;
				tablesAccordion.getPanes().get(tableNames.indexOf(newValue)).setExpanded(true);
				btnDeleteTable.setText("Delete " + newValue);

			}
		});

		btnDeleteTable.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				int selectedIdx = tableListView.getSelectionModel().getSelectedIndex();

				if (selectedIdx == -1) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No table selected");
					alert.setHeaderText("You must select a table to delete");
					alert.showAndWait();
				}

				tableNames.remove(selectedIdx);
				listProperty.set(FXCollections.observableArrayList(tableNames));

				tablesAccordion.getPanes().remove(selectedIdx);

				btnDeleteTable.setText("Delete");
			}
		});

		userValueMenuClear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				userValue.clear();
			}
		});

		userValue.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case ENTER:
					String name = userValue.getText();
					if (!checkTableName(name)) {
						return;
					} else {

						TitledPane dynamic = new TitledPane();
						dynamic.setText(name);
						addColumnControlsToPane(dynamic);
						dynamic.setText(name);
						tablesAccordion.getPanes().add(dynamic);
						addValueToList();
					}
					break;
				default:
					break;
				}
			};
		});

		tablesAccordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			@Override
			public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue,
					TitledPane newValue) {
				if (newValue != null) {
					int idx = tablesAccordion.getPanes().indexOf(newValue);
					tableListView.getSelectionModel().select(idx);
				}

			}
		});

		miGenerateCode.setOnAction(event -> generateJavaCode());

	}

	private void addValueToList() {
		tableNames.add(userValue.getText());
		listProperty.set(FXCollections.observableArrayList(tableNames));
		userValue.clear();
	}

	private void addColumnControlsToPane(TitledPane tp) {

		VBox vbox = new VBox();

		HBox hbox = new HBox();
		hbox.setSpacing(10);

		TextField colName = new TextField();
		colName.setPromptText("Column Name");

		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setPromptText("Column Type");
		comboBox.getItems().addAll("INTEGER", "REAL", "TEXT", "BLOB");

		CheckBox notNull = new CheckBox("Not Null");
		Button btnAdd = new Button("Add Column");

		hbox.getChildren().addAll(colName, comboBox, notNull, btnAdd);

		ListView<SQLTableColumn> colList = new ListView<>();
		colList.setId("paneColList");
		colList.setCellFactory(param -> new ColumnListCell());
		colList.getItems().add(new SQLTableColumn("ID", ColType.INTEGER, true));
		colList.setMaxHeight(100);

		vbox.getChildren().addAll(hbox, new Separator(), colList);

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				addColumnToList(colName, comboBox, notNull, colList);
				colName.requestFocus();
			}
		});

		btnAdd.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				addColumnToList(colName, comboBox, notNull, colList);
				colName.requestFocus();
			}
		});

		notNull.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				notNull.setSelected(!notNull.isSelected());
			}
		});

		colName.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {

					addColumnToList(colName, comboBox, notNull, colList);
				}
			}
		});

		tp.setContent(vbox);
	}

	private void addColumnToList(TextField colName, ComboBox<String> comboBox, CheckBox notNull,
			ListView<SQLTableColumn> colList) {

		boolean check = checkColName(colName.getText(), colList);
		if (!check)
			return;

		try {
			SQLTableColumn col = new SQLTableColumn(colName.getText(),
					ColType.getTypeByString(comboBox.getSelectionModel().getSelectedItem()), notNull.isSelected());
			colList.getItems().add(col);
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Type Specified");
			alert.setHeaderText("You must choose a type for the new column");
			alert.showAndWait();
			return;
		}

		// clear
		colName.clear();
		comboBox.getSelectionModel().clearSelection();
		notNull.setSelected(false);

	}

	private boolean checkColName(String text, ListView<SQLTableColumn> list) {
		if (!SQLNameValidator.isValid(text.toUpperCase())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Illegal name");
			alert.setHeaderText("The column name is illegal");
			alert.showAndWait();
			return false;
		}

		ObservableList<SQLTableColumn> cols = list.getItems();
		for (int i = 0; i < cols.size(); i++) {
			if (cols.get(i).getName().toUpperCase().equals(text.toUpperCase())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Name Already Exists");
				alert.setHeaderText("The column name is already in the table");
				alert.showAndWait();
				return false;
			}
		}

		return true;
	}

	private boolean checkTableName(String text) {

		if (!SQLNameValidator.isValid(text.toUpperCase())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Illegal name");
			alert.setHeaderText("The table name is illegal");
			alert.showAndWait();
			return false;
		}

		ObservableList<String> cols = tableListView.getItems();
		for (int i = 0; i < cols.size(); i++) {
			if (cols.get(i).toUpperCase().equals(text.toUpperCase())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Name Already Exists");
				alert.setHeaderText("The table name is already in the database");
				alert.showAndWait();
				return false;
			}
		}

		return true;

	}

	@SuppressWarnings("unchecked")
	private void generateJavaCode() {

		if (!validateDatabaseInfo())
			return;

		Database db = new Database(tfDatabaseName.getText());
		for (TitledPane tp : tablesAccordion.getPanes()) {
			ListView<SQLTableColumn> colsView = (ListView<SQLTableColumn>) tp.getContent().lookup("#paneColList");
			Table current = new Table(tp.getText());
			if (colsView.getItems().size() == 1) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Table " + tp.getText() + " has no columns");
				a.setHeaderText("Please give the table at least one column or delete it.");
				a.showAndWait();
				return;
			}
			for (SQLTableColumn col : colsView.getItems()) {
				current.add(col);
			}
			db.add(current);
		}

		CodeGenerator generator = new CodeGenerator(db, this);
		generator.generateCode();
	}

	private boolean validateDatabaseInfo() {
		// validate db name
		if (tfDatabaseName.getText() == null || tfDatabaseName.getText().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Missing Database Name");
			a.setHeaderText("Please give a name for your database");
			a.showAndWait();
			return false;
		} else if (!SQLNameValidator.isValid(tfDatabaseName.getText())) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Database name is invalid");
			a.setHeaderText("Please give a valid name for your database");
			a.showAndWait();
			return false;
		}
		// make sure there are tables
		if (tableNames.isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("No Tables");
			a.setHeaderText("Please create at least one table");
			a.showAndWait();
			return false;
		}
		return true;
	}

	@Override
	public File showSaveDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a location to save the generated files");
		fileChooser.setInitialFileName("databaseFolder");
		File f = fileChooser.showSaveDialog(btnDeleteTable.getScene().getWindow());
		return f;
	}

	@Override
	public void onGenerationComplete(String loc) {
		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("Code generation complete");
		a.setHeaderText("The code has been generated successfully");
		a.setContentText("The generated files are located at:\n" + loc);
		a.showAndWait();
	}

}
