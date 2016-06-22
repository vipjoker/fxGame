package mygame.demo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Makhobey Oleh on 6/12/16.
 * email: tajcig@ya.ru
 */
public class DragAndDropSample extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();
       final TextField source = new TextField("DRAG ME");

       final Text target = new Text(300,100,"DROP HERE");


       source.setOnDragDetected(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        System.out.println("detected");
                                        Dragboard dragboard = source.startDragAndDrop(TransferMode.ANY);
                                        ClipboardContent content = new ClipboardContent();
                                        content.putString(source.getText());
                                        dragboard.setContent(content);

                                    }
                                });

        target.setOnDragOver(new EventHandler<DragEvent>() {


                                 @Override
                                 public void handle(DragEvent event) {

                                     if(event.getGestureSource()  != target &&
                                             event.getDragboard().hasString()){
                                         event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                                     }
                                     event.consume();
                                 }
                             });
        target.setOnDragEntered(new EventHandler<DragEvent>() {
                                    @Override
                                    public void handle(DragEvent event) {
                                        if(event.getGestureSource() != target &&
                                                event.getDragboard().hasString()){
                                            target.setFill(Color.GREEN);
                                        }
                                        event.consume();
                                    }
                                });

        target.setOnDragExited(new EventHandler<DragEvent>() {
                                   @Override
                                   public void handle(DragEvent event) {
                                       target.setFill(Color.BLACK);
                                       event.consume();
                                   }

                               });

        target.setOnDragDropped(new EventHandler<DragEvent>() {

                                    @Override
                                    public void handle(DragEvent event) {
                                        Dragboard db = event.getDragboard();
                                        boolean success = false;
                                        if(db.hasString()){

                                            target.setText(target.getText() +"\n"+ db.getString());
                                            success = true;
                                        }
                                        event.setDropCompleted(success);
                                        event.consume();
                                    }

                                });
        source.setOnDragDone(new EventHandler<DragEvent>() {
                                 @Override
                                 public void handle(DragEvent event) {
                                    if(event.getTransferMode() == TransferMode.COPY){
                                        source.setText("");
                                    }
                                     event.consume();
                                 }
                             });


                group.getChildren().addAll(source, target);
        primaryStage.setScene(new Scene(group,500,500));
        primaryStage.show();


    }

    public static void main (String...args){
        launch(args);
    }
}
//http://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm