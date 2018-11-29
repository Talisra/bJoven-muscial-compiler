import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorStage extends Stage implements CompilerFinals{ // stage for error messages.
	private ImageView error = FileManager.makeImageView(ERROR_MESSAGE);
	private Label message = new Label();
	private HBox pane = new HBox();
	private VBox vbox = new VBox();
	private Scene scene = new Scene(vbox);
	private Button ok = new Button(OK);

	public ErrorStage(String errorMessage) { // error message should be inserted in that constructor, the stage will
												// adapt it.
		this.message.setText(errorMessage);
		this.initModality(Modality.APPLICATION_MODAL);
		vbox.getChildren().add(pane);
		vbox.getChildren().add(ok);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		pane.setAlignment(Pos.BASELINE_CENTER);
		pane.getChildren().add(message);
		message.setGraphic(error);
		this.setScene(scene);
		this.setWidth(ERROR_STAGE_WIDTH + errorMessage.length() * 4);
		this.setHeight(ERROR_STAGE_HEIGHT);
		this.setTitle(ERROR_TITLE);
		this.show();
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		ok.requestFocus();
		ok.setPrefWidth(GENERIC_BUTTON_WIDTH);
		ok.setOnAction(e -> this.close());
	}
}