import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	Font customFontRegular = Font.loadFont(Main.class.getResourceAsStream("/Product Sans Regular.ttf"), 18);
	static Font customFontBold = Font.loadFont(Main.class.getResourceAsStream("/Product Sans Bold.ttf"), 18);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TabPane tp = new TabPane();
		Tab homeTab = new Tab("Home");
		Tab dpTableTab = new Tab("DP Table");
		Tab exportTab = new Tab("Export");
		Tab resultTab = new Tab("LED Visuals");
		tp.getTabs().addAll(homeTab, dpTableTab, resultTab, exportTab);
		homeTab.setClosable(false);
		dpTableTab.setClosable(false);
		exportTab.setClosable(false);
		resultTab.setClosable(false);
		Scene scene = new Scene(tp, 920, 500);

		HomeScreen home = new HomeScreen(primaryStage, scene);
		homeTab.setContent(home);
		TableScreen tableScreen = new TableScreen(primaryStage, scene);
		dpTableTab.setContent(tableScreen);

		ExportScreen exportScreen = new ExportScreen(primaryStage, scene);
		exportTab.setContent(exportScreen);

		ResultScreen resultScreen = new ResultScreen(primaryStage, scene);
		resultTab.setContent(resultScreen);

		Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
		scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Algorithms project 1 by Omar Kashour - 1210082");
	}
}
