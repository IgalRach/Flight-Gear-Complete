package viewModel;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import interpreter.Interpreter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import matrix.Matrix;
import models.AirplaneListenerModel;
import models.ConnectModel;
import models.MatrixModel;
import models.Property;

public class ViewModel extends Observable implements Observer {

	MatrixModel matrixModel;
	AirplaneListenerModel airplaneModel;
	ConnectModel connectModel;
	Interpreter interpreterModel;

	public Property<String> ipSimulator;
	public Property<String> portSimulator;
	public StringProperty shortestPath;
	public BooleanProperty isConnectedToSolver;

	public IntegerProperty airplanePositionX;
	public IntegerProperty airplanePositionY;

	public Property<Matrix> propertyMat;
	public Property<String[]> csv;

	public StringProperty fileName;


	public Property<Point> startPosition;
	public Property<Point> exitPosition;


// Controllers
	public DoubleProperty aileron;
	public DoubleProperty elevator;
	public DoubleProperty throttle;
	public DoubleProperty rudder;

//Server
	public Property<String> ipSolver;
	public Property<String> portSolver;



	public ViewModel(MatrixModel matrixModel, AirplaneListenerModel airplaneModel, ConnectModel connectModel) {
		this.matrixModel = matrixModel;
		this.airplaneModel = airplaneModel;
		this.connectModel = connectModel;
		airplanePositionX = new SimpleIntegerProperty();
		airplanePositionY = new SimpleIntegerProperty();
		shortestPath = new SimpleStringProperty();
		fileName = new SimpleStringProperty();

		// Controls
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
		rudder = new SimpleDoubleProperty();
		throttle = new SimpleDoubleProperty();

		propertyMat = new Property<>();
		csv = new Property<>();
		ipSimulator = new Property<>();
		portSimulator = new Property<>();
		ipSolver = new Property<>();
		portSolver = new Property<>();
		startPosition = new Property<>();
		exitPosition = new Property<>();

		isConnectedToSolver = new SimpleBooleanProperty();
	}


	//Setters
	public void setExitPosition() {
		matrixModel.setExitPosition(exitPosition.get());
	}


	//Send
	public void sendRudderValues() {
		connectModel.sendCommandToSimulator("set /controls/flight/rudder ", rudder.get());
	}

	public void sendAileronValues() {
		connectModel.sendCommandToSimulator("set /controls/flight/aileron ", aileron.get());
	}

	public void sendThrottleValues() {
		connectModel.sendCommandToSimulator("set /controls/engines/current-engine/throttle ", throttle.get());
	}

	public void sendElevatorValues() {
		connectModel.sendCommandToSimulator("set /controls/flight/elevator ", elevator.get());
	}


	public void buildMatrix(int rows, int cols) {
		matrixModel.buildMatrix(csv.get(), rows, cols);
	}

	//Connect
	public void connectToSimulator() {
		connectModel.connect(ipSimulator.get(), Integer.parseInt(portSimulator.get()));
	}

	public void connectToSolver() {
		matrixModel.connect(ipSolver.get(), Integer.parseInt(portSolver.get()));
	}

	public void connectToServer(String server) {
		switch (server) {
			case "FlightGear Simulator":
				connectToSimulator();
				break;
			case "Path Calculator":
				connectToSolver();
				break;
		}
	}

	public void calculatePath() {
		matrixModel.getPathFromServer();
	}

	public void interpret() {
		interpreterModel = new Interpreter();
		new Thread(() -> interpreterModel.interpret(Interpreter.lexer(fileName.get()))).start();
	}

	@Override
	public void update(Observable o, Object arg) {
		String data = (String) arg;
		if (o == airplaneModel) {
			this.airplanePositionX.set(airplaneModel.getAirplanePosition().x);
			this.airplanePositionY.set(airplaneModel.getAirplanePosition().y);
			matrixModel.setStartPosition(new Point(airplanePositionX.get(), airplanePositionY.get()));
			setChanged();
			notifyObservers("airplane");
		}
		if (o == matrixModel) {
			if (data.equals("matrix")) {
				propertyMat.set(matrixModel.getMatrix());
				airplaneModel.setStartX(matrixModel.getStartCooX());
				airplaneModel.setStartY(matrixModel.getStartCooY());
				setChanged();
				notifyObservers("matrix");
			}
			if (data.equals("shortest path")) {
				this.shortestPath.set(matrixModel.getShortestPath());
				this.isConnectedToSolver.set(true);
				setChanged();
				notifyObservers("shortest path");
			}

			if (data.equals("not connected")) {
				this.isConnectedToSolver.set(false);
				setChanged();
				notifyObservers("not connected");
			}
		}
	}

}