import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExportScreen extends BorderPane {
	Label exportLedsResL = new Label("Export LEDs result to a file:");
	Label exportDPtableL = new Label("Export DP Table to a file:");

	Button exportLedsResBtn = new Button("Choose File");
	Button exportDPtableBtn = new Button("Choose File");

	public ExportScreen(Stage primaryStage, Scene scene) {
		GridPane gp = new GridPane();
		gp.add(exportLedsResL, 0, 0);
		gp.add(exportDPtableL, 0, 1);

		gp.add(exportLedsResBtn, 1, 0);
		gp.add(exportDPtableBtn, 1, 1);

		gp.setVgap(15);
		gp.setHgap(15);
		gp.setPadding(new Insets(15));

		setTop(gp);

		exportLedsResBtn.setOnAction(e -> {
			exportLEDs(primaryStage);
		});

		exportDPtableBtn.setOnAction(e -> {
			exportDPtable(primaryStage);
		});

	}

	private void exportDPtable(Stage primaryStage) {
		if (HomeScreen.resultList == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("No results.");
			alert.setContentText("There are no results ready for export.");
			alert.show();
			return;
		}

		FileChooser fc = new FileChooser();
		File f = fc.showOpenDialog(primaryStage);
		PrintWriter out;
		try {
			out = new PrintWriter(f);
			for (int i = 0; i < HomeScreen.n; i++) {
				for (int j = 0; j < HomeScreen.n; j++) {
					out.print(HomeScreen.dp[i][j] + "\t");
				}
				out.println("");
			}
			out.close();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success!");
			alert.setHeaderText("Data Exported");
			alert.setContentText("Exported to file: " + f.getName());
			alert.show();
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("File Not Found!");
			alert.setContentText("Please choose a file to export to!");
			alert.show();
			return;
		}
	}

	private void exportLEDs(Stage primaryStage) {
		if (HomeScreen.resultList == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("No results.");
			alert.setContentText("There are no results ready for export.");
			alert.show();
			return;
		}
		FileChooser fc = new FileChooser();
		File f = fc.showOpenDialog(primaryStage);
		PrintWriter out;
		try {
			out = new PrintWriter(f);
			StringBuilder res = new StringBuilder("");
			for (Integer i : HomeScreen.resultList) {
				res.append(i + " ");
			}
			out.println(res.toString());
			out.close();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success!");
			alert.setHeaderText("Data Exported");
			alert.setContentText("Exported to file: " + f.getName());
			alert.show();
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("File Not Found!");
			alert.setContentText("Please choose a file to export to!");
			alert.show();
			return;
		}
	}

}
