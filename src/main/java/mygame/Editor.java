package mygame;


import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Editor {



//    @FXML
//    WebView editorView;
//
//    @FXML
//    TextArea compileErrors;

//    private RunScriptAction action;
//    private Stage modalStage;
//    private GameScene gameScene;

//    public void initialize(GameScene aGameScene, RunScriptAction aAction, Stage aModalStage) {
//        action = aAction;
//        modalStage = aModalStage;
//        gameScene = aGameScene;

        // We need JavaScript support
//        editorView.getEngine().setJavaScriptEnabled(true);
//        editorView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) - & gt; {
//            if (newValue == Worker.State.SUCCEEDED) {
//                initializeHTML();
//            }
//        });
        // The build in ACE context menu does not work because
        // JavaScript Clipboard interaction is disabled by security.
        // We have to do this by ourselfs.
//        editorView.setContextMenuEnabled(false);

        // Load the bootstrap html
        // It will trigger the initializeHTML() method by the above registered state change listener
        // after the everything was loaded
//        editorView.getEngine().load(EditScriptDialog.class.getResource("/ace/editor.html").toExternalForm());

        // Copy & Paste Clipboard support
//        final KeyCombination theCombinationCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
//        final KeyCombination theCombinationPaste = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
//        aModalStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, aEvent - & gt; {
//            if (theCombinationCopy.match(aEvent)) {
//                onCopy();
//            }
//            if (theCombinationPaste.match(aEvent)) {
//                onPaste();
//            }
//        });
//    }

//    private void onCopy() {
//
//        // Get the selected content from the editor
//        // We to a Java2JavaScript downcall here
//        // For details, take a look at the function declaration in editor.html
//        String theContentAsText = (String) editorView.getEngine().executeScript("copyselection()");
//
//        // And put it to the clipboard
//        Clipboard theClipboard = Clipboard.getSystemClipboard();
//        ClipboardContent theContent = new ClipboardContent();
//        theContent.putString(theContentAsText);
//        theClipboard.setContent(theContent);
//    }

//    private void onPaste() {
//
//        // Get the content from the clipboard
//        Clipboard theClipboard = Clipboard.getSystemClipboard();
//        String theContent = (String) theClipboard.getContent(DataFormat.PLAIN_TEXT);
//        if (theContent != null) {
//            // And put it in the editor
//            // We do a Java2JavaScript downcall here
//            // For details, take a look at the function declaration in editor.html
//            JSObject theWindow = (JSObject) editorView.getEngine().executeScript("window");
//            theWindow.call("pastevalue", theContent);
//        }
//    }

//    private void initializeHTML() {
//        // Initialize the editor
//        // and fill it with the LUA script taken from our editing action
//        Document theDocument = editorView.getEngine().getDocument();
//        Element theEditorElement = theDocument.getElementById("editor");
//
//     //   theEditorElement.setTextContent(action.scriptProperty().get().script);
//
//        editorView.getEngine().executeScript("initeditor()");
//    }

 //   private boolean test(Script aScript) {
      //  LUAScriptEngine theEngine = null;
       // try {

            // We only want to test on a clone
            // so the test does not change enything
//            GameScene theClone = persistenceManager.cloneSceneForPreview(gameScene);
//
//            // Execute a single run for verification
//            GameObject theObject = new GameObject(theClone, "dummy");
//            GameObjectInstance theInstance = theClone.createFrom(theObject);
//            theEngine = theClone.getRuntime().getScriptEngineFactory().createNewEngine(theClone, aScript);
//            theEngine.registerObject("instance", theInstance);
//            theEngine.registerObject("scene", theClone);
//            theEngine.registerObject("game", theClone.getGame());

        //    Object theResult = theEngine.proceedGame(100, 16);
         //   if (theResult == null) {
        //        throw new RuntimeException("Got NULL as a response, expected " + GameProcess.ProceedResult.STOPPED + " or " + GameProcess.ProceedResult.CONTINUE_RUNNING);
          //  }

          //  GameProcess.ProceedResult theResultAsEnum = GameProcess.ProceedResult.valueOf(theResult.toString());

          //  theEngine.shutdown();

           // compileErrors.setText("Got response : " + theResultAsEnum);

//            return true;
//        } catch (Exception e) {
//
//            StringWriter theWriter = new StringWriter();
//            e.printStackTrace(new PrintWriter(theWriter));
//
//            compileErrors.setText("Exception : " + theWriter);
//        } finally {
//     //       if (theEngine != null) {
//             //   theEngine.shutdown();
//      //      }
//        }
//        return false;
//    }

//    @FXML
//    public void onOk() {
        // We need to sace the edited script to the game model.
//        String theContent = (String) editorView.getEngine().executeScript("getvalue()");
      //  Script theNewScript = new Script(theContent);

     //   action.scriptProperty().set(theNewScript);
      //  modalStage.close();
//    }

//    @FXML
//    public void onTest() {
//        String theContent = (String) editorView.getEngine().executeScript("getvalue()");
       // Script theNewScript = new Script(theContent);
      //  test(theNewScript);
//    }

//    @FXML
//    public void onCancel() {
//        modalStage.close();
//    }
//
//    public void performEditing() {
//        modalStage.show();
//    }
}