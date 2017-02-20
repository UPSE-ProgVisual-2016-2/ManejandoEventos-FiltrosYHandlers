package application;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
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
	
	private static Node createLoginPanel(){
		//Un control que tiene Toogles y los maneja a todos.
		//Un toggle es un control que puede pasar entre estados seleccionados y no seleecionados
		final ToggleGroup toogleGroup = new ToggleGroup();
		
		final TextField textField = new TextField();
		textField.setPrefColumnCount(10);
		textField.setPromptText("Tu nombre");
		
		final PasswordField passwordField = new PasswordField();
		passwordField.setPrefColumnCount(10);
		passwordField.setPromptText("Su clave");
		
		//El combo box puede ser reescrito, el choiceBox solo muestra texto y solo se puede
		//seleccionar una opcion.
		final ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList("English", 
				"\u0420\u0443\u0441\u0441\u043a\u0438\u0439",
                "Fran\u00E7ais"));
		
		choiceBox.setTooltip(new Tooltip("Selecciona tu lenguaje"));
		choiceBox.getSelectionModel().select(0);
		
		final HBox panel =
				createHBox(6,
						createVBox(2,
								createRadioButton("Alto", toogleGroup, true),
								createRadioButton("Medio", toogleGroup, false),
								createRadioButton("Bajo", toogleGroup, false)),
						createVBox(2, textField, passwordField),
						choiceBox);
		
		panel.setAlignment(Pos.BOTTOM_LEFT);
		
		
		return panel;
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
