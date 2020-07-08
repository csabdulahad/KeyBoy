package keyboy.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import keyboy.KeyBoy;
import keyboy.data.Chord;
import keyboy.data.Note;
import keyboy.helper.PrefHelper;
import keyboy.helper.StageHelper;
import keyboy.musimath.MusicMath;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Main implements Initializable {

    // pattern to filter different parts of chord
    private String patternNote = "[^ABCDEFGH\\#b]";
    private String patternExtension = "[A-H\\#b]";

    // for pin
    private boolean pinStatus;
    private Image pinOn, pinOff;

    private Tooltip pinTooltip;
    private Tooltip unpinTooltip;
    private Tooltip aboutTooltip;

    private Stage stage;

    @FXML
    private ImageView pin, keyboy;

    @FXML
    private TextArea chord;

    @FXML
    private ToggleGroup shape;

    @FXML
    private RadioButton maj, min;

    @FXML
    private VBox root;

    @FXML
    private ComboBox<String> key;

    @FXML
    private Label chordFrom, chordTo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        aboutTooltip = new Tooltip("About the author");
        aboutTooltip.setStyle("-fx-font-size: 15");

        pinTooltip = new Tooltip("Pin KeyBoy window");
        pinTooltip.setStyle("-fx-font-size: 15");

        unpinTooltip = new Tooltip("Unpin KeyBoy window");
        unpinTooltip.setStyle("-fx-font-size: 15");

        pinOn = new Image(KeyBoy.class.getResource("icon/pin_on.png").toString());
        pinOff = new Image(KeyBoy.class.getResource("icon/pin_off.png").toString());

        // about tooltip
        Tooltip.install(keyboy, aboutTooltip);

        // remove initial focus from the chord box to be able to show the hint
        Platform.runLater(() -> root.requestFocus());

        loadPreviousCalculation();

        // add listener to the shape combo box
        shape.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                String shape = ((RadioButton) newValue).getId();

                // clear the current item from the key
                key.getItems().clear();

                // add new items based on shape selection
                ArrayList<String> chordList = shape.equals("maj") ? Chord.CHORD_MAJOR : Chord.CHORD_MINOR;
                key.getItems().addAll(chordList);

                // save values to preference
                PrefHelper.putString(KeyBoy.KEY_SHAPE, shape);
            }
        });


    }

    public void convert(ActionEvent actionEvent) {
        String input = chord.getText();

        // check for empty chords
        if (input.isEmpty() || input.trim().isEmpty()) {
            chord.requestFocus();
            return;
        }

        // check for key selection
        if (key.getSelectionModel().getSelectedIndex() == -1) {
            key.requestFocus();
            return;
        }
        String selectedKey = key.getSelectionModel().getSelectedItem();

        // split chords and check for minimum chords in the progression
        String[] inputChords = input.split(",");
        if (inputChords.length < 2) {
            this.chord.requestFocus();
            return;
        }

        //  trim the extra white-space of input chords
        for (int i = 0; i < inputChords.length; i++) {
            inputChords[i] = inputChords[i].trim();
        }

        // get keys for conversion
        String homeNoteOrig = inputChords[0].replaceAll(patternNote, "");
        String homeNoteDest = selectedKey.replaceAll(patternNote, "");

        String[] outputChord = new String[inputChords.length];

        for (int i = 0; i < inputChords.length; i++) {
            String chord = inputChords[i];

            // verify valid chords
            if (!validChordName(chord)) {
                this.chord.requestFocus();
                return;
            }

            // filter the note name & entension
            String note = chord.replaceAll(patternNote, "");
            String extension = chord.replaceAll(patternExtension, "");

            // skip the first chord of the destination home key
            if (i == 0) {
                String firstChord = Note.getNote(Note.getIndex(homeNoteDest), inputChords[0].contains("b")) + extension;
                outputChord[i] = firstChord;
                continue;
            }

            // calculate differences of chords from original home key
            int stepDiff = MusicMath.diff(homeNoteOrig, note);

            // calculate the desired chord
            int result = MusicMath.moveOnFretBoard(Note.getIndex(homeNoteDest), stepDiff);
            String resultChord = Note.getNote(result, chord.contains("b")) + extension;
            outputChord[i] = resultChord;
        }


        //  calculate number of white-space required to have monospaced representation of chords in output
        int maxLetterInChord = 0;
        for (int i = 0; i < inputChords.length; i++) {
            int max = Math.max(inputChords[i].length(), outputChord[i].length());
            if (max > maxLetterInChord) maxLetterInChord = max;
        }

        // builder for output string

        StringBuilder output1 = new StringBuilder();
        StringBuilder output2 = new StringBuilder();

        for (int i = 0; i < inputChords.length; i++) {
            output1.append(inputChords[i]);
            output2.append(outputChord[i]);

            if (i != inputChords.length - 1) output1.append(", ");
            if (i != inputChords.length - 1) output2.append(", ");

            output1.append(spacer(inputChords[i], maxLetterInChord));
            output2.append(spacer(outputChord[i], maxLetterInChord));
        }

        // show the result
        chordFrom.setText(output1.toString());
        chordTo.setText(output2.toString());

        // save key
        PrefHelper.putString(KeyBoy.KEY_KEY, key.getSelectionModel().getSelectedIndex());

        // save chords
        PrefHelper.putString(KeyBoy.KEY_CHORD, input);

        // save input-output string
        PrefHelper.putString(KeyBoy.KEY_INPUT, output1.toString());
        PrefHelper.putString(KeyBoy.KEY_OUTPUT, output2.toString());
    }

    // verify whether a chord in in valid range
    private boolean validChordName(String chord) {
        char value = chord.charAt(0);
        return value >= 'A' && value <= 'G';
    }

    /*
     * a series of output chords be like Am, C, C#. here this method can calculate
     * the string of spaces that need to added to have monospaced length of chords
     * */
    private String spacer(String to, int range) {
        int tobeAdded = range - to.length();
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < tobeAdded; i++) {
            space.append(" ");
        }
        return space.toString();
    }

    private void loadPreviousCalculation() {
        // load the pin status & add the tooltip for pin button
        pinStatus = PrefHelper.getBoolean(KeyBoy.KEY_PIN, true);
        Tooltip install;
        if (pinStatus) {
            pin.setImage(pinOn);
            install = unpinTooltip;
        } else {
            pin.setImage(pinOff);
            install = pinTooltip;
        }
        Tooltip.install(pin, install);

        // select correct shape
        String shape = PrefHelper.getString(KeyBoy.KEY_SHAPE, "maj");
        if (shape.equals("maj")) maj.setSelected(true);
        else min.setSelected(true);

        // load correct value for shape in the key selection
        ArrayList<String> keys = shape.equals("maj") ? Chord.CHORD_MAJOR : Chord.CHORD_MINOR;
        key.getItems().addAll(keys);

        // select correct key in combo box
        int keyIndex = PrefHelper.getInt(KeyBoy.KEY_KEY, -1);
        if (keyIndex != -1) key.getSelectionModel().select(keyIndex);

        // load the chords in the chord box
        String chords = PrefHelper.getString(KeyBoy.KEY_CHORD, null);
        if (chords != null)
            this.chord.setText(chords);

        // load previously calculated input
        String inputChords = PrefHelper.getString(KeyBoy.KEY_INPUT, null);
        if (inputChords != null) chordFrom.setText(inputChords);

        // load previously calculated output
        String outputChords = PrefHelper.getString(KeyBoy.KEY_OUTPUT, null);
        if (outputChords != null) chordTo.setText(outputChords);
    }

    public void about() {
        StageHelper.showStage("The Author - KeyBoy", "layout/about.fxml", false, stage, false);
    }

    public void pin() {
        if (pinStatus) {
            pin.setImage(pinOff);
            stage.setAlwaysOnTop(false);
        } else {
            pin.setImage(pinOn);
            stage.setAlwaysOnTop(true);
        }

        // delete previous tooltip
        Tooltip installed = pinStatus ? pinTooltip : unpinTooltip;
        Tooltip.uninstall(pin, installed);

        pinStatus = !pinStatus;

        // install new tooltip
        Tooltip install = pinStatus ? unpinTooltip : pinTooltip;
        Tooltip.install(pin, install);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setAlwaysOnTop(PrefHelper.getBoolean(KeyBoy.KEY_PIN, true));

        // get the stage & set the listener to save window pin status
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                PrefHelper.putBoolean(KeyBoy.KEY_PIN, pinStatus);
            }
        });
    }

}
