import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TableScreen extends BorderPane {
	static ScrollPane sp = new ScrollPane();
	static GridPane gp = new GridPane();

	public TableScreen(Stage primaryStage, Scene scene) {
		resetTableScreen();
		setCenter(sp);
	}

	public static void resetTableScreen() {
		gp.getChildren().clear();
		Button displayTableBtn = new Button("Display Table");
		gp.add(displayTableBtn, 0, 0);
		gp.setAlignment(Pos.CENTER);
		sp.setContent(gp);
		displayTableBtn.setOnAction(e -> {
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
			} else if (HomeScreen.resultList == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("No result.");
				alert.setContentText("You need to calculate the result first");
				alert.show();
				return;
			}

			updateTableContent();
		});
		setAlignment(sp, Pos.CENTER);
	}

	public static void updateTableContent() {
		gp.getChildren().clear();
		int z = 1;
		for (int i = 0; i < HomeScreen.n; i++) {
			Rectangle rec = new Rectangle(40, 40);
			Label l = new Label(z + "");
			rec.setStyle("-fx-stroke: black; -fx-stroke-width: 1; -fx-fill: white;");
			StackPane sp = new StackPane(rec, l);
			sp.setStyle("-fx-border-color: #7752FE; -fx-border-width: 1;");
			gp.add(sp, 0, i + 4);
			z++;
		}

		for (int i = 0; i < HomeScreen.n; i++) {
			Rectangle rec2 = new Rectangle(40, 40);
			Label l2 = new Label(HomeScreen.leds[i] + "");
			rec2.setStyle("-fx-stroke: black; -fx-stroke-width: 1; -fx-fill: white;");
			StackPane sp2 = new StackPane(rec2, l2);
			sp2.setStyle("-fx-border-color: #7752FE; -fx-border-width: 1;");
			gp.add(sp2, i + 4, 0);

		}
		for (int i = 0; i < HomeScreen.n + 1; i++) {
			for (int j = 0; j < HomeScreen.n + 1; j++) {
				Rectangle rec = new Rectangle(40, 40);
				Label l = new Label(HomeScreen.dp[i][j] + "");
				rec.setStyle("-fx-stroke: black; -fx-stroke-width: 1; -fx-fill: #C2D9FF;");
				StackPane sp = new StackPane(rec, l);
				sp.setStyle("-fx-border-color: #7752FE; -fx-border-width: 1;");
				gp.add(sp, i + 3, j + 3);
			}
		}

		gp.setVgap(0);
		gp.setHgap(0);
		gp.setAlignment(Pos.CENTER);
		sp.setContent(gp);
		setAlignment(sp, Pos.CENTER);
	}

}
