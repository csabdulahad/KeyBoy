package keyboy.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import keyboy.KeyBoy;
import keyboy.data.Chord;
import keyboy.helper.PrefHelper;
import keyboy.musimath.MusicMath;

import java.util.ArrayList;

public class Main {

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

    @FXML
    public void initialize() {

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
        if (input.isEmpty() || input.trim().isEmpty()) {
            chord.requestFocus();
            return;
        }

        if (key.getSelectionModel().getSelectedIndex() == -1) {
            key.requestFocus();
            return;
        }

        // split, trim & calculate number of white-space required to have monospaced representation in output
        String[] chords = input.split(",");
        if (chords.length < 2) {
            this.chord.requestFocus();
            return;
        }

        int numOfLatterInChord = 0;
        for (int i = 0; i < chords.length; i++) {
            // trim extra white-space
            chords[i] = chords[i].trim();
            int len = chords[i].length();
            if (len > numOfLatterInChord) numOfLatterInChord = len;
        }

        // get keys for conversion
        char homeKeyOrig = chords[0].toLowerCase().charAt(0);
        char homeKeyDest = key.getSelectionModel().getSelectedItem().toLowerCase().charAt(0);

        // builder for output string
        StringBuilder inputChord = new StringBuilder();
        StringBuilder outputChord = new StringBuilder(key.getSelectionModel().getSelectedItem() + ", ");

        for (int i = 0; i < chords.length; i++) {
            // trim extra white-space
            String chord = chords[i];

            // verify valid chords
            if (!validChordName(chord)) {
                this.chord.requestFocus();
                return;
            }

            // add the chord to input builder
            inputChord.append(chord);
            inputChord.append(spacer(chord, numOfLatterInChord));
            if (i != chords.length - 1) inputChord.append(", ");

            // skip the destination home key
            if (i == 0) continue;

            // calculate differences of chords from original home key
            char to = chord.toLowerCase().charAt(0);
            int diff = MusicMath.diff(homeKeyOrig, to);

            // calculate the desired chord
            String calculatedChord = String.valueOf(MusicMath.charMath(homeKeyDest, diff)).toUpperCase() + chord.substring(1);
            outputChord.append(calculatedChord);
            outputChord.append(spacer(calculatedChord, numOfLatterInChord));

            if (i != chords.length - 1) outputChord.append(", ");
        }

        // show the result
        chordFrom.setText(inputChord.toString());
        chordTo.setText(outputChord.toString());

        // save key
        PrefHelper.putString(KeyBoy.KEY_KEY, key.getSelectionModel().getSelectedIndex());

        // save chords
        PrefHelper.putString(KeyBoy.KEY_CHORD, input);

        // save input-output string
        PrefHelper.putString(KeyBoy.KEY_INPUT, inputChord.toString());
        PrefHelper.putString(KeyBoy.KEY_OUTPUT, outputChord.toString());

    }

    // verify whether a chord in in valid range
    private boolean validChordName(String chord) {
        char value = chord.toLowerCase().charAt(0);
        return !(value < 'a' || value > 'g');
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

}
