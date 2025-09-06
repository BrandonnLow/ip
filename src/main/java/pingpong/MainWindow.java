package pingpong;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Pingpong pingpong;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/Ping.JPG"));
    private final Image pingpongImage = new Image(this.getClass().getResourceAsStream("/images/Pong.JPG"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Pingpong instance */
    public void setPingpong(Pingpong p) {
        pingpong = p;
        showWelcomeMessage();
    }

    /**
     * Shows the welcome message when the application starts.
     */
    private void showWelcomeMessage() {
        String welcomeMessage = " Hello! I'm Pingpong\n What can I do for you?";
        dialogContainer.getChildren().addAll(
                DialogBox.getPingpongDialog(welcomeMessage, pingpongImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Pingpong's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = getPingpongResponse(input);

        addDialogBoxes(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPingpongDialog(response, pingpongImage)
        );
        userInput.clear();
    }

    /**
     * Gets response from Pingpong by processing the user input.
     */
    private String getPingpongResponse(String input) {
        try {
            MockUi mockUi = new MockUi();

            if (input.trim().equals("bye")) {
                return " Bye. Hope to see you again soon!";
            }

            pingpong.processCommand(input, mockUi);
            return mockUi.getOutput();

        } catch (PingpongException e) {
            return " OOPS!!! " + e.getMessage();
        }
    }

    /**
     * Adds multiple dialog boxes to the dialog container using varargs.
     */
    private void addDialogBoxes(DialogBox... dialogBoxes) {
        dialogContainer.getChildren().addAll(dialogBoxes);
    }
}
