package sample;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Worker extends Thread {

    String search;
    TextArea wikitTextArea;
    Button createNewButton;
    public Worker(String search, TextArea wikitTextArea, Button createNewButton){
        this.search = search;
        this.wikitTextArea = wikitTextArea;
        this.createNewButton = createNewButton;
    }
    @Override
    public void run(){
        try{
            //WORKER


            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "wikit "+search+" | sed 's/[.] /&\\n/g' | nl > \"" + search+".txt\"; cat " + search+".txt");
            Process process = pb.start();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = stdout.readLine()) != null){
                //show result in the text area with line number
                //When not found, alert error
                //run by piece of paper
                PieceOfPaper paper = new PieceOfPaper(search, line, wikitTextArea);
                Platform.runLater(paper);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //This enables create button only once the worker finishes the wikit command
        PieceOfPaper2 paper2 = new PieceOfPaper2(createNewButton);
        Platform.runLater(paper2);
    }



}
