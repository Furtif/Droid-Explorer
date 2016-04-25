package com.droid.explorer

import com.droid.explorer.command.shell.impl.ListFiles
import com.droid.explorer.controller.Entry
import com.droid.explorer.gui.TextIconCell
import com.droid.explorer.tracking.PathTracking
import com.droid.explorer.tracking.PathTracking.currentPath
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import org.controlsfx.control.BreadCrumbBar
import tornadofx.App
import tornadofx.FX
import tornadofx.View
import java.util.*

/**
 * Created by Jonathan on 4/23/2016.
 */

fun main(args: Array<String>) {
	Application.launch(AppClass::class.java)
}

class AppClass : App() {

	override val primaryView = DroidExplorer::class

}

class DroidExplorer : View() {

	override val root: AnchorPane by fxml()

	@FXML lateinit var fileTable: TableView<Entry>
	@FXML lateinit var name: TableColumn<Entry, String>
	@FXML lateinit var date: TableColumn<Entry, String>
	@FXML lateinit var permissions: TableColumn<Entry, String>
	@FXML lateinit var type: TableColumn<Entry, String>
	@FXML lateinit var filePath: BreadCrumbBar<String>
	@FXML lateinit var back: Button
	@FXML lateinit var forward: Button
	@FXML lateinit var refresh: Button
	@FXML lateinit var home: Button

	init {
		title = "Droid Explorer"

		primaryStage.minHeight = 300.0
		primaryStage.minWidth = 575.0

		root.style = "-fx-background-color: #3d3d3d;";

		name.cellValueFactory = PropertyValueFactory("name")
		date.cellValueFactory = PropertyValueFactory("date")
		permissions.cellValueFactory = PropertyValueFactory("permissions")
		type.cellValueFactory = PropertyValueFactory("type")

		fileTable.placeholder = Label("This folder is empty.");

		FX.stylesheets.add(javaClass.getResource("css/DroidExplorer.css").toExternalForm())
		primaryStage.icons.add(Image(javaClass.getResource("img/favicon.png").toExternalForm()));

		back.graphic = ImageView(Image(javaClass.getResource("img/back.png").toExternalForm()))
		forward.graphic = ImageView(Image(javaClass.getResource("img/forward.png").toExternalForm()))
		refresh.graphic = ImageView(Image(javaClass.getResource("img/refresh.png").toExternalForm()))
		home.graphic = ImageView(Image(javaClass.getResource("img/home.png").toExternalForm()))

		refresh.setOnAction({ event -> refresh() })
		home.setOnAction({ event -> navigate("/") })

		back.setOnAction({ event -> PathTracking.back(this) })
		forward.setOnAction({ event -> PathTracking.forward(this) })

		name.setCellFactory({ column -> TextIconCell(this) })

		fileTable.onMouseClicked = EventHandler<MouseEvent> { mouseEvent ->
			if (mouseEvent.clickCount === 2) {
				val file = fileTable.selectionModel.selectedItem
				if (file != null && file.isDirectory()) {
					navigate(file.absolutePath)
				}
			}
		}

		filePath.setOnCrumbAction { navigate(currentPath.replaceAfter(it.selectedCrumb.value, "") + "/") }

		navigate("/")
	}

	fun navigate(path: String, add: Boolean = true) {
		if (add)
			PathTracking.add(path)
		currentPath = path
		refresh()
	}

	fun refresh() {
		var path = arrayOf("/", *currentPath.split("/").filterNot { it.isNullOrEmpty() }.toTypedArray())
		filePath.selectedCrumb = BreadCrumbBar.buildTreeModel(*path)

		val list = ArrayList<Entry>()

		ListFiles(currentPath, "-l").callback { list.add(it) }

		list.sortBy { it.type() }

		fileTable.items = FXCollections.observableArrayList(list)

		PathTracking.check(this)
	}
}

