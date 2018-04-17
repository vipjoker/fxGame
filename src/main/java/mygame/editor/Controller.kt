package mygame.editor

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.*

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode

import javafx.scene.layout.*
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import mygame.editor.actions.*
import mygame.editor.kotlin.ActionListenerDelegate
import mygame.editor.ui.CustomPane
import mygame.editor.view.AbstractView
import mygame.editor.model.Point


import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.URL
import java.util.*

import mygame.Constants.*
import mygame.editor.actions.shapes.CircleDrawer
import mygame.editor.actions.shapes.LineDrawer
import mygame.editor.actions.shapes.PolygonDrawer
import mygame.editor.actions.shapes.RectangleDrawer
import javafx.scene.control.TreeView
import javafx.scene.control.TreeItem
import mygame.editor.ui.TreeItemPath
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


class Controller : Initializable, ActionListenerDelegate {
//    https://www.iconfinder.com/icons/299098/cogs_icon#size=128
    private var tilePane: TilePane? = null
    var btnRun: Button? = null

    lateinit var root: SplitPane
    var group: ToggleGroup? = null
    private var box2dDialog: Box2dDialog = Box2dDialog()
    var canvas: CustomPane? = null

    lateinit var leftPane:VBox

    lateinit var rightPane: VBox

    lateinit var centerPane:AnchorPane

    lateinit var resourcesTreeview:TreeView<Path>

    private var currentDrawer: Action? = null
    var actions: MutableMap<String, Action> = mutableMapOf()

    private var views: MutableList<AbstractView> = mutableListOf()

    private val imageIcon = Image(javaClass.getResourceAsStream("/icons/image.png"))
    private val folderIcon = Image(javaClass.getResourceAsStream("/icons/foder_basic.png"))


    override fun initialize(location: URL?, resources: ResourceBundle?) {


        setListeners()

    }

    private fun setListeners() {

        Platform.runLater {
            canvas = CustomPane(
                    AppHolder.instance?.stage!!.width,
                    AppHolder.instance?.stage!!.height)



            canvas?.setListenerDelegate(this)
            AppHolder.instance?.scene!!.setOnKeyPressed {

                if (it.code == KeyCode.ENTER)
                    currentDrawer?.finishDrawing()
            }
            root.prefWidthProperty().bind(AppHolder.instance?.stage?.widthProperty())
            root.prefHeightProperty()?.bind(AppHolder.instance?.stage?.heightProperty())
            centerPane.children.add(canvas)
            AnchorPane.setTopAnchor(canvas,0.0)
            AnchorPane.setBottomAnchor(canvas,0.0)
            AnchorPane.setLeftAnchor(canvas,0.0)
            AnchorPane.setRightAnchor(canvas,0.0)
            root.setDividerPositions(.1, .8)
            setLeftPane()
            setRightPane()
            initActions()

        }
    }

    private fun setRightPane() {


        val loader = FXMLLoader(this.javaClass.getResource("/info.fxml"));
        val load: Parent = loader.load()
        val controller: InfoController = loader.getController();
        controller.setNameInfo("Test message")


        rightPane.children.add(load)
    }


    fun setLeftPane() {




        val treeRoot = TreeItem(Paths.get("resources"))

        fillTreeView(Paths.get(javaClass.getResource("/").toURI()), treeRoot)



        resourcesTreeview.isShowRoot = true
        resourcesTreeview.root = treeRoot

        resourcesTreeview.setCellFactory ({TreeItemPath() })

        resourcesTreeview.setOnMouseClicked {
            val selected = resourcesTreeview.selectionModel.selectedItem
            if (selected != null && selected.isLeaf) {


                val image = Image(selected.value.toUri().toString())

                val imageView = ImageView(image)
                canvas?.addItem(imageView)
            }
        }

    }





    private fun initActions() {
        actions = mutableMapOf(
                Pair(ACTION_SELECT, SelectAction(rightPane, canvas!!, views)),
                Pair(ACTION_POLYGON, PolygonDrawer(canvas!!, views)),
                Pair(ACTION_CIRCLE, CircleDrawer(canvas!!, views)),
                Pair(ACTION_RECTANGLE, RectangleDrawer(canvas!!, views)),
                Pair(ACTION_CHAIN, LineDrawer(canvas!!, views)),
                Pair(ACTION_MOVE, MoverAction(canvas!!, views)),
                Pair(ACTION_ROTATE, RotateAction(canvas, views)),
                Pair(ACTION_EDIT, EditAction(canvas!!, views)),
                Pair(ACTION_CREATE_BODY, CreateBodyAction(canvas!!, views)),
                Pair(ACTION_CREATE_JOINT, CreateJointAction(canvas!!, views))
        )
    }

    private fun fillTreeView(dir: Path, root: TreeItem<Path>) {


        Files.list(dir).forEach({
            if (!it.toFile().isDirectory) {
                val treeItem2 = TreeItem<Path>(it)
                root.children.add(treeItem2)
                val folderIcon = ImageView(imageIcon);
                folderIcon.fitHeight = 20.0
                folderIcon.fitWidth = 20.0
                treeItem2.graphic = folderIcon
            } else {
                val newRoot = TreeItem<Path>(it)
                root.children.add(newRoot)
//                if(it.toFile().name.endsWith(".png") || it.toFile().name.endsWith(".jpg")){
                    val folderIcon = ImageView(folderIcon);
                    folderIcon.fitWidth = 20.0
                    folderIcon.fitHeight = 20.0
                    newRoot.graphic = folderIcon
//                }
                fillTreeView(it, newRoot)

            }
        })
    }


    fun onSave(event: ActionEvent) {

        val fileChooser = FileChooser()
        fileChooser.setSelectedExtensionFilter(FileChooser.ExtensionFilter("Level files", ".level"));
        var file = fileChooser.showSaveDialog(AppHolder.instance?.window);


    }

    fun onLoad(event: ActionEvent) {
        val fileChooser = FileChooser();
        fileChooser.setTitle("Open level");

        var file = fileChooser.showOpenDialog(AppHolder.instance?.window)


    }


    fun onAdd(event: ActionEvent) {
        val node = event.target as Node
        val window = node.scene.window
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "Open Resource File"
        val imageDir: File = directoryChooser.showDialog(window)
        if (imageDir != null) {

            for (file in imageDir.listFiles())
                addTile(file)
        }
    }

    fun addTile(file: File) {

        var image: Image? = null
        try {
            image = Image(FileInputStream(file))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val pane: StackPane = StackPane()

        val imageView: ImageView = ImageView(image)


        imageView.fitWidth = 50.0
        imageView.fitHeight = 50.0
        pane.style = "-fx-border-color: #000000; -fx-border-width: 5px;-fx-border-radius: 5px;"

        pane.children.add(imageView)

        onImageClicked(imageView)
        tilePane!!.children.add(pane)


    }


    fun onImageClicked(image: ImageView) {

        image.setOnMouseClicked {


            val imageView: ImageView = it.getSource() as ImageView

//            imageView.getParent().setStyle("-fx-border-color: #ff005e;-fx-border-width: 5px;-fx-border-radius: 5px;");
//            if (mPane != null)
//                (mPane as StackPane).setStyle("-fx-border-color: #000000;-fx-border-width: 5px;-fx-border-radius: 5px;");
//
//            mPane =  imageView.getParent() as StackPane
        }
    }




    fun onRun(actionEvent: ActionEvent) {

        box2dDialog.show()
    }

    fun onMove(actionEvent: ActionEvent) {

        switchDrawer(ACTION_MOVE)
    }




    fun onPolygon(event: ActionEvent) {
        switchDrawer(ACTION_POLYGON)
    }

    fun onChain(event: ActionEvent) {
        switchDrawer(ACTION_CHAIN)
    }

    fun onCircle(event: ActionEvent) {
        switchDrawer(ACTION_CIRCLE)
    }




    private fun switchDrawer(action: String) {
        if (currentDrawer != null) currentDrawer?.finishDrawing();
        currentDrawer = actions[action] as Action
        currentDrawer?.init()
    }

    fun onRotate(event: ActionEvent) {
        switchDrawer(ACTION_ROTATE)
    }

    fun onEdit(event: ActionEvent) {
        switchDrawer(ACTION_EDIT)
    }

    fun onSelect(event: ActionEvent) {
        switchDrawer(ACTION_SELECT)
    }

    fun onCreateBody(event: ActionEvent) {
        switchDrawer(ACTION_CREATE_BODY)
    }

    fun onCreateJoint(event: ActionEvent) {
        switchDrawer(ACTION_CREATE_JOINT)
    }


    override fun onMousePressed(point: Point) {
        currentDrawer?.mousePressed(point);
    }


    override fun onMouseReleased(point: Point) {
        currentDrawer?.mouseReleased(point);
    }


    override fun onMouseDragged(point: Point) {
        currentDrawer?.mouseMoved(point);
    }


}
