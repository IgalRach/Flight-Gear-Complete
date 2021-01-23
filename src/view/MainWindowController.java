package view;

import java.awt.Point;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import matrix.Matrix;
import models.Property;
import viewModel.ViewModel;

public class MainWindowController extends Window implements Initializable, Observer {

	ViewModel vm;
	PrintWriter outToSolver;
	@FXML
	Circle outerCircle;
	@FXML
	Circle innerCircle;
	@FXML
	ToggleGroup tg;
	@FXML
	MapPainter mapPainter;
	@FXML
	RadioButton manual;
	@FXML
	RadioButton autoPilot;
	@FXML
	TextArea systemLog;
	@FXML
	TextArea textArea;
	Joystick joystick;
	@FXML
	Slider rudderSlider;
	@FXML
	Slider throttleSlider;
	public StringProperty shortestPath;
	public Property<Point> exitPos;
	public Property<String> ipSolver;
	public Property<String> ipSimulator;
	public Property<String> portSimulator;
	public Property<String> portSolver;
	public BooleanProperty isConnectedToSolver;
	public Property<Matrix> propertyMat;
	public Property<String[]> csv;
	public StringProperty fileName;

	public static int rows;
	public static int cols;
	boolean isManual;
	boolean autoFlag;
	Point curAirplaneLocation;
	int airplanePosX;
	int airplanePosY;

	public String[] csvParser(File f) {
		systemLog.appendText("Parsing Map File...\n");
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			StringBuilder sb = new StringBuilder();
			String line;
			int linesCount = -1; // excluding the coordinates data
			int colsCount = 0;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				colsCount = values.length;
				for (String s : values) {
					sb.append(s + ",");
				}
				linesCount++;
			}
			rows = linesCount-1;//changed by igal
			cols = colsCount;
			return sb.toString().split(",");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setViewModel(ViewModel vm) {
		this.vm = vm;
		this.propertyMat.bindTo(vm.propertyMat);
		this.shortestPath.bind(vm.shortestPath);

		this.isConnectedToSolver.bind(vm.isConnectedToSolver);

		vm.rudder.bind(this.rudderSlider.valueProperty());
		vm.throttle.bind(this.throttleSlider.valueProperty());
		vm.aileron.bind(joystick.aileron);
		vm.elevator.bind(joystick.elevator);

		vm.csv.bindTo(this.csv);
		vm.ipSimulator.bindTo(this.ipSimulator);
		vm.portSimulator.bindTo(this.portSimulator);
		vm.ipSolver.bindTo(this.ipSolver);
		vm.portSolver.bindTo(this.portSolver);
		vm.exitPosition.bindTo(this.exitPos);
		vm.fileName.bind(this.fileName);
	}

	public void onAirplanePositionChange() {
		curAirplaneLocation = new Point(airplanePosX, airplanePosY);
		mapPainter.setAirplanePosition(curAirplaneLocation);
	}

	public void generateConnectionDialog(String[] params, Property<String> ip, Property<String> port) {
		String server = params[0];
		String title = params[1];
		String successMessage = params[2];
		Stage window = new Stage();
		GridPane grid = new GridPane();
		TextField ipInput = new TextField();
		TextField portInput = new TextField();
		Label ipLabel = new Label(server + " IP:");
		Label portLabel = new Label(server + " Port:");
		Button connectButton = new Button("Connect");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Text connect = new Text(title);
		connect.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(connect, 0, 0);
		grid.add(ipLabel, 0, 1);
		grid.add(ipInput, 1, 1);
		grid.add(portLabel, 0, 2);
		grid.add(portInput, 1, 2);
		HBox hbButton = new HBox(10);
		hbButton.setAlignment(Pos.BOTTOM_CENTER);
		hbButton.getChildren().add(connectButton);
		grid.add(hbButton, 1, 4);
		connectButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			connectButton.setCursor(Cursor.HAND);
		});
		window.setScene(new Scene(grid, 400, 250));
		window.show();
		connectButton.setOnAction(e -> {
			if (!ipInput.getText().equals("") && !portInput.getText().equals("")) {
				ip.set(ipInput.getText());
				port.set(portInput.getText());
				vm.connectToServer(server);
				systemLog.appendText(successMessage);
				window.close();
			} else {
				systemLog.appendText("Failed to connect to server.\n");
			}
		});
	}

	public void loadDataClicked() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Load Data");
		fc.setInitialDirectory(new File("./resources"));
		File selectedFile = fc.showOpenDialog(this);
		if (selectedFile != null) {
			csv.set(csvParser(selectedFile));
			vm.buildMatrix(rows, cols);
			systemLog.appendText("Loaded Map succesfully.\n");
		}
	}

	public void radioButtonClicked() {
		tg = new ToggleGroup();
		manual.setToggleGroup(tg);
		autoPilot.setToggleGroup(tg);
		if (tg.getSelectedToggle().equals(manual)) {
			systemLog.appendText("Switched to Manual Mode.\n");
			isManual = true;
			autoFlag = false;
		}
		if (tg.getSelectedToggle().equals(autoPilot)) {
			isManual = false;
			autoFlag = true;
			FileChooser fc = new FileChooser();
			fc.setTitle("Load an autoPilot pilot script");
			fc.setInitialDirectory(new File("./resources"));
			File selectedFile = fc.showOpenDialog(this);
			try {
				if (selectedFile != null) {
					Scanner sc = new Scanner(selectedFile);
					while (sc.hasNextLine()) {
						textArea.appendText(sc.nextLine());
						textArea.appendText("\n");
					}
					sc.close();
					fileName.setValue(selectedFile.getName());
					vm.interpret();
				}
			} catch (FileNotFoundException e) {
			}
			systemLog.appendText("Autopilot mode Activated.\n");
		}

	}

	public void connectClicked() {
		String[] params = { "FlightGear Simulator", "Connect to Simulator",
				"Connection established with simulator.\n" };
		generateConnectionDialog(params, this.ipSimulator, this.portSimulator);
	}

	public void calculatePathClicked() {
		String[] params = { "Path Calculator", "Calculate Path", "Connected to path calculator server.\n" };
		generateConnectionDialog(params, ipSolver, portSolver);
	}

	public void joystickClick(MouseEvent e) {
		if (isManual)
			joystick.joystickClick(e);
	}

	public void joystickMovement(MouseEvent e) {
		if (isManual) {
			joystick.joystickMovement(e);
			vm.sendElevatorValues();
			vm.sendAileronValues();
		}
	}

	public void joystickToCenter(MouseEvent e) {
		joystick.joystickToCenter(e);
		vm.sendElevatorValues();
		vm.sendAileronValues();
	}

	public void mapClicked(MouseEvent e) {
		if (mapPainter.heightMatrix != null) {
			exitPos.set(mapPainter.setRoute((e.getSceneX() - 5), (e.getSceneY() - 75)));
			vm.setExitPosition();
			vm.calculatePath();
			if (isConnectedToSolver.get()) {
				systemLog.appendText("Shortest path calculated.\n");
			}
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		propertyMat = new Property<>();
		curAirplaneLocation = new Point(0, 0);
		ipSimulator = new Property<>();
		csv = new Property<>();
		ipSolver = new Property<>();
		portSolver = new Property<>();
		joystick = new Joystick(innerCircle, outerCircle, rudderSlider, throttleSlider);
		isConnectedToSolver = new SimpleBooleanProperty();
		portSimulator = new Property<>();
		exitPos = new Property<>();
		fileName = new SimpleStringProperty();
		systemLog.setEditable(false);
		shortestPath = new SimpleStringProperty();
		manual.setSelected(true);
		autoPilot.setEffect(null);
		isManual = true;
		throttleSlider.setMin(0);
		throttleSlider.setMax(1);
		rudderSlider.setMin(-1);
		rudderSlider.setMax(1);

		joystick.rudder.valueProperty().addListener((ov, old_val, new_val) -> {
			if (isManual)
				vm.sendRudderValues();
		});
		joystick.throttle.valueProperty().addListener((ov, old_val, new_val) -> {
			if (isManual)
				vm.sendThrottleValues();
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		String data = (String) arg;
		if (data.equals("airplane")) {
			airplanePosX = vm.airplanePositionX.get();
			airplanePosY = vm.airplanePositionY.get();
			onAirplanePositionChange();
		}

		if (data.equals("matrix")) {
			mapPainter.setHeightData(propertyMat.get());
			mapPainter.setCursor(Cursor.HAND);
		}

		if (data.equals("not connected")) {
			systemLog.appendText("Please connect to a Path Calculator server.\n");
		}

		if (data.equals("shortest path")) {
			mapPainter.drawPath(shortestPath.get(), curAirplaneLocation);
		}

	}

}
