package application;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

	private static HBox createHBox(final double spacing, final Node... children)
	{
		final HBox hbox = new HBox(spacing);
		hbox.getChildren().addAll(children);
		return hbox;
	}

	private static VBox createVBox(final double spacing, final Node... children) {
		final VBox vbox = new VBox(spacing);
		vbox.getChildren().addAll(children);
		return vbox;
	}
	
	private static RadioButton createRadioButton(final String text,
												 final ToggleGroup toogleGroup,
												 final boolean selected)
	{
		final RadioButton radioButton = new RadioButton(text);
		radioButton.setToggleGroup(toogleGroup);
		radioButton.setSelected(selected);
		
		return radioButton;
	}
}
