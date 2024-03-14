import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import atlantafx.base.theme.PrimerLight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeScreen extends BorderPane {

	Label numberOfLightsL = new Label("Enter the number of LEDs (n >= 1):");
	Label customTestCaseL = new Label("Enter a custom test case seperated by a space (Optional):");
	Label importTestCaseL = new Label("Import a custom test case (space seperated):");
	Label resultL = new Label("The maximum number of lights that can be turned on at once =");
	TextField numberOfLightsTF = new TextField();
	TextField customTestCaseTF = new TextField();
	TextField resultTF = new TextField();

	Button importTestCaseBtn = new Button("Choose File");
	Button generateTestCaseBtn = new Button("Generate Test Case");
	Button displayTestCaseBtn = new Button("Display Test Case");
	Button calculateResultBtn = new Button("Calculate Result");
	Button displayResultBtn = new Button("Display LEDs Result");
	Button enterBtn = new Button("Enter Test Case");

	static int[] leds;
	static int n;
	static LinkedList<Integer> resultList = new LinkedList<Integer>();
	static int[][] dp;
	static int[][] direction;

	public HomeScreen(Stage primaryStage, Scene scene) {
		customTestCaseL.setTextFill(Color.RED);
		GridPane topGP = new GridPane();
		initializeTopGP(topGP);

		HBox hb = new HBox(generateTestCaseBtn, displayTestCaseBtn, calculateResultBtn);
		hb.setSpacing(25);
		hb.setAlignment(Pos.CENTER);

		GridPane bottomGP = new GridPane();
		initializeBottomGP(bottomGP);

		VBox mainVB = new VBox(topGP, bottomGP, hb);
		mainVB.setSpacing(15);
		resultTF.setEditable(false);

		numberOfLightsTF.setOnKeyReleased(e -> {
			checkInputOfn(e);
		});
		customTestCaseTF.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.SPACE)
				checkInputOfCustomTestCase(e);
		});

		enterBtn.setOnAction(e -> {
			checkTestCase(customTestCaseTF.getText().trim());
		});

		importTestCaseBtn.setOnAction(e -> {
			importTestCase(primaryStage);
		});

		generateTestCaseBtn.setOnAction(e -> {
			generateTestCase();
		});

		displayTestCaseBtn.setOnAction(e -> {
			displayTestCase();
		});

		calculateResultBtn.setOnAction(e -> {
			calculateResult();
		});

		displayResultBtn.setOnAction(e -> {
			displayResult();
		});

		setCenter(mainVB);

	}

	public void checkInputOfCustomTestCase(KeyEvent e) {
		if (e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.LEFT && e.getCode() != KeyCode.RIGHT)
			try {
				HashSet<Integer> set = new HashSet<>();
				String[] lights = customTestCaseTF.getText().trim().split(" ");
				if (lights.length > n) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Invalid number of lights.");
					alert.setContentText("The number of lights cannot exceed the number of power sources, (1 - n)");
					alert.show();
					return;
				}
				int input = 0;
				for (int i = 0; i < lights.length; i++) {
					input = Integer.parseInt(lights[i]);
					if (input > n) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setHeaderText("Invalid light input.");
						alert.setContentText("The light number cannot exceed the number of power sources, (1 - n)");
						alert.show();
						return;
					} else if (input == 0) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setHeaderText("Invalid light input.");
						alert.setContentText("The light number cannot be zero, (1 - n)");
						alert.show();
						return;
					}
					if (!set.add(input)) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setHeaderText("Invalid light input.");
						alert.setContentText("There cannot be duplicate light values!");
						alert.show();
						return;
					}
				}
			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Invalid value of n.");
				alert.setContentText("n must an integer that is greater or equal to 1.");
				alert.show();
				numberOfLightsTF.clear();
//					e1.printStackTrace();
			}
	}

	private void checkInputOfn(KeyEvent e) {
		if (e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.LEFT && e.getCode() != KeyCode.RIGHT)
			try {
				String string = numberOfLightsTF.getText().trim();
//				System.out.println(string);
				n = Integer.parseInt(string);
			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Invalid value of n.");
				alert.setContentText("n must an integer that is greater or equal to 1.");
				alert.show();
				numberOfLightsTF.clear();
//				e1.printStackTrace();
			}

	}

	private void importTestCase(Stage primaryStage) {
		FileChooser fc = new FileChooser();
		int temp = n;
		try {
			File f = fc.showOpenDialog(primaryStage);
			Scanner sc = new Scanner(f);
			n = sc.nextInt();
			StringBuilder testcase = new StringBuilder("");
			while (sc.hasNext()) {
				testcase.append(sc.next() + " ");
			}
			if (testcase.length() != 2 * n) { // spaces and numbers
				n = temp;
				throw new InputMismatchException();
			}
			checkTestCase(testcase.toString().trim());
			numberOfLightsTF.setText(n + "");
		} catch (InputMismatchException e1) {
			n = temp;
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Invalid File Format!");
			alert.setContentText(
					"The file should contain the number of lights on the first line and the light values on the second line seperated by a space.");
			alert.show();

		} catch (Exception e2) {
			n = temp;
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("File Not Found!");
			alert.setContentText("Please choose a file containing the test case.");
			alert.show();
		}
	}

	private void generateTestCase() {
		if (n == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Value of n not specified.");
			alert.setContentText("You need to enter the value of n");
			alert.show();
			return;
		}
		HashSet<Integer> set = new HashSet<Integer>();
		Random rand = new Random();
		leds = new int[n];
		for (int i = 0; i < n; i++) {
			int num = rand.nextInt(n) + 1;
			while (!set.add(num)) {
				num = rand.nextInt(n) + 1;
			}
//				System.out.print(num + " ");
			leds[i] = num;
		}
		resultList = null;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success!");
		alert.setHeaderText("Test Case Generated!");
		alert.setContentText("Press ok.");
		alert.show();
	}

	private void displayTestCase() {
		if (leds == null || n == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("No test case entered!");
			alert.setContentText("Please enter or import a custom test case.");
			alert.show();
			return;
		}
		StringBuilder testcase = new StringBuilder("");
		for (int i = 0; i < n; i++) {
			testcase.append(leds[i] + " ");
		}

		BorderPane bp = new BorderPane();
		TextArea caseArea = new TextArea();
		caseArea.setText(testcase.toString());
		caseArea.setWrapText(true);
		caseArea.setEditable(false);
		bp.setCenter(caseArea);
		bp.setMargin(caseArea, new Insets(15));
		Label caseL = new Label("Current Test Case");
		bp.setTop(caseL);
		bp.setAlignment(caseL, Pos.CENTER);
		Stage caseStage = new Stage();
		Scene caseScene = new Scene(bp, 400, 400);
		caseScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
		caseScene.getStylesheets().add("style.css");
		caseStage.setScene(caseScene);
		caseStage.show();
	}

	private void displayResult() {
		if (n == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Value of n not specified.");
			alert.setContentText("You need to enter the value of n");
			alert.show();
			return;
		} else if (resultList == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("No result.");
			alert.setContentText("You need to calculate the result first");
			alert.show();
			return;
		}
		BorderPane bp = new BorderPane();
		TextArea resultArea = new TextArea();
		StringBuilder res = new StringBuilder("");
		for (Integer i : resultList) {
			res.append(i + " ");
		}
		resultArea.setText(res.toString());
		resultArea.setWrapText(true);
		bp.setCenter(resultArea);
		bp.setMargin(resultArea, new Insets(15));
		Label resL = new Label(
				"Maximum LEDs powered at once (other combinations might exist but the maximum number of leds is the same)");
		bp.setTop(resL);
		bp.setAlignment(resL, Pos.CENTER);
		Stage caseStage = new Stage();
		Scene caseScene = new Scene(bp, 1100, 500);
		caseScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
		caseScene.getStylesheets().add("style.css");
		caseStage.setScene(caseScene);
		caseStage.setTitle("LEDs Result");
		caseStage.show();
	}

	private void calculateResult() {
		if (n == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Value of n not specified.");
			alert.setContentText("You need to enter the value of n");
			alert.show();
			return;
		} else if (leds == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("No test case");
			alert.setContentText("Please generate or enter a test case");
			alert.show();
			return;
		}
		
		
	
		// dp relation: dp[i][j] = 0 if (i = 0) or ( j = 0 ), dp[i,j] = dp[i-1,j-1] + 1 if xi = yj , max(dp[i-1, j],dp[i,j-1]) if xi != yj
		dp = new int[n + 1][n + 1];
		direction = new int[n + 1][n + 1]; // array to save dp table direction
		for (int i = 1; i < dp.length; i++) { // O(n^2)
			for (int j = 1; j < dp[i].length; j++) {
				if (leds[i - 1] == j) { // compare with j since we don't have to store the power sources from 1 to n (waste of memory)
					dp[i][j] = dp[i - 1][j - 1] + 1;
					direction[i][j] = 1; // diagonal
				} else {
					if (dp[i - 1][j] > dp[i][j - 1]) {
						dp[i][j] = dp[i - 1][j];
						direction[i][j] = 2; // up
					} else {
						dp[i][j] = dp[i][j - 1];
						direction[i][j] = 3; // left
					}

				}

			}
		}

		int i = n;
		int j = n;
		int length = dp[n][n];
		int[] result = new int[length];
		while (length > 0) {
			if (direction[i][j] == 1) { // go diagonal
				result[length - 1] = leds[i - 1];
				i--;
				j--;
				length--;
			} else if (direction[i][j] == 2) { // go up
				i--;
			} else if (direction[i][j] == 3) { // go left
				j--;
			}

		}

		resultList = new LinkedList<>();
		resultList.clear();
		for (int t = 0; t < result.length; t++) {
			resultList.addLast(result[t]);
		}
		resultTF.setText(dp[n][n] + "");
		TableScreen.resetTableScreen();
		ResultScreen.resetResultScreen();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success!");
		alert.setHeaderText("The results are ready!");
		alert.setContentText("Press ok.");
		alert.show();
	}

	private void checkTestCase(String s) {
		try {
			HashSet<Integer> set = new HashSet<>();
			String[] lights = s.trim().split(" ");
			if (lights.length > n) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Invalid number of lights.");
				alert.setContentText("The number of lights cannot exceed the number of power sources, (1 - n)");
				alert.show();
				return;
			}
			int input = 0;
			for (int i = 0; i < lights.length; i++) {
				input = Integer.parseInt(lights[i]);
				if (input > n) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Invalid light input.");
					alert.setContentText("The light number cannot exceed the number of power sources, (1 - n)");
					alert.show();
					return;
				} else if (input <= 0) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Invalid light input.");
					alert.setContentText("The light number cannot be less than n, (1 - n)");
					alert.show();
					return;
				}
				if (!set.add(input)) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Invalid light input.");
					alert.setContentText("There cannot be duplicate light values!");
					alert.show();
					return;
				}
			}
			leds = new int[lights.length];
			for (int i = 0; i < lights.length; i++) {
				leds[i] = Integer.parseInt(lights[i]);
				if (leds[i] > n)
					throw new Exception();
			}

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Test case has been loaded successfully!");
			alert.show();
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Invalid values in file");
			alert.setContentText("Please check the file format.");
			alert.show();
			numberOfLightsTF.clear();
//			e1.printStackTrace();
		}
	}

	private void initializeBottomGP(GridPane bottomGP) {
		bottomGP.add(resultL, 0, 0);
		bottomGP.add(resultTF, 1, 0);
		bottomGP.add(displayResultBtn, 1, 1);
		bottomGP.setVgap(15);
		bottomGP.setHgap(15);
		bottomGP.setPadding(new Insets(15));
	}

	private void initializeTopGP(GridPane topGP) {
		topGP.add(numberOfLightsL, 0, 0);
		topGP.add(customTestCaseL, 0, 1);
		topGP.add(importTestCaseL, 0, 2);

		topGP.add(numberOfLightsTF, 1, 0);
		topGP.add(customTestCaseTF, 1, 1);
		topGP.add(enterBtn, 2, 1);
		topGP.add(importTestCaseBtn, 1, 2);

		topGP.setPadding(new Insets(15));
		topGP.setVgap(15);
		topGP.setHgap(15);
	}

}
