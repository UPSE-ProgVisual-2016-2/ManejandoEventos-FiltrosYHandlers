package application;

import javafx.stage.Stage;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Main2 extends Application{

	private final BooleanProperty dragModeActiveProperty =
			new SimpleBooleanProperty(this, "dragModeActive", true );
	
	@Override
	public void start(Stage primaryStage) {
	
		final Node loginPanel = makeDraggable(createLoginPanel());
		final Node progressPanel = makeDraggable(createProgressPanel());
		final Node confirmationPanel = makeDraggable(createConfirmationPanel());
		
		loginPanel.relocate(0, 0);
		confirmationPanel.relocate(0, 67);
		progressPanel.relocate(0, 106);
		
		final Pane panelsPane = new Pane();
		panelsPane.getChildren().addAll(confirmationPanel, progressPanel, loginPanel);
		
		final BorderPane sceneRoot = new BorderPane();
		sceneRoot.setCenter(panelsPane);
		
		final CheckBox dragModeCheckbox = new CheckBox("Arrastrar Paneles");
		sceneRoot.setBottom(dragModeCheckbox);
		BorderPane.setMargin(dragModeCheckbox, new Insets(6));
		
		dragModeActiveProperty.bind(dragModeCheckbox.selectedProperty());
		
		final Scene scene = new Scene(sceneRoot, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Ejemplo filtros arrastrables");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Node makeDraggable(final Node node)
	{
		final DragContext dragContext = new DragContext();
		final Group wrapGroup = new Group(node);
		
		wrapGroup.addEventFilter(MouseEvent.ANY, 
				e -> { if(dragModeActiveProperty.get())
						{
							e.consume();
						}
				});
		
		wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, 
				e -> {
						if(dragModeActiveProperty.get())
						{
							dragContext.mouseAnchorX = e.getX();
							dragContext.mouseAnchorY = e.getY();
							dragContext.initialTranslateX = node.getTranslateX();
							dragContext.initialTranslateY = node.getTranslateY();
						}
				});
		
		wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, 
				new EventHandler<MouseEvent>(){

					@Override
					public void handle(MouseEvent event) {
						if(dragModeActiveProperty.get())
						{
							node.setTranslateX(
									dragContext.initialTranslateX
									+ event.getX()
									- dragContext.mouseAnchorX);
							node.setTranslateY(
									dragContext.initialTranslateY
									+ event.getY()
									- dragContext.mouseAnchorY);
						}
					}
			
		});
		
		return wrapGroup;
	}
	
	private static Node createLoginPanel()
	{
		final ToggleGroup toggleGroup = new ToggleGroup();
		
		final TextField textUsername = new TextField();
		textUsername.setPrefColumnCount(10);
		textUsername.setPromptText("Su nombre de Usuario");
		
		final PasswordField passwordField = new PasswordField();
		passwordField.setPrefColumnCount(10);
		passwordField.setPromptText("Su contrasena");
		
		final ChoiceBox<String> choiceBox = new ChoiceBox<String>(
				FXCollections.observableArrayList("English", "Espanol", "Francais"));
		choiceBox.setTooltip(new Tooltip("Su idioma"));
		choiceBox.getSelectionModel().select(1);
		
		final VBox radioVBox = createVBox(2, 
											createRadioButton("Alto", toggleGroup, true),
											createRadioButton("Medio", toggleGroup, false),
											createRadioButton("Bajo", toggleGroup, false));
		
		final HBox panel = createHBox(6, 
										radioVBox,
										createVBox(2, textUsername, passwordField),
										choiceBox);
		
		panel.setAlignment(Pos.BOTTOM_LEFT);
		configureBorder(panel);
		return panel;
	}
	
	private static Node createConfirmationPanel()
	{
		final Label acceptanceLabel = new Label("No disponible");
		
		final Button acceptButton = new Button("Aceptar");
		acceptButton.setOnAction( e -> acceptanceLabel.setText("Aceptado"));
		
		final Button declineButton = new Button("Denegar");
		declineButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				acceptanceLabel.setText("Denegado");
				
			}
		});
		
		final HBox panel = createHBox(6,
										acceptButton,
										declineButton,
										acceptanceLabel);
		
		panel.setAlignment(Pos.CENTER_LEFT);
		configureBorder(panel);
		
		return panel;
	}
	
	private static Node createProgressPanel() 
	{
		final Slider slider = new Slider();
		
		final ProgressIndicator progressIndicator = new ProgressIndicator(0);
		progressIndicator.progressProperty().bind(
				Bindings.divide(slider.valueProperty(), 
						slider.maxProperty()));
	
		final HBox panel = createHBox(6, 
									new Label("Pogreso: "),
									slider, 
									progressIndicator);
		
		configureBorder(panel);
		
		return panel;
	}
	
	private static void configureBorder(final Region region)
	{
		region.setStyle("-fx-background-color: white;"
						+ "-fx-border-color: black;"
						+ "-fx-border-width: 1;"
						+ "-fx-border-radius: 6;"
						+ "-fx-padding: 6;");
	}
	
	private static RadioButton createRadioButton(
			final String text,
			final ToggleGroup toggleGroup,
			final boolean selected){
		
		final RadioButton radioButton = new RadioButton(text);
		radioButton.setToggleGroup(toggleGroup);
		radioButton.setSelected(selected);
		
		return radioButton;
	}
	
	private static HBox createHBox(final double spacing,
			final Node... children)
	{
		final HBox hbox = new HBox(spacing);
		hbox.getChildren().addAll(children);
		return hbox;
	}
	
	private static VBox createVBox(final double spacing,
			final Node... children)
	{
		final VBox vbox = new VBox(spacing);
		vbox.getChildren().addAll(children);
		return vbox;
	}
	
	private static final class DragContext {
		public double mouseAnchorX;
		public double mouseAnchorY;
		public double initialTranslateX;
		public double initialTranslateY;
	}
}
