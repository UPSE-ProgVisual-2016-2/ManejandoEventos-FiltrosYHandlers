/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Handling event filters in draggable panel example
 */

package application;
	


import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
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


public class Main extends Application {
	//Los BooleanProperties permiten 
	//atar una propiedad a una aplicacion
	private final BooleanProperty dragModeActiveProperty =
			new SimpleBooleanProperty(this, "dragModeActive", true);
	
	@Override
	public void start(Stage primaryStage) {
		try {
			final Node loginPanel = 
					makeDraggable(createLoginPanel());
			final Node confirmationPanel = 
					makeDraggable(createConfirmationPanel());
			final Node progressPanel = 
					makeDraggable(createProgressPanel());
				
			loginPanel.relocate(0, 0);
			confirmationPanel.relocate(0, 67);
			progressPanel.relocate(0, 106);
			
			final Pane panelsPane = new Pane();
			panelsPane.getChildren().addAll(loginPanel, confirmationPanel, progressPanel);
			
			final BorderPane sceneRoot = new BorderPane();
			
			BorderPane.setAlignment(panelsPane, Pos.TOP_LEFT);
			sceneRoot.setCenter(panelsPane);
			
			final CheckBox dragModeCheckbox = new CheckBox("Modo de Arrastre");
			//Insets permite definir los 4 lados de un contenedor (norte, sur, este, oeste)
			BorderPane.setMargin(dragModeCheckbox, new Insets(6));
			sceneRoot.setBottom(dragModeCheckbox);

			//Amarrando la propiedad global a la del checkbox
			dragModeActiveProperty.bind(dragModeCheckbox.selectedProperty());
		
			final Scene scene = new Scene(sceneRoot, 400, 300);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Filtros con paneles moviles");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Node makeDraggable(final Node node){
		final DragContext dragContext = new DragContext();
		final Group wrapGroup = new Group(node);
		
		wrapGroup.addEventFilter(MouseEvent.ANY, 
				e -> {
					if(dragModeActiveProperty.get())
					{
						//Filtra cualquier evento del mouse
						//hacia sus hijos
						e.consume();
					}
				});
		
		wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent mouseEvent) {
						if(dragModeActiveProperty.get())
						{
							//Guardar las coordinadas iniciales
							//de la posicion del mouse.
							dragContext.mouseAnchorX = mouseEvent.getX();
							dragContext.mouseAnchorY = mouseEvent.getY();
							dragContext.initialTranslateX = node.getTranslateX();
							dragContext.initialTranslateY = node.getTranslateY();
						}
					}
				});
		
		wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED,
				e -> {
					if(dragModeActiveProperty.get())
					{
						node.setTranslateX(dragContext.initialTranslateX
								+ e.getX() - dragContext.mouseAnchorX);
						node.setTranslateY(dragContext.initialTranslateY
								+ e.getY() - dragContext.mouseAnchorY);
					}
				});
		
		return wrapGroup;
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
		
		configureBorder(panel);
		return panel;
	}
	
	private static Node createConfirmationPanel()
	{
		final Label acceptanceLabel = new Label("No Disponible");
		
		final Button acceptButton = new Button("Aceptar");
		acceptButton.setOnAction( e -> acceptanceLabel.setText("Aceptado"));
		
		final Button declineButton = new Button("Rechazar");
		declineButton.setOnAction( e -> acceptanceLabel.setText("Rechazado"));
		
		final HBox panel = createHBox(6, acceptButton, declineButton, acceptanceLabel);
		panel.setAlignment(Pos.CENTER_LEFT);
		
		configureBorder(panel);
		return panel;
	}
	
	private static Node createProgressPanel()
	{
		final Slider slider = new Slider();
		
		final ProgressIndicator progressIndicator = new ProgressIndicator(0);
		progressIndicator.progressProperty().bind(
				Bindings.divide(slider.valueProperty(), slider.maxProperty()));
		
		final HBox panel = createHBox(6, 
										new Label("Progreso: "),
										slider,
										progressIndicator);
		configureBorder(panel);
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
	
	private static void configureBorder(final Region region)
	{
		region.setStyle("-fx-background-color: white;"
							+ "-fx-border-color: black;"
							+ "-fx-border-width: 1;"
							+ "-fx-border-radius: 6;"
							+ "-fx-padding: 6;");
	}
	
	private static final class DragContext {
		public double mouseAnchorX;
		public double mouseAnchorY;
		public double initialTranslateX;
		public double initialTranslateY;
	}
}
