package application;


import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardUpLeftHandler;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
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
	
	private static final class Keyboard {
		private final Key[] keys;
		
		public Keyboard(final Key...keys)
		{
			this.keys = keys.clone();
		}
		
		private Key lookupKey(final KeyCode keyCode)
		{
			for(final Key key: keys)
			{
				if(key.getKeyCode() == keyCode)
				{
					return key;
				}
			}
			return null;
		}
		
		private static Node getNextNode(final Parent parent,
				final Node node)
		{
			final Iterator<Node> childIterator =
					parent.getChildrenUnmodifiable().iterator();
			
			while(childIterator.hasNext())
			{
				if(childIterator.next() == node)
				{
					return childIterator.hasNext() ? childIterator.next() : null;
				}
			}
			
			return null;
		}
		
		private static Node getPreviousNode(final Parent parent, 
				final Node node)
		{
			final Iterator<Node> childIterator =
					parent.getChildrenUnmodifiable().iterator();
			Node lastNode = null;
			
			while (childIterator.hasNext())
			{
				final Node currentNode = childIterator.next();
				if(currentNode == node)
				{
					return lastNode;
				}
				
				lastNode = currentNode;
			}
			
			return null;
		}
		
		private static void handleFocusTraversal(final Parent traversalGroup,
				final KeyEvent keyEvent)
		{
			final Node nextFocusedNode;
			switch (keyEvent.getCode()) {
				case LEFT:
					nextFocusedNode = getPreviousNode(traversalGroup, (Node) keyEvent.getTarget());
					keyEvent.consume();
					break;
					
				case RIGHT:
					nextFocusedNode = getNextNode(traversalGroup, (Node) keyEvent.getTarget());keyEvent.consume();
					keyEvent.consume();
					break;
				
				default:
					return;
			}
			
			if (nextFocusedNode != null)
			{
				nextFocusedNode.requestFocus();
			}
		}
		
		private void installEventHandler(final Parent keyboardNode)
		{
			//Manejador para eventos keypressed y keyreleased que no
			//son manejados por key nodes
			
			final EventHandler<KeyEvent> keyEventHandler =
					new EventHandler<KeyEvent>() {
						
						@Override
						public void handle(KeyEvent event) {
							final Key key = lookupKey(event.getCode());
							if(key != null)
							{
								key.setPressed(event.getEventType() == 
										KeyEvent.KEY_PRESSED);
								event.consume();
							}
						}
					};
			
			keyboardNode.setOnKeyPressed(keyEventHandler);
			keyboardNode.setOnKeyReleased(keyEventHandler);
			
			keyboardNode.addEventHandler(KeyEvent.KEY_PRESSED, 
					new EventHandler<KeyEvent>(){

						@Override
						public void handle(KeyEvent event) {
							//Manejar el foco
							handleFocusTraversal(keyboardNode,
									event);
						}
				
			});
		}
		
		public Node createNode()
		{
			final HBox keyboardNode = new HBox(6);
			keyboardNode.setPadding(new Insets(6));
			
			final List<Node> keyboardNodeChildren = keyboardNode.getChildren();
			
			for(final Key key: keys)
			{
				keyboardNodeChildren.add(key.createNode());
			}
			
			installEventHandler(keyboardNode);
			return keyboardNode;
		}
		
	}
	
}
