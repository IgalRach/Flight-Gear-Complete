package view;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import matrix.Matrix;

public class MapPainter extends Canvas {

	int[][] heightMatrix;
	int aCol, aRow;
	int toDrawRow, toDrawCol;
	List<Point> points;
	StringProperty airplaneImage, destinationImage, pathImage;

	public MapPainter() {
		airplaneImage = new SimpleStringProperty();
		destinationImage = new SimpleStringProperty();
		pathImage = new SimpleStringProperty();
		points = new ArrayList<>();
	}

	public void setAirplaneImage(String data) {
		airplaneImage.set(data);
	}

	public void setDestinationImage(String data) {
		destinationImage.set(data);
	}

	public void setPathImage(String data) {
		pathImage.set(data);
	}

	public String getAirplaneImage() {
		return airplaneImage.get();
	}

	public String getDestinationImage() {
		return destinationImage.get();
	}

	public String getPathImage() {
		return pathImage.get();
	}

	public void setHeightData(Matrix m) { //
		heightMatrix = m.getData();
		redraw();
	}

	public void setAirplanePosition(Point pos) {
		System.out.println(pos.toString());
		aCol = pos.x;
		aRow = pos.y;
		redraw();
	}

	public Point setRoute(double clickedX, double clickedY) { // Setting destination
		double cellSizeX =(double) ((400.0) / (double) (heightMatrix[0].length));
		double cellSizeY=(double) ((400.0) / (double) (heightMatrix.length));
		toDrawCol = (int) (((clickedX) / cellSizeX));
		toDrawRow = (int) ((clickedY) / cellSizeY);
		redraw();
		return new Point(toDrawRow, toDrawCol);
	}

	public int getMaxHeight() {
		int maxHeight = 0;
		for (int i = 0; i < heightMatrix.length; i++)
			for (int j = 0; j < heightMatrix[i].length; j++)
				if (maxHeight < heightMatrix[i][j])
					maxHeight = heightMatrix[i][j];

		return maxHeight;
	}

	public void redraw() {
		if (heightMatrix != null) {
			double W = getWidth();
			double H = getHeight();
			double w = W / heightMatrix[0].length;
			double h = H / heightMatrix.length;
			int maxHeight = getMaxHeight();
			GraphicsContext gc = getGraphicsContext2D();
			Image airplane = null;
			Image destination = null;
			Image path = null;
			try {
				airplane = new Image(new FileInputStream(airplaneImage.get()));
				destination = new Image(new FileInputStream(destinationImage.get()));
				path = new Image(new FileInputStream(pathImage.get()));
			} catch (FileNotFoundException e) {
				System.out.println("file not found");
			}
			gc.clearRect(0, 0, W, H);

			for (int i = 0; i < heightMatrix.length; i++) {
				for (int j = 0; j < heightMatrix[i].length; j++) {
					gc.setFill(setColor(rgbNormalizer(heightMatrix[i][j], maxHeight)));
					gc.fillRect(j * w, i * h, w, h);

				}
			}


			//draw path
			for (Point p : points) {
				gc.drawImage(path, p.x * w, p.y * h, 3, 3);
			}
			//airplane position
			gc.drawImage(airplane, aCol * w, aRow * h, 25,25);

			//Destination Position
			if(toDrawCol!=0 && toDrawRow!=0) {
				gc.drawImage(destination, toDrawCol * w, toDrawRow * h, 22, 22); // Draw destination
			}
		}

	}

	public double[] rgbNormalizer(int height, int maxHeight) {
		double[] rg = new double[2]; // rg[0] - red, rg[1] - green
		double ratio = (double) height / maxHeight;

		if (ratio <= 0.5) {
			rg[0] = 255;
			rg[1] = 2 * ratio * 255;
		} else {
			rg[0] = (1 - ratio) * 255;
			rg[1] = 255;
		}

		return rg;
	}

	private Color setColor(double[] colors) {
		return Color.rgb((int) colors[0], (int) colors[1], 0);
	}

	public void drawPath(String shortestPath, Point current) {
		points.clear();
		String[] steps = shortestPath.split(",");
		Point prev = current;
		for (String s : steps) {
			if (s.equals("Right")) {
				prev = new Point(prev.x + 1, prev.y);
				points.add(prev);
			}
			if (s.equals("Down")) {
				prev = new Point(prev.x, prev.y + 1);
				points.add(prev);
			}
			if (s.equals("Left")) {
				prev = new Point(prev.x - 1, prev.y);
				points.add(prev);
			}

			if (s.equals("Up")) {
				prev = new Point(prev.x, prev.y - 1);
				points.add(prev);
			}
		}
		redraw();

	}

}
