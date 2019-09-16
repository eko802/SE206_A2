package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class PieceOfPaper implements Runnable{

    String search;
    String line;
    TextArea wikitTextArea;
    public PieceOfPaper(String search, String line, TextArea wikitTextArea){
        this.search = search;
        this.line = line;
        this.wikitTextArea = wikitTextArea;
    }

    @Override
    public void run() {
        try {
            if (line.equals("     1\t" + search + " not found :^(")) {
                //remove txt file created
                ProcessBuilder pb2 = new ProcessBuilder("/bin/bash", "-c", "rm " + search + ".txt");
                Process process2 = pb2.start();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Search");
                alert.setContentText("The term is not found. Please enter another term.");
                alert.showAndWait();
                return;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        wikitTextArea.appendText(line + "\n");
    }
}
