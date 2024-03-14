
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class ResultScreen extends BorderPane {
	static ScrollPane sp = new ScrollPane();
	static GridPane gp = new GridPane();
	static Button displayLedsBtn = new Button("Display LEDs");
	static Pane mainPane = new Pane(); 

	public ResultScreen(Stage primaryStage, Scene scene) {
		gp.setAlignment(Pos.CENTER);
		displayLedsBtn.setOnAction(e -> {
			displayResults();
		});
		resetResultScreen();
		setCenter(mainPane);
		setAlignment(mainPane, Pos.CENTER);
	}

	public static void resetResultScreen() {
		gp.getChildren().clear();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(gp);
		gp.add(displayLedsBtn, 0, 0);
	}

	private void displayResults() {
		if (HomeScreen.n == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Value of n not specified.");
			alert.setContentText("You need to enter the value of n");
			alert.show();
			return;
		} else if (HomeScreen.leds == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("No test case.");
			alert.setContentText("You need to enter or generate a test case");
			alert.show();
			return;
		} else if (HomeScreen.resultList == null || HomeScreen.resultList.size() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("No result.");
			alert.setContentText("You need to calculate the result first");
			alert.show();
			return;
		}
		gp.getChildren().clear();
		Image on = new Image("led-on.png");
		Image off = new Image("led-off.png");
		Image powerSourceImg = new Image("power-source.png");
		for (int i = 0; i < HomeScreen.n; i++) {
			ImageView led = new ImageView(off);
			if (HomeScreen.resultList.contains(HomeScreen.leds[i])) {
				led = new ImageView(on);
				Line line = new Line((i * 80 + 40),60,((HomeScreen.leds[i]-1) * 80) + 40 ,265); // each image has a width of 65, adding the gp margin of 15, the total width is 80, adding an offset of 40 to align the wire in the center
				line.setStroke(Color.BLACK);
				line.setStrokeWidth(3);
//				System.out.println("Led position: " + i + " Line: " + line);
				mainPane.getChildren().add(line);
			}
			Label l = new Label(HomeScreen.leds[i] + "");
			StackPane s = new StackPane(led, l);
			s.setAlignment(l, Pos.TOP_LEFT);
			led.setFitHeight(65);
			led.setFitWidth(65);
			gp.add(s, i, 0);
		}
		for (int i = 0; i < HomeScreen.n; i++) {
			ImageView power = new ImageView(powerSourceImg);
			Label l = new Label((i + 1) + "");
			StackPane s = new StackPane(power, l);
			s.setAlignment(l, Pos.TOP_LEFT);
			power.setFitHeight(65);
			power.setFitWidth(65);
			gp.add(s, i, 2);
		}
	
		gp.setVgap(100);
		gp.setHgap(15);
		sp.setContent(mainPane);
		setCenter(sp);
	}

}
