package mygame.editor

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.Insets
import javafx.geometry.Pos
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
import mygame.editor.ui.CustonPane
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
import javafx.util.Callback
import mygame.editor.ui.TreeItemPath
import sun.net.www.http.HttpClient
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Arrays


class Controller : Initializable, ActionListenerDelegate {
//    https://www.iconfinder.com/icons/299098/cogs_icon#size=128
    @FXML
    var tilePane: TilePane? = null
    @FXML
    var btnRun: Button? = null
    @FXML
    var tvStatus: Label? = null

    @FXML lateinit var root: SplitPane
    @FXML
    var group: ToggleGroup? = null
    var box2dDialog: Box2dDialog = Box2dDialog()
    var canvas: CustonPane? = null

    val leftPane: VBox = VBox()
    val rightPane: VBox = VBox()

    var currentDrawer: Action? = null
    var actions: MutableMap<String, Action> = mutableMapOf()

    var views: MutableList<AbstractView> = mutableListOf()

    val imageIcon = Image(javaClass.getResourceAsStream("/icons/image.png"))
    val folderIcon = Image(javaClass.getResourceAsStream("/icons/foder_basic.png"))


    override fun initialize(location: URL?, resources: ResourceBundle?) {

//        if (buttonLayout != null)
//            for (node in (buttonLayout as FlowPane).children) {
//                group?.toggles?.add(node as ToggleButton)
//            }
//


        setListeners()

    }

    fun setListeners() {

        Platform.runLater {
            canvas = CustonPane(
                    AppHolder.instance?.stage!!.width,
                    AppHolder.instance?.stage!!.height)



            canvas?.setListenerDelegate(this)
            AppHolder.instance?.scene!!.setOnKeyPressed {

                if (it.code == KeyCode.ENTER)
                    currentDrawer?.finishDrawing()
            }
            root.prefWidthProperty().bind(AppHolder.instance?.stage?.widthProperty())


            root.prefHeightProperty()?.bind(AppHolder.instance?.stage?.heightProperty())

            root.items.addAll(leftPane, canvas, rightPane)
            root.setDividerPositions(.1, .8)
            setLeftPane()
            setRightPane();

//            canvas.prefWidthProperty().bind(root.getCenterPane().widthProperty())
//            canvas.prefHeightProperty().bind(root.getCenterPane().heightProperty())

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
        leftPane.minWidth =300.0


//        leftPane.spacing = 10.0
//        leftPane.padding = Insets(10.0)
//        leftPane.alignment = Pos.TOP_CENTER

        var accordion = VBox()

        var createPane = TitledPane()
        createPane.isAnimated = true
        createPane.text = "Create"
        var craeateBox = VBox()
        craeateBox.spacing = 10.0
        createPane.content = craeateBox
        var createBody = Button("Create body")
        createBody.setOnMouseClicked { onCreateBody() }
        var createJoint = Button("Create joint")
        createJoint.setOnMouseClicked { onCreateJoint() }

        craeateBox.children.addAll(createBody, createJoint)


        var titled = TitledPane()
        titled.isAnimated = true
        titled.text = "Edit"


        val btnSelect = Button("Select")
        val btnEdit = Button("Edit")
        val btnMove = Button("Move")
        val btnRotate = Button("Rotate")

        btnEdit.setOnMouseClicked { onEdit() }
        btnSelect.setOnMouseClicked { onSelect() }
        btnMove.setOnMouseClicked { onMove() }
        btnRotate.setOnMouseClicked { onRotate() }


        var vbox = VBox(btnSelect, btnMove, btnEdit)

        vbox.spacing = 10.0
        titled.content = vbox


        var titled2 = TitledPane()

        titled2.text = "Fixtures"
        var btnLine = Button("Line")
        btnLine.setOnMouseClicked { onChain() }
        var btnCircle = Button("Circle")
        btnCircle.setOnMouseClicked { onCircle() }
        var btnPolygon = Button("Polygon")
        btnPolygon.setOnMouseClicked { onPolygon() }

        var vbox2 = VBox(btnLine, btnCircle, btnPolygon)
        vbox2.spacing = 10.0

        titled2.content = vbox2
        accordion.children.addAll(titled, createPane, titled2)




        val path = Paths.get("")
        val files = Files.list(path)
        files.forEach({ print(it.fileName) })

        val treeRoot = TreeItem(Paths.get("resources"))

        fillTreeView(Paths.get(javaClass.getResource("/").toURI()), treeRoot)


        val treeView = TreeView<Path>()
        treeView.isShowRoot = true
        treeView.root = treeRoot
        treeRoot.isExpanded = true

        treeView.setCellFactory ({TreeItemPath() })


        treeView.setOnMouseClicked {
            val selected = treeView.selectionModel.selectedItem;
            if (selected != null && selected.isLeaf) {
                Dialog.showDialog(selected.value)
            }
        }

        leftPane.children.addAll(accordion, treeView)
    }

    fun initActions() {
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

    fun fillTreeView(dir: Path, root: TreeItem<Path>) {


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

        val fileChooser: FileChooser = FileChooser()
        fileChooser.setSelectedExtensionFilter(FileChooser.ExtensionFilter("Level files", ".level"));
        var file = fileChooser.showSaveDialog(AppHolder.instance?.window);


    }

    fun onLoad(event: ActionEvent) {
        val fileChooser = FileChooser();
        fileChooser.setTitle("Open level");

        var file = fileChooser.showOpenDialog(AppHolder.instance?.window)


    }


    fun onAdd(event: ActionEvent) {

        var directoryChooser = DirectoryChooser();
        directoryChooser.setTitle("Open Resource File");
        var window: Stage = AppHolder.instance?.stage!!
        var imageDir: File = directoryChooser.showDialog(window);
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


    fun onAddH(event: ActionEvent) {

    }

    fun onRemoveH(event: ActionEvent) {

    }


    fun onRun(actionEvent: ActionEvent) {

        box2dDialog.show()
    }

    fun onMove() {

        switchDrawer(ACTION_MOVE);
    }


    //buttons
    fun onStatic(actionEvent: ActionEvent) {

    }

    fun onKinematic(actionEvent: ActionEvent) {

    }

    fun onDynamic(actionEvent: ActionEvent) {

    }

    fun onPolygon() {
        switchDrawer(ACTION_POLYGON)
    }

    fun onChain() {
        switchDrawer(ACTION_CHAIN)
    }

    fun onRectangle(actionEvent: ActionEvent) {
        switchDrawer(ACTION_RECTANGLE);
    }

    fun onCircle() {
        switchDrawer(ACTION_CIRCLE)
    }


    fun onDistance(actionEvent: ActionEvent) {

    }

    fun onRevolute(actionEvent: ActionEvent) {

    }


    fun switchDrawer(action: String) {
        if (currentDrawer != null) currentDrawer?.finishDrawing();
        currentDrawer = actions[action] as Action
        currentDrawer?.init()
    }

    fun onRotate() {
        switchDrawer(ACTION_ROTATE)
    }

    fun onEdit() {
        switchDrawer(ACTION_EDIT)
    }

    fun onSelect() {
        switchDrawer(ACTION_SELECT)
    }

    fun onCreateBody() {
        switchDrawer(ACTION_CREATE_BODY)
    }

    fun onCreateJoint() {
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
