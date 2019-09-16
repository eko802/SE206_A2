package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Optional;

public class Main extends Application {

    private BorderPane root;
    private BorderPane defaultPane;
    private GridPane viewPane;
    private GridPane createPane;
    private GridPane playPane;
    private HBox mainMenuButtons;
    private Button viewButton;
    private Button createButton;
    private Button playButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wiki Player");

        //create panes for each menu
        root = new BorderPane();
        defaultPane = new BorderPane();
        viewPane = new GridPane();
        createPane = new GridPane();
        playPane = new GridPane();
        //main menu buttons
        viewButton = new Button("View/Delete");
        createButton = new Button("Create");
        playButton = new Button("Play");
        mainMenuButtons = new HBox();
        mainMenuButtons.getChildren().addAll(viewButton, createButton, playButton);
        mainMenuButtons.setAlignment(Pos.CENTER);


        //Default Pane design
        Text defaultPaneText = new Text("##WELCOME TO WIKI PLAYER##");
        defaultPaneText.setStyle("-fx-font: 18 arial;");
        Text instruction = new Text("Please select a menu at the bottom");
        defaultPane.setCenter(instruction);
        defaultPane.setTop(defaultPaneText);
        defaultPane.setAlignment(defaultPaneText, Pos.CENTER);

        //View Pane design
        Text viewPaneText = new Text("######View/Delete Existing Creations######");
        viewPaneText.setStyle("-fx-font: 18 arial;");
        viewPane.add(viewPaneText,0,0,2,1);
        viewPane.getColumnConstraints().add(new ColumnConstraints(300));
        viewPane.getColumnConstraints().add(new ColumnConstraints(100));
        viewPane.getRowConstraints().add(new RowConstraints(40));
        viewPane.getRowConstraints().add(new RowConstraints(200));
        viewPane.getRowConstraints().add(new RowConstraints(25));
        TextArea listTA = new TextArea("[Click \"Update list\" to update the list]");
        viewPane.add(listTA,0,1,1,1);
        Button updateButton = new Button("Update list");
        viewPane.add(updateButton,1,1,1,1);
        TextField deleteTF = new TextField("Enter name of creation to delete");
        viewPane.add(deleteTF, 0, 2,1,1);
        Button deleteButton = new Button("Delete");
        viewPane.add(deleteButton,1,2,1,1);

        //Play Pane design
        Text playPaneText = new Text("######Play Creations######");
        playPaneText.setStyle("-fx-font: 18 arial;");
        playPane.add(playPaneText,0,0,2,1);
        playPane.getColumnConstraints().add(new ColumnConstraints(300));
        playPane.getColumnConstraints().add(new ColumnConstraints(100));
        playPane.getRowConstraints().add(new RowConstraints(40));
        playPane.getRowConstraints().add(new RowConstraints(200));
        playPane.getRowConstraints().add(new RowConstraints(25));
        TextArea listTA2 = new TextArea("[Click \"Update list\" to update the list]");
        playPane.add(listTA2,0,1,1,1);
        Button updateButton2 = new Button("Update list");
        playPane.add(updateButton2,1,1,1,1);
        TextField playTF = new TextField("Enter name of creation to Play");
        playPane.add(playTF, 0, 2,1,1);
        Button playCButton = new Button("Play");
        playPane.add(playCButton,1,2,1,1);

        //Create Pane design
        Text createPaneText = new Text("######Create A New Creation######");
        createPaneText.setStyle("-fx-font: 18 arial;");
        createPane.add(createPaneText, 0, 0,2,1);
        createPane.getColumnConstraints().add(new ColumnConstraints(300));
        createPane.getColumnConstraints().add(new ColumnConstraints(100));
        createPane.getRowConstraints().add(new RowConstraints(40));
        createPane.getRowConstraints().add(new RowConstraints(25));
        createPane.getRowConstraints().add(new RowConstraints(300));
        createPane.getRowConstraints().add(new RowConstraints(25));
        TextField searchTextField = new TextField("Type here to search");
        searchTextField.setPrefSize(300,1000);
        createPane.add(searchTextField, 0, 1, 1, 1);
        Button searchButton = new Button("Search");
        createPane.add(searchButton,1,1,1,1);
        TextArea wikitTextArea = new TextArea();
        createPane.add(wikitTextArea, 0, 2,2,1);
        TextField lnTextField = new TextField("How many lines? (Enter a number)");
        createPane.add(lnTextField, 0, 3,1,1);
        Button createNewButton = new Button("Create");
        createPane.add(createNewButton, 1, 3,1,1);

        //Play Pane Button functions
        updateButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    ProcessBuilder pbup = new ProcessBuilder("/bin/bash", "-c", "cat list.txt; if [ $? -ne 0 ]; then echo EMPTY; fi");
                    Process processup = pbup.start();
                    BufferedReader stdoutup = new BufferedReader(new InputStreamReader(processup.getInputStream()));
                    String lineup = stdoutup.readLine();

                    //Check if the creation list is empty
                    if(lineup == null || lineup.equals("EMPTY")){
                        listTA2.clear();
                        listTA2.appendText("EMPTY");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("List is empty");
                        alert.setContentText("There are no creations created");
                        alert.showAndWait();
                        return;
                    }

                    ProcessBuilder pblist = new ProcessBuilder("/bin/bash", "-c", "cat list.txt");
                    Process processlist = pblist.start();
                    BufferedReader stdoutlist = new BufferedReader(new InputStreamReader(processlist.getInputStream()));

                    //If not empty, print out the creation list
                    String linelist;
                    listTA2.clear();
                    while((linelist = stdoutlist.readLine()) != null){
                        listTA2.appendText(linelist+"\n");
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        playCButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Check if the creation chosen exists
                String creationToPlay;
                creationToPlay = playTF.getText();

                try {
                    ProcessBuilder pbplay = new ProcessBuilder("/bin/bash", "-c", "if [ -d " + creationToPlay + " ]; then echo PLAY; fi");
                    Process processplay = pbplay.start();
                    BufferedReader stdoutplay = new BufferedReader(new InputStreamReader(processplay.getInputStream()));
                    String lineplay = stdoutplay.readLine();

                    if(lineplay != null && lineplay.equals("PLAY")){
                        ProcessBuilder pbplay2 = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit "+creationToPlay+"/*_CREATION.mp4 &> /dev/null");
                        Process processplay2 = pbplay2.start();
                    }else{
                        Alert alert3 = new Alert(Alert.AlertType.ERROR);
                        alert3.setTitle("CREATION CANNOT BE PLAYED");
                        alert3.setContentText("Creation not found. Try different name.");
                        alert3.showAndWait();
                        return;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        //View Pane Button functions
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "cat list.txt; if [ $? -ne 0 ]; then echo EMPTY; fi");
                    Process process = pb.start();
                    BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = stdout.readLine();

                    //Check if the creation list is empty
                    if(line == null || line.equals("EMPTY")){
                        listTA.clear();
                        listTA.appendText("EMPTY");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("List is empty");
                        alert.setContentText("There are no creations created");
                        alert.showAndWait();
                        return;
                    }

                    ProcessBuilder pblist = new ProcessBuilder("/bin/bash", "-c", "cat list.txt");
                    Process processlist = pblist.start();
                    BufferedReader stdoutlist = new BufferedReader(new InputStreamReader(processlist.getInputStream()));

                    //If not empty, print out the creation list
                    String linelist;
                    listTA.clear();
                    while((linelist = stdoutlist.readLine()) != null){
                        listTA.appendText(linelist+"\n");
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String creationToDel;
                creationToDel = deleteTF.getText();
                //Ask again if you really want to delete
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Are you sure?");
                alert.setContentText("Do you really want to delete it?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.CANCEL){
                    return;
                }else if (result.get() == ButtonType.OK) {
                    try {
                        ProcessBuilder pbdel = new ProcessBuilder("/bin/bash", "-c", "if [ -d " + creationToDel + " ]; then echo DEL; fi");
                        Process processdel = pbdel.start();
                        BufferedReader stdoutdel = new BufferedReader(new InputStreamReader(processdel.getInputStream()));
                        String linedel = stdoutdel.readLine();

                        if (linedel != null && linedel.equals("DEL")) {
                            ProcessBuilder pbdel2 = new ProcessBuilder("/bin/bash", "-c", "rm -rf " + creationToDel + "; sed /" + creationToDel + "/d list.txt > tmp.txt; cat tmp.txt > list.txt; rm tmp.txt");
                            Process processdel2 = pbdel2.start();
                        } else {
                            Alert alert2 = new Alert(Alert.AlertType.ERROR);
                            alert2.setTitle("CREATION NOT DELETED");
                            alert2.setContentText("Creation not found. Try different name.");
                            alert2.showAndWait();
                            return;
                        }
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert2.setTitle("CREATION DELETED");
                        alert2.setContentText("Creation is successfully deleted. Please click [Update list] to remove the deleted creation on the list.");
                        alert2.showAndWait();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        //Create Pane Button functions
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String search = searchTextField.getText();
                //Wikit run by worker
                Worker worker = new Worker(search, wikitTextArea, createNewButton);
                worker.start();
            }
        });

        createNewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int ln; //number of lines from wikit search
                try {
                    ln = Integer.parseInt(lnTextField.getText());
                }catch(Exception e){
                    //If the input is not number, popup error message
                    Alert numberAlert = new Alert(Alert.AlertType.ERROR);
                    numberAlert.setTitle("Invalid Input");
                    numberAlert.setContentText("Please enter a number");
                    numberAlert.showAndWait();
                    return;
                }
                //trunkate with the number specified
                try{
                    // Create txtfile with selected lines, then generate audio file, then generate video, then merge audio and video
                    String search = searchTextField.getText();
                    ProcessBuilder pb3 = new ProcessBuilder("/bin/bash", "-c", "head -n"+ln+" "+search+".txt > tmp.txt; cat tmp.txt > "+search+".txt; rm tmp.txt; espeak -f "+search+".txt -w "+search+".wav &> /dev/null; ffmpeg -f lavfi -i color=c=blue:s=320x240:d=`soxi -D "+search+".wav` -vf \"drawtext=fontfile=myfont.ttf:fontsize=30: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text="+search+"\" "+search+".mp4 &> /dev/null; ffmpeg -i "+search+".mp4 -i "+search+".wav -c:v copy -c:a aac -strict experimental "+search+"\"_CREATION\".mp4 &> /dev/null");
                    Process process3 = pb3.start();
                    String creationName;

                    while(true) {
                        TextInputDialog dialog = new TextInputDialog("Enter Here");
                        dialog.setTitle("New Creation Name");
                        dialog.setContentText("Please enter the name of the creation: ");
                        Optional<String> result = dialog.showAndWait();

                        if (result.isPresent()) {
                            creationName = result.get();
                            ProcessBuilder pb4 = new ProcessBuilder("/bin/bash", "-c", "if [ -e "+creationName+" ]; then echo EXISTING; fi");
                            Process process4 = pb4.start();
                            BufferedReader stdout = new BufferedReader(new InputStreamReader(process4.getInputStream()));
                            BufferedReader stderr = new BufferedReader(new InputStreamReader(process4.getErrorStream()));
                            String status = stdout.readLine();

                            if(status == null){
                                break;
                            }else if (status.equals("EXISTING")){
                                Alert existing = new Alert(Alert.AlertType.ERROR);
                                existing.setTitle("Invalid Creation Name");
                                existing.setContentText("Same name already exists. Try another name.");
                                existing.showAndWait();
                            }
                        }else{
                            return;
                        }
                    }

                    Alert creationAlert = new Alert(Alert.AlertType.INFORMATION);
                    creationAlert.setTitle("Creation Created");
                    creationAlert.setContentText("Creation successfully created");
                    creationAlert.showAndWait();

                    //create directory with the creation name. Move all necessary files into the directory
                    ProcessBuilder pb5 = new ProcessBuilder("/bin/bash", "-c", "mkdir "+ creationName + "; mv "+search+".txt " + creationName + "; mv "+search+".wav " +creationName +"; mv " +search+".mp4 "+creationName+"; mv "+search+"_CREATION.mp4 " + creationName + "; echo "+ creationName + " >> list.txt");
                    Process process5 = pb5.start();
                    //clear all textFields after finished
                    searchTextField.clear();
                    wikitTextArea.clear();
                    lnTextField.clear();

                    //disable create button once finished, and change it back to default create menu
                    createNewButton.setDisable(true);
                    searchTextField.setText("Type here to search");
                    lnTextField.setText("How many lines? (Enter a number)");
                    wikitTextArea.clear();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Main Buttons functions
        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //change to View Pane
                root.getChildren().remove(defaultPane);
                root.getChildren().remove(playPane);
                root.getChildren().remove(createPane);
                root.getChildren().add(viewPane);
                listTA.setText("[Click \"Update list\" to update the list]");
                deleteTF.setText("Enter name of creation to delete");
                viewButton.setDisable(true);
                createButton.setDisable(false);
                playButton.setDisable(false);
            }
        });
        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.getChildren().remove(defaultPane);
                root.getChildren().remove(playPane);
                root.getChildren().remove(viewPane);
                root.getChildren().add(createPane);
                searchTextField.setText("Type here to search");
                lnTextField.setText("How many lines? (Enter a number)");
                wikitTextArea.clear();
                createNewButton.setDisable(true); //this button is only enabled when wikit is finished
                createButton.setDisable(true);
                playButton.setDisable(false);
                viewButton.setDisable(false);
            }
        });
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.getChildren().remove(defaultPane);
                root.getChildren().remove(createPane);
                root.getChildren().remove(viewPane);
                root.getChildren().add(playPane);
                listTA2.setText("[Click \"Update list\" to update the list]");
                playTF.setText("Enter name of creation to Play");
                playButton.setDisable(true);
                createButton.setDisable(false);
                viewButton.setDisable(false);
            }
        });

        //GUI default configuration
        root.setCenter(defaultPane);
        root.setBottom(mainMenuButtons);
        root.setAlignment(mainMenuButtons, Pos.CENTER);



        //SHOW GUI
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
