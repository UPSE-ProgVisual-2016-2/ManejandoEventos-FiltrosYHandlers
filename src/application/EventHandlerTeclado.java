package application;


import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardUpLeftHandler;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EventHandlerTeclado extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private static final class Key{
		
		private final KeyCode keyCode;
		private final BooleanProperty pressedProperty;
		
		public Key(final KeyCode keyCode)
		{
			this.keyCode = keyCode;
			this.pressedProperty = new SimpleBooleanProperty(this, "pressed");
		}
		
		public KeyCode getKeyCode()
		{
			return keyCode;
		}
		
		public boolean isPressed()
		{
			return pressedProperty.get();
		}
		
		public void setPressed(final boolean value)
		{
			pressedProperty.set(value);
		}
		
		private void installEventHandler(final Node keyNode)
		{
			//Handler (manejador) de evento para tecla ennter, 
			//soltar tecla.
			//Otras teclas son manejadas por el padre (keyboard node handler)
			final EventHandler<KeyEvent> keyEventHandler =
					new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent event) {
							// TODO Auto-generated method stub
							//KEyCode es un enum con codigo de teclas
							if(event.getCode() == KeyCode.ENTER)
							{
								setPressed(event.getEventType() == KeyEvent.KEY_PRESSED);
								event.consume();
							}
						}
				
					};
			keyNode.setOnKeyPressed(keyEventHandler);
			keyNode.setOnKeyPressed(keyEventHandler);
		}
		
		public Node createNode()
		{
			final StackPane keyNode = new StackPane();
			//Esto permite al foco traversar a traves de los elementos
			keyNode.setFocusTraversable(true);
			installEventHandler(keyNode);
			
			//Cambia el color de fondo segun lo amarrado en la propiedad 
			final Rectangle keyBackground = new Rectangle(50, 50);
			keyBackground.fillProperty().bind(
					Bindings.when(pressedProperty)
						.then(Color.RED)
						.otherwise(Bindings.when(keyNode.focusedProperty())
								.then(Color.LIGHTGRAY)
								.otherwise(Color.WHITE)));
			
			//Da el color del contorno
			keyBackground.setStroke(Color.BLACK);
			keyBackground.setStrokeWidth(2);
			keyBackground.setArcWidth(12);
			keyBackground.setArcHeight(12);
			
			final Text keyLabel = new Text(keyCode.getName());
			keyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
			
			keyNode.getChildren().addAll(keyBackground, keyLabel);
			
			return keyNode;
		}
	}
	
	
	
}
