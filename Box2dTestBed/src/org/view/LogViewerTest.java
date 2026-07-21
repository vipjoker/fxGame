package org.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogViewerTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LogViewer logViewer = new LogViewer();
        Scene scene = new Scene(logViewer);
            primaryStage.setTitle("Log Viewer using ListView");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Add a welcoming log entry
            logViewer.addLog(LogViewer.LogLevel.INFO, "Log viewer started.");
        }



}
