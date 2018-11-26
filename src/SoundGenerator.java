import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SoundGenerator extends Stage implements CompilerFinals {
	private BorderPane pane = new BorderPane();
	private HBox buttonsPane = new HBox();
	private Scene mainScene = new Scene(pane);
	private ImageView pause_icon = FileManager.makeImageView(PLAYER_PAUSE);
	private ImageView play_icon = FileManager.makeImageView(PLAYER_PLAY);
	private ImageView stop_icon = FileManager.makeImageView(PLAYER_STOP);
	private Button play = new Button();
	private Button stop = new Button();
	private Synthesizer midiSynth;
	private Instrument[] instr;
	private MidiChannel[] mChannels;
	private double gain;
	private boolean isPlaying = true;
	// private Button close = new Button(close);

	public SoundGenerator(int volume) {
		this.initModality(Modality.APPLICATION_MODAL);
		this.setScene(mainScene);
		this.setWidth(PLAYER_STAGE_WIDTH);
		this.setHeight(PLAYER_STAGE_HEIGHT);
		this.setTitle(PLAYER_TITLE);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		play.setGraphic(play_icon);
		play.setOnAction(e -> this.playOrPause());
		stop.setGraphic(stop_icon);
		buttonsPane.setPadding(new Insets(20));
		buttonsPane.setSpacing(10);
		buttonsPane.getChildren().addAll(play,stop);
		pane.setBottom(buttonsPane);

		try {
			midiSynth = MidiSystem.getSynthesizer();
			midiSynth.open();
			// get and load default instrument and channel lists
			instr = midiSynth.getDefaultSoundbank().getInstruments();
			mChannels = midiSynth.getChannels();

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}

		setGainByVolume(volume);
	}

	public void setGainByVolume(int volume) {
		gain = (double) (0.01 * volume);
	}

	public void playOrPause() {
		if (isPlaying) {
			play.setGraphic(pause_icon);
			isPlaying = false;
		}
		else {
			play.setGraphic(play_icon);
			isPlaying = true;
		}
	}
	
	public void run() {
		this.show();
		midiSynth.loadInstrument(instr[0]);// load an instrument
		mChannels[0].controlChange(7, (int) (gain * 127.0));
		mChannels[0].noteOn(50, 100);// On channel 0, play note number 60 with velocity 100
		try {
			Thread.sleep(100); // wait time in milliseconds to control duration
		} catch (InterruptedException e) {
		}
		mChannels[0].noteOff(60);// turn of the note
	}

	public void playVolume() {
		midiSynth.loadInstrument(instr[0]);
		mChannels[0].controlChange(7, (int) (gain * 127.0));
		mChannels[0].noteOn(80, 1000);
	}
}
