package newStuff.box2dEditor.org.view;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogViewer extends VBox {

    // Maximum number of log entries to keep
    private static final int MAX_LOG_ENTRIES = 1000;

    // The data model for the ListView
    private final ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();

    // ListView control
    private final ListView<LogEntry> listView = new ListView<>(logEntries);

    // Date formatter for log timestamps
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public LogViewer() {
        // Configure the ListView with a custom cell factory for colored log levels
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(LogEntry entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(entry.toString());
                    // Apply color based on log level
                    String color = switch (entry.getLevel()) {
                        case ERROR -> "#cc0000";    // red
                        case WARN  -> "#e67e00";    // orange
                        case INFO  -> "#0066cc";    // blue
                        case DEBUG -> "#666666";    // gray
                    };
                    setStyle("-fx-text-fill: " + color + ";");
                }
            }
        });
        listView.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-control-inner-background: #1e1e1e;" +
                        "-fx-background-insets: 0;"
        );
        // Auto-scroll to the latest entry whenever the list changes
        logEntries.addListener((javafx.collections.ListChangeListener.Change<? extends LogEntry> change) -> {
            if (!logEntries.isEmpty()) {
                listView.scrollTo(logEntries.size() - 1);
            }
        });

        // Clear button
        Button clearButton = new Button("Clear Log");
        clearButton.setOnAction(e -> logEntries.clear());

        // Layout
        setSpacing(10);
        getChildren().addAll( listView, clearButton);

        setPadding(new Insets(10));
        setPrefSize(600, 400);


    }

    /**
     * Thread‑safe method to add a log entry.
     * Automatically limits the list size.
     */
    public void addLog(LogLevel level, String message) {
        Platform.runLater(() -> {
            LogEntry entry = new LogEntry(LocalDateTime.now(), level, message);
            logEntries.add(entry);

            // Enforce max size
            while (logEntries.size() > MAX_LOG_ENTRIES) {
                logEntries.remove(0);
            }
        });
    }


    // ---------- Model classes ----------

    public enum LogLevel {
        ERROR, WARN, INFO, DEBUG
    }

    public static class LogEntry {
        private final LocalDateTime timestamp;
        private final LogLevel level;
        private final String message;

        public LogEntry(LocalDateTime timestamp, LogLevel level, String message) {
            this.timestamp = timestamp;
            this.level = level;
            this.message = message;
        }

        public LogLevel getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return TIME_FORMATTER.format(timestamp) +
                    " [" + level.name() + "] " +
                    message;
        }
    }

    // ---------- Launch ----------

}
