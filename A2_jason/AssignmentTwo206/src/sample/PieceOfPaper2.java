package sample;

import javafx.scene.control.Button;

public class PieceOfPaper2 implements Runnable{

    Button createNewButton;

    PieceOfPaper2(Button createNewButton){
        this.createNewButton = createNewButton;
    }

    @Override
    public void run() {
        createNewButton.setDisable(false);
    }
}
