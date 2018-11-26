
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class CompilerStage extends Application implements CompilerFinals {
	private FileManager fm = new FileManager();
	private SoundGenerator soundGenerator;
	private String workSpacePath;
	private VBox topPanel = new VBox();
	private VBox bottomPanel = new VBox();
	private ButtonPane buttonsPane;
	private VBox menuPane = new VBox();
	private BorderPane screen = new BorderPane();
	private BorderPane compile = new BorderPane();
	private TabPane compilerArea = new TabPane();
	private ArrayList<Section> allSections = new ArrayList<>();
	private Pane explorer = new Pane();
	private PackageExplorer pe = new PackageExplorer();
	private Scene scene = new Scene(screen, MINIMIZED_WIDTH, MINIMIZED_HEIGHT);
	private SplitPane mainPane = new SplitPane();
	private MainMenuBar menu;
	private Project currentProject;
	private int volumeLevel;
	private VolumeBar volumeBar;

	@Override
	public void start(Stage pStage) {
		try {
			fm.loadWorkSpace();
			workSpacePath = fm.getWorkPath();
		} catch (FileNotFoundException e1) {
			new ErrorStage(FILE_ERROR_MESSAGE);
		} catch (IOException e1) {
			new ErrorStage(IO_ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (ProjectNotFoundException e1) {
			new ErrorStage(WORKSPACE_ERROR_MESSAGE);
		}
		try {
			currentProject = fm.getWorkSpace().get(0);
		} catch (IndexOutOfBoundsException e) {
			// if workspace is empty, don't set the current project.
		}
		pe.resetProjectsTree();
		pStage.setMaximized(true);
		pStage.setTitle(TITLE);
		pStage.setScene(scene);
		screen.setBottom(bottomPanel);
		bottomPanel.setPrefHeight(BOTTOM_PANEL_HEIGHT);
		screen.setCenter(mainPane);
		pStage.show();
		pStage.setResizable(true);
		buttonsPane = new ButtonPane();
		modifyMainPane(pStage);
		menu = new MainMenuBar();
		modifyTopPane(pStage);
		pStage.setOnCloseRequest(e -> {
			e.consume();
			safeExit();
		});
		volumeLevel = fm.getInitVolume();
		volumeBar = new VolumeBar();
		buttonsPane.getMetronomeButton().setSelected(fm.getMetroState());
		soundGenerator = new SoundGenerator(volumeLevel);
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Events

	public void initiateCompiler() {
		
	}
	
	public void run() {
		soundGenerator.run();
	}

	public void setVolume() {
		volumeBar.wakeUp();
	}
	
	public void toggleMetronome(ActionEvent e) {

	}
	
	public void search() {
		// TODO
	}

	public void rebindButtonsToFileTab(FileTab ft) { // refresh the button's bindings to fit the current opened FileTab.
		buttonsPane.getButtonDisablePropertyByIndex(BUTTON_REDO).bind(ft.redoProperty.not());
		buttonsPane.getButtonDisablePropertyByIndex(BUTTON_UNDO).bind(ft.undoProperty.not());
		buttonsPane.getButtonDisablePropertyByIndex(BUTTON_SAVE).bind(ft.savedProperty);

		// NOTE: the buttonsPane is binded to the FileTab, and the MainMenuBar items are
		// binded to the buttonsPane.
	}

	public void undo() {
		FileTab ft = (FileTab) compilerArea.getSelectionModel().getSelectedItem();
		if (fileTabAvailable(ft)) {
			ft.getCodeArea().undo();
		}
	}

	public void redo() {
		FileTab ft = (FileTab) compilerArea.getSelectionModel().getSelectedItem();
		if (fileTabAvailable(ft)) {
			ft.getCodeArea().redo();
		}
	}

	public void copy() {
		FileTab ft = (FileTab) compilerArea.getSelectionModel().getSelectedItem();
		if (fileTabAvailable(ft))
			ft.getCodeArea().copy();
	}

	public void cut() {
		FileTab ft = (FileTab) compilerArea.getSelectionModel().getSelectedItem();
		if (fileTabAvailable(ft))
			ft.getCodeArea().cut();
	}

	public void paste() {
		FileTab ft = (FileTab) compilerArea.getSelectionModel().getSelectedItem();
		if (fileTabAvailable(ft))
			ft.getCodeArea().paste();
	}

	public void delete() {
		FileTab ft = (FileTab) compilerArea.getSelectionModel().getSelectedItem();
		if (fileTabAvailable(ft))
			ft.getCodeArea().replaceSelection("");
	}

	public void selectAll() {
		FileTab ft = (FileTab) compilerArea.getSelectionModel().getSelectedItem();
		if (fileTabAvailable(ft))
			ft.getCodeArea().selectAll();
	}

	public boolean fileTabAvailable(FileTab ft) {
		if (ft == null)
			return false;
		return true;
	}

	public boolean checkUser() { // method to warn the user if he's sure executing the process.
		BooleanStage bs = new BooleanStage();
		return bs.getDecision();
	}

	public void manageExplorer(ActionEvent e) {
		if (menu.PackageIsSelected())
			pe.resetExplorer();
		else
			pe.closeExplorer();
	}

	public void addCompilerTab(Section s) {
		Tab ft = new FileTab(s);
		if (!allSections.contains(s) || allSections.isEmpty()) {
			if (compilerArea.getTabs().size() < MAX_TABS_SIMULTANEOUS) {
				allSections.add(s);
				compilerArea.getTabs().add(ft);
				SingleSelectionModel<Tab> selectionModel = compilerArea.getSelectionModel();
				selectionModel.select(ft);
			}
		}
		int index = allSections.indexOf(s);
		SingleSelectionModel<Tab> selectionModel = compilerArea.getSelectionModel();
		selectionModel.select(compilerArea.getTabs().get(index));
	}

	public void newProject() {
		new ProjectNamer();
	}

	public void newSection() {
		new SectionCreator();
	}

	public void open() {
		FileChooser fc = new FileChooser();
		File f = fc.showOpenDialog(null);
		try {
			Project p = fm.convertFileToProject(f);
			fm.addProject(p);
			pe.addProject(p);
		} catch (ProjectNotFoundException e) {
			new ErrorStage(FILE_NOT_SUPPORTED);
			return;
		} catch (NullPointerException e) { // if file has not been chosen. same as: ' if (f == null) return;'
			return;
		}

	}

	public void close() {
		try {
			for (Section s : currentProject.getSectionList()) {
				int index = allSections.indexOf(s);
				try {
					allSections.remove(index);
					compilerArea.getTabs().remove(index);
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing if the index is not found.
				}
			}
			currentProject = null;
		} catch (NullPointerException e) {
			return;
		}
	}

	public void closeThis(FileTab ft) {
		allSections.remove(ft.selfSection);
		compilerArea.getTabs().remove(ft);
	}

	public void closeAll() {
		allSections.clear();
		compilerArea.getTabs().clear();
		currentProject = null;
	}

	public void openWorkSpace() {
		try {
			Desktop.getDesktop().open(new File(workSpacePath));
		} catch (IOException e) {
			new ErrorStage(FILE_ERROR_MESSAGE);
		}
	}

	public void changeWorkSpace() {
		DirectoryChooser dc = new DirectoryChooser();
		File directory = dc.showDialog(null);
		if (directory != null) {
			workSpacePath = directory.getAbsolutePath() + '\\'; // '\' must be added for the path
			fm.setWorkPath(workSpacePath);
		}
	}

	public void save() {
		FileTab ft = (FileTab) (compilerArea.getSelectionModel().getSelectedItem());
		if (fileTabAvailable(ft)) {
			String code = ft.getCode();
			Section s = ft.getSelfSection();
			int index = fm.getWorkSpace().indexOf(s.getParent());
			fm.getWorkSpace().get(index).getSectionList().get(s.getIndexAtProjArray()).setArgs(code);
			fm.updateProject(s.getParent());
			ft.savedProperty.set(true);
		}
	}

	public void saveAll() {
		try {
			for (Tab tab : compilerArea.getTabs()) {
				FileTab ft = (FileTab) tab;
				if (fileTabAvailable(ft)) {
					String code = ft.getCode();
					Section s = ft.getSelfSection();
					int index = fm.getWorkSpace().indexOf(s.getParent());
					fm.getWorkSpace().get(index).getSectionList().get(s.getIndexAtProjArray()).setArgs(code);
					fm.updateProject(s.getParent());
					ft.savedProperty.set(true);
					buttonsPane.disableSaveAll();
				}
			}
		} catch (NullPointerException e) {
			return;
		}
	}

	public void deleteProject(Project p) {
		if (checkUser()) {
			for (Section s : p.getSectionList()) {
				int index = allSections.indexOf(s);
				allSections.remove(s);
				try {
					compilerArea.getTabs().remove(index);
				} catch (IndexOutOfBoundsException e) {
					// ;
				}
			}
			pe.removeProject(p);
			fm.deleteProject(p);
		}

	}

	public void deleteSection(Section s) {
		if (checkUser()) {
			pe.removeSection(s);
			fm.deleteSection(s);
		}
		int index = allSections.indexOf(s);
		allSections.remove(s);
		try {
			compilerArea.getTabs().remove(index);
		} catch (IndexOutOfBoundsException e) {
			// do nothing, 'cause the current tab is not opened.
		}
	}

	public void safeExit() {
		if (checkUser()) {
			try {
				fm.setInitVolume(volumeLevel);
				fm.setMetroState(menu.metronomeIsSelected());
				fm.saveWorkSpace();
				for (Project p : fm.getWorkSpace()) {
					fm.saveProject(p);
				}
			} catch (IOException e) {
				new ErrorStage(WORKSPACE_ERROR_MESSAGE);
			}
			Platform.exit();
		}
	}

	// Modify visuals

	public void modifyTopPane(Stage stg) {
		screen.setTop(topPanel);
		topPanel.getChildren().add(menuPane);
		menuPane.getChildren().add(menu);
		topPanel.getChildren().add(buttonsPane);
		buttonsPane.setPrefHeight(BUTTON_HEIGHT);
	}

	public void modifyMainPane(Stage stg) {
		mainPane.setOrientation(Orientation.HORIZONTAL);
		mainPane.getItems().addAll(explorer, compile);
		mainPane.setPrefWidth(stg.getWidth() * MAIN_PANE_MMULTIPLIER);
		explorer.getChildren().add(pe);
		pe.prefHeightProperty()
				.bind(mainPane.heightProperty().subtract(bottomPanel.heightProperty().multiply(EXPLORER_BIND_VALUE)));
		pe.prefWidthProperty().bind(mainPane.getDividers().get(0).positionProperty().multiply(mainPane.getPrefWidth()));
		compile.setCenter(compilerArea);

		try {
			addCompilerTab(fm.getWorkSpace().get(0).getSectionList().get(0));
		} catch (IndexOutOfBoundsException e) {
			// do nothing if there are no projects.
		}
		mainPane.setDividerPositions(SPLIT_GAP);
	}

	class PackageExplorer extends TabPane {
		private ImageView treeIcon = FileManager.makeImageView(FOLDERS_ICON);
		private Tab tab = new Tab();
		private TreeItem<Object> rootNode = new TreeItem<>(PACKAGE_NAME, treeIcon);
		private TreeView<Object> tree;

		// IMPORTANT: when adding a new TreeItem, the icon must be a new ImageView.
		// creating a private ImageView will disrupt the package explorer graphics.

		public PackageExplorer() {
			resetProjectsTree();
			tab.setText(PACKAGE_NAME);
			tab.setGraphic(FileManager.makeImageView(TREE_ICON));
			this.getTabs().add(tab);
			tab.setOnClosed(e -> menu.setPackageSelector(false));
			tree.setEditable(true);
			tab.setContent(tree);
			tree.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> touchProject(newValue));
			tree.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
				@Override
				public TreeCell<Object> call(TreeView<Object> p) {
					return new TextFieldTreeCellImpl();
				}
			});
		}

		public void resetProjectsTree() {
			rootNode.getChildren().clear();
			int workSize = fm.getWorkSpace().size();
			for (int i = 0; i < workSize; i++) {
				TreeItem<Object> newItem = new TreeItem<>(fm.getWorkSpace().get(i),
						FileManager.makeImageView(PROJECT_ICON));
				rootNode.getChildren().add(newItem);
				for (int j = 0; j < fm.getWorkSpace().get(i).getNumOfSections(); j++) {
					rootNode.getChildren().get(i).getChildren().add(new TreeItem<>(
							fm.getWorkSpace().get(i).getSectionList().get(j), FileManager.makeImageView(SECTION_ICON)));
				}
			}
			tree = new TreeView<>(rootNode);
			rootNode.setExpanded(true);
		}

		public void addProject(Project p) { // adding a project to the explorer must be done AFTER the project is added
											// to the FileManager!
			TreeItem<Object> item = new TreeItem<>(p, FileManager.makeImageView(PROJECT_ICON));
			rootNode.getChildren().add(item);
			for (Section s : p.getSectionList()) {
				item.getChildren().add(new TreeItem<>(s, FileManager.makeImageView(SECTION_ICON)));
			}
		}

		public void removeProject(Project p) { // removing a project to the explorer must be done BEFORE the project is
												// removed from the FileManager!
			int index = fm.getWorkSpace().indexOf(p);
			rootNode.getChildren().remove(index);
		}

		public void addSection(Section s) { // adding a section to the explorer must be done AFTER the section is added
											// to the FileManager!
			TreeItem<Object> item = new TreeItem<>(s, FileManager.makeImageView(SECTION_ICON));
			int index = fm.getWorkSpace().indexOf(s.getParent());
			rootNode.getChildren().get(index).getChildren().add(item);
		}

		public void removeSection(Section s) { // removing a section to the explorer must be done AFTER the section is
												// removed from the FileManager!
			int projIndex = fm.getWorkSpace().indexOf(s.getParent());
			int secIndex = fm.getWorkSpace().get(projIndex).getSectionList().indexOf(s);
			rootNode.getChildren().get(projIndex).getChildren().remove(secIndex);
		}

		public void touchProject(TreeItem<Object> newValue) {
			if (newValue == null)
				return;
			if (newValue.getValue().getClass().equals(Project.class)) {
				currentProject = (Project) newValue.getValue();
			}
		}

		public void closeExplorer() {
			this.getTabs().clear();
		}

		public void resetExplorer() {
			this.getTabs().add(tab);
			mainPane.getDividers().get(0).setPosition(SPLIT_GAP);
		}

		private final class TextFieldTreeCellImpl extends TreeCell<Object> {

			private TextField textField;
			private ContextMenu projMenu = new ContextMenu();
			private ContextMenu sectionMenu = new ContextMenu();
			private ContextMenu packMenu = new ContextMenu();

			public TextFieldTreeCellImpl() {
				MenuItem addProj = new MenuItem(ADD_PROJ);
				MenuItem addSec = new MenuItem(ADD_SECTION);
				MenuItem addSec2 = new MenuItem(ADD_SECTION);
				MenuItem sDelete = new MenuItem(S_DELETE);
				MenuItem pDelete = new MenuItem(P_DELETE);
				MenuItem openSection = new MenuItem(OPEN_SECTION);
				MenuItem pClose = new MenuItem(P_CLOSE);
				MenuItem changeWS = new MenuItem(CHANGE_WORKSPACE);
				MenuItem openWS = new MenuItem(OPEN_WORKSPACE);
				packMenu.getItems().addAll(addProj, openWS, changeWS);
				projMenu.getItems().addAll(pClose, pDelete, addSec);
				sectionMenu.getItems().addAll(openSection, sDelete, addSec2);
				openSection.setOnAction(e -> addCompilerTab((Section) getTreeItem().getValue()));
				pClose.setOnAction(e -> close());
				sDelete.setOnAction(e -> deleteSection((Section) getTreeItem().getValue()));
				pDelete.setOnAction(e -> deleteProject((Project) getTreeItem().getValue()));
				addProj.setOnAction(e -> newProject());
				addSec.setOnAction(e -> newSection());
				addSec2.setOnAction(e -> newSection());
				changeWS.setOnAction(e -> changeWorkSpace());
				openWS.setOnAction(e -> openWorkSpace());
			}

			@Override
			public void startEdit() { // doubleclick event
				if (getTreeItem().getValue().getClass() == Project.class) {
					currentProject = ((Project) getTreeItem().getValue());

				} else if (getTreeItem().getValue().getClass() == Section.class) {
					Section c = ((Section) getTreeItem().getValue());
					addCompilerTab(c);
				}
			}

			@Override
			public void cancelEdit() {
				super.cancelEdit();
				setText((String) getItem());
				setGraphic(getTreeItem().getGraphic());
			}

			@Override
			public void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					if (isEditing()) {
						if (textField != null) {
							textField.setText(getString());
						}
						setText(null);
						setGraphic(textField);
					} else {
						setText(getString());
						setGraphic(getTreeItem().getGraphic());
						if (getTreeItem().getValue().getClass().equals(Section.class)) { // sets the section contex menu
							setContextMenu(sectionMenu);
						} else if (getTreeItem().getValue().getClass().equals(Project.class)) // sets the project contex
																								// menu
							setContextMenu(projMenu);
						else
							setContextMenu(packMenu);
					}
				}
			}

			private String getString() {
				return getItem() == null ? "" : getItem().toString();
			}
		}
	}

	class MainMenuBar extends MenuBar implements MenuFinals {
		// when adding a Menu/MenuItem,if needed, change the current index in the Finals
		// interface!!!
		private ArrayList<Menu> menues = new ArrayList<>();
		private Menu newMenu = new Menu(NEW);
		private MenuItem[] fileItems = { new MenuItem(OPEN), new MenuItem(CLOSE), new MenuItem(CLOSEALL),
				new MenuItem(SAVE), new MenuItem(SAVEALL), new MenuItem(OPEN_WORK_SPACE), new MenuItem(WORK_SPACE),
				new MenuItem(EXIT) };
		private MenuItem[] editItems = { new MenuItem(UNDO), new MenuItem(REDO), new MenuItem(CUT), new MenuItem(COPY),
				new MenuItem(PASTE), new MenuItem(DELETE), new MenuItem(SELECTALL) };
		private CheckMenuItem[] windowItems = { new CheckMenuItem(PACKAGE_EXPLORER) };
		private MenuItem[] playerItems = {new MenuItem(VOLUME), new CheckMenuItem(METRONOME)};
		private ImageView[] soundLevels = {FileManager.makeImageView(PLAYER_MUTE), FileManager.makeImageView(PLAYER_UNMUTE)};

		public MainMenuBar() {
			menues.add(new Menu(FILE));
			menues.add(new Menu(EDIT));
			menues.add(new Menu(RUN));
			menues.add(new Menu(PLAYER));
			menues.add(new Menu(WINDOW));
			menues.add(new Menu(HELP));

			newMenu.getItems().add(new MenuItem(PROJECT));
			newMenu.getItems().add(new MenuItem(SECTION));

			getMenus().addAll(menues);
			menues.get(FILE_INDEX).getItems().add(newMenu);
			menues.get(FILE_INDEX).getItems().addAll(fileItems);
			menues.get(EDIT_INDEX).getItems().addAll(editItems);
			menues.get(PLAYER_INDEX).getItems().addAll(playerItems);
			menues.get(WINDOW_INDEX).getItems().addAll(windowItems);

			fileItems[F_OPEN_INDEX].setOnAction(e -> open());
			fileItems[F_CLOSE_INDEX].setOnAction(e -> close());
			fileItems[F_CLOSE_INDEX].setAccelerator(KeyCombination.keyCombination("SHORTCUT+W"));
			fileItems[F_CLOSEALL_INDEX].setOnAction(e -> closeAll());
			fileItems[F_CLOSEALL_INDEX].setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+W"));
			fileItems[F_SAVE_INDEX].setOnAction(e -> save());
			fileItems[F_SAVE_INDEX].setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));
			fileItems[F_SAVE_INDEX].setGraphic(FileManager.makeImageView(MENU_SAVE));
			fileItems[F_SAVE_INDEX].disableProperty().bind(buttonsPane.getButtonDisablePropertyByIndex(BUTTON_SAVE));
			fileItems[F_SAVEALL_INDEX].setOnAction(e -> saveAll());
			fileItems[F_SAVEALL_INDEX].setGraphic(FileManager.makeImageView(MENU_SAVEALL));
			fileItems[F_SAVEALL_INDEX].setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+S"));
			fileItems[F_SAVEALL_INDEX].disableProperty()
					.bind(buttonsPane.getButtonDisablePropertyByIndex(BUTTON_SAVEALL));
			fileItems[F_OPEN_WORK_SPACE_INDEX].setOnAction(e -> openWorkSpace());
			fileItems[F_WORK_SPACE_INDEX].setOnAction(e -> changeWorkSpace());
			fileItems[F_EXIT_INDEX].setOnAction(e -> safeExit());

			windowItems[W_PACKAGE_INDEX].setSelected(true);
			((CheckMenuItem) playerItems[1]).setSelected(false);

			newMenu.getItems().get(0).setOnAction(e -> newProject());
			newMenu.getItems().get(1).setOnAction(e -> newSection());

			editItems[0].setOnAction(e -> undo());
			editItems[0].setGraphic(FileManager.makeImageView(EDIT_UNDO));
			editItems[0].setAccelerator(KeyCombination.keyCombination("SHORTCUT+Z"));
			editItems[0].disableProperty().bind(buttonsPane.getButtonDisablePropertyByIndex(BUTTON_UNDO));
			editItems[1].setOnAction(e -> redo());
			editItems[1].setAccelerator(KeyCombination.keyCombination("SHORTCUT+Y"));
			editItems[1].setGraphic(FileManager.makeImageView(EDIT_REDO));
			editItems[1].disableProperty().bind(buttonsPane.getButtonDisablePropertyByIndex(BUTTON_REDO));
			editItems[2].setOnAction(e -> cut());
			editItems[2].setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
			editItems[2].setGraphic(FileManager.makeImageView(EDIT_CUT));
			editItems[3].setOnAction(e -> copy());
			editItems[3].setAccelerator(KeyCombination.keyCombination("SHORTCUT+C"));
			editItems[3].setGraphic(FileManager.makeImageView(EDIT_COPY));
			editItems[4].setOnAction(e -> paste());
			editItems[4].setAccelerator(KeyCombination.keyCombination("SHORTCUT+P"));
			editItems[4].setGraphic(FileManager.makeImageView(EDIT_PASTE));
			editItems[5].setOnAction(e -> delete());
			editItems[5].setAccelerator(KeyCombination.keyCombination("DELETE"));
			editItems[5].setGraphic(FileManager.makeImageView(EDIT_DELETE));
			editItems[6].setOnAction(e -> selectAll());
			editItems[6].setAccelerator(KeyCombination.keyCombination("SHORTCUT+A"));

			playerItems[0].setOnAction(e -> setVolume());
			playerItems[0].setGraphic(FileManager.makeImageView(PLAYER_UNMUTE));
			playerItems[1].setOnAction(e -> toggleMetronome(e));
			playerItems[1].setGraphic(FileManager.makeImageView(PLAYER_METRONOME));
			buttonsPane.getMetronomeButton().selectedProperty().bindBidirectional(this.getMetronomeMenuItem().selectedProperty()); //binds the MenuItem and Button of the metronome.
			windowItems[W_PACKAGE_INDEX].setOnAction(e -> manageExplorer(e));
			
		}

		public void refreshSound(int soundLevel) {
			if (soundLevel == 0)
				playerItems[0].setGraphic(soundLevels[0]);
			else
				playerItems[0].setGraphic(soundLevels[1]);
		}
		
		public void disableSaves() {
			fileItems[F_SAVE_INDEX].setDisable(true);
			fileItems[F_SAVEALL_INDEX].setDisable(true);
		}

		public void enableSaves() {
			fileItems[F_SAVE_INDEX].setDisable(false);
			fileItems[F_SAVEALL_INDEX].setDisable(false);
		}
		
		public CheckMenuItem getMetronomeMenuItem() {
			return (CheckMenuItem) playerItems[1];
		}
		
		public boolean metronomeIsSelected() {
			return ((CheckMenuItem) playerItems[1]).isSelected();
		}
		
		public void setPackageSelector(boolean value) {
			if (value == true)
				windowItems[W_PACKAGE_INDEX].setSelected(true);
			else
				windowItems[W_PACKAGE_INDEX].setSelected(false);
		}

		public boolean PackageIsSelected() {
			return windowItems[W_PACKAGE_INDEX].isSelected();

		}

		public ArrayList<Menu> getMenues() {
			return menues;
		}

	}

	class ButtonPane extends HBox {
		private ArrayList<String> filePaths = new ArrayList<>();
		private Button[] buttons;
		private ToggleButton metronome = new ToggleButton();
		private int metronomeIndex = 11;
		private ImageView[] volumeLevels = {FileManager.makeImageView(VOLUME_MUTE_SMALL),FileManager.makeImageView(VOLUME_LOW_SMALL),FileManager.makeImageView(VOLUME_MEDIUM_SMALL),FileManager.makeImageView(VOLUME_FULL_SMALL)};

		public ButtonPane() {
			super(PLATFORM_GAP);
			// Adding a platform must be done through the finals interface.
			PlatformPane[] platforms = new PlatformPane[TOTAL_PLATFORMS];
			Separator[] seperators = new Separator[TOTAL_PLATFORMS];
			MenuButton newButton = new MenuButton();
			newButton.getItems().addAll(new MenuItem("Project"), new MenuItem("Section"));
			newButton.setGraphic(FileManager.makeImageView(NEW_FILE_ICON));
			newButton.setPrefSize(ICONS_SIZE, ICONS_SIZE * 2);
			newButton.getItems().get(0).setOnAction(e -> newProject());
			newButton.getItems().get(1).setOnAction(e -> newSection());
			newButton.setStyle("-fx-base:  	#F8F8FF;");
			for (int i = 0; i < TOTAL_PLATFORMS; i++) {
				platforms[i] = new PlatformPane();
				seperators[i] = new Separator();
				seperators[i].setOrientation(Orientation.VERTICAL);
			}
			// when adding a new button only add the icon to this array list, then change
			// the TOTAL buttons in the finals interface in addition to the button's
			// category.
			filePaths.add(NEW_FILE_ICON);
			filePaths.add(OPEN_ICON);
			filePaths.add(SAVE_ICON);
			filePaths.add(SAVEALL_ICON);
			filePaths.add(UNDO_ICON);
			filePaths.add(REDO_ICON);
			filePaths.add(COPY_ICON);
			filePaths.add(CUT_ICON);
			filePaths.add(PASTE_ICON);
			filePaths.add(PLAY_ICON);
			filePaths.add(VOLUME_FULL_SMALL);
			filePaths.add(SEARCH_ICON);
			buttons = new Button[filePaths.size()];
			for (int i = 0; i < filePaths.size(); i++) {
				buttons[i] = new Button();
				buttons[i].setGraphic(FileManager.makeImageView(filePaths.get(i)));
			}

			for (Button b : buttons) {
				b.setStyle("-fx-font: 22 arial; -fx-base:  	#F8F8FF;");
				b.setPrefSize(ICONS_SIZE, ICONS_SIZE);
			}

			// Metronome is a toggle button, therefore it is initiated here.
			metronome.setGraphic(FileManager.makeImageView(METRONOME_ICON));
			metronome.setStyle("-fx-font: 22 arial; -fx-base:  	#F8F8FF;");
			metronome.setPrefSize(ICONS_SIZE, ICONS_SIZE);

			// when adding buttons change the TOTAL BUTTONS in the finals interface!
			for (int i = 0; i < buttons.length; i++) {
				if (i == 0)
					platforms[0].getChildren().add(newButton);
				if (i >= 1 && i < TOTAL_FILE_BUTTONS)
					platforms[0].getChildren().add(buttons[i]);
				else if (i >= TOTAL_FILE_BUTTONS && i < TOTAL_FILE_BUTTONS + TOTAL_EDIT_BUTTONS)
					platforms[1].getChildren().add(buttons[i]);
				else if (i >= TOTAL_FILE_BUTTONS + TOTAL_EDIT_BUTTONS
						&& i < TOTAL_FILE_BUTTONS + TOTAL_EDIT_BUTTONS + TOTAL_APP_BUTTONS) {
					if (i == metronomeIndex)
						platforms[2].getChildren().add(metronome);
					platforms[2].getChildren().add(buttons[i]);
				}
			}
			for (int i = 0; i < TOTAL_PLATFORMS; i++) {
				this.getChildren().add(platforms[i]);
				this.getChildren().add(seperators[i]);
			}
			buttons[BUTTON_OPEN].setOnAction(e -> open());
			buttons[BUTTON_SAVE].setOnAction(e -> save());
			buttons[BUTTON_SAVEALL].setOnAction(e -> saveAll());
			disableSaveAll();
			buttons[BUTTON_UNDO].setOnAction(e -> undo());
			buttons[BUTTON_REDO].setOnAction(e -> redo());
			buttons[BUTTON_COPY].setOnAction(e -> copy());
			buttons[BUTTON_CUT].setOnAction(e -> cut());
			buttons[BUTTON_PASTE].setOnAction(e -> paste());
			buttons[BUTTON_PLAY].setOnAction(e -> run());
			buttons[BUTTON_SEARCH].setOnAction(e -> search());
			buttons[BUTTON_VOLUME].setOnAction(e -> setVolume());
			metronome.setSelected(false);
			
		}
		
		public void refreshSound(int soundLevel) {
			if (soundLevel == 0) {
				buttons[BUTTON_VOLUME].setGraphic(volumeLevels[0]);
			}
			else if (soundLevel > 0 && soundLevel < 34) {
				buttons[BUTTON_VOLUME].setGraphic(volumeLevels[1]);
			}
			else if (soundLevel >= 34 && soundLevel < 67) {
				buttons[BUTTON_VOLUME].setGraphic(volumeLevels[2]);
			}
			else if (soundLevel >= 67 && soundLevel <= 100) {
				buttons[BUTTON_VOLUME].setGraphic(volumeLevels[3]);
			}
		}
		
		public ToggleButton getMetronomeButton() {
			return metronome;
		}
		
		public BooleanProperty getButtonDisablePropertyByIndex(int buttonIndex) {
			return buttons[buttonIndex].disableProperty();
		}

		public void disableSaveAll() {
			buttons[BUTTON_SAVEALL].setDisable(true);
		}

		public void enableSaveAll() {
			buttons[BUTTON_SAVEALL].setDisable(false);
		}

		private class PlatformPane extends HBox {
			public PlatformPane() {
				super(ICONS_GAP);
				this.setPadding(new Insets(ICONS_GAP, ICONS_GAP, ICONS_GAP, ICONS_GAP));
			}
		}

	}

	class FileTab extends Tab {
		private ScrollPane scroller = new ScrollPane();
		private CodeArea codeArea = new CodeArea();
		private HBox tabContent = new HBox();
		private TextArea sample = new TextArea();
		private Section selfSection;
		// self-made properties so the program can manage with SimpleBooleanProperty.
		SimpleBooleanProperty undoProperty = new SimpleBooleanProperty(true);
		SimpleBooleanProperty redoProperty = new SimpleBooleanProperty(true);
		SimpleBooleanProperty savedProperty = new SimpleBooleanProperty(true);

		public FileTab(Section s) {
			undoProperty.bind(codeArea.undoAvailableProperty());
			redoProperty.bind(codeArea.redoAvailableProperty());
			initCodeArea();
			codeArea.replaceText(s.getArgs());
			codeArea.getUndoManager().forgetHistory();
			codeArea.setContextMenu(sample.getContextMenu());
			codeArea.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
					savedProperty.set(false);
					buttonsPane.enableSaveAll();
				}
			});
			selfSection = s;
			this.setText(s.getName());
			this.setGraphic(FileManager.makeImageView(TAB_ICON));
			this.setContent(scroller);
			this.setContextMenu(new FileTabContextMenu(this));
			tabContent.setPadding(new Insets(PLATFORM_GAP));
			tabContent.getChildren().add(codeArea);
			scroller.setContent(tabContent);
			setOnClosed(e -> allSections.remove(s));
			this.setOnSelectionChanged(e -> {
				currentProject = selfSection.getParent();
				rebindButtonsToFileTab(this);
			});

		}

		public void initCodeArea() {
			// change this method only if you know what you are doing.
			codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
			tabContent.getStylesheets().add(LanguageKeyWords.class.getResource(KEYWORDS_CSS).toExternalForm());
			codeArea.richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved())).subscribe(change -> {
				codeArea.setStyleSpans(0, LanguageKeyWords.computeHighlighting(codeArea.getText()));
			});
			codeArea.setPrefWidth(CODE_AREA_WIDTH);
			codeArea.setPrefHeight(CODE_AREA_HEIGHT);
			codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
			codeArea.setAutoScrollOnDragDesired(true);
		}

		public String[] sendArguments() {
			String args[] = codeArea.getText().split("\\r?\\n");
			return args;
		}

		public CodeArea getCodeArea() {
			return codeArea;
		}

		public String getCode() {
			return codeArea.getText();
		}

		public Section getSelfSection() {
			return selfSection;
		}

		public class FileTabContextMenu extends ContextMenu {
			MenuItem mClose = new MenuItem(P_CLOSE);
			MenuItem mCloseAll = new MenuItem(CLOSE_ALL);

			public FileTabContextMenu(FileTab ft) {
				getItems().add(mClose);
				getItems().add(mCloseAll);
				mClose.setOnAction(e -> closeThis(ft));
				mCloseAll.setOnAction(e -> closeAll());
			}
		}

	}

	class ProjectNamer extends Stage {
		private Label text = new Label(NAMER_MESSAGE);
		private StackPane stack = new StackPane();
		private HBox buttons = new HBox();
		private BorderPane pane = new BorderPane();
		private Scene namerScene = new Scene(pane);
		private Button accept = new Button(OK);
		private Button cancel = new Button(CANCEL);
		private TextField namer = new TextField();

		public ProjectNamer() { // stage that shows up when starting a new project
			this.initModality(Modality.APPLICATION_MODAL);
			namer.setMaxWidth(250);
			namer.setPrefHeight(25);
			pane.setCenter(namer);
			pane.setTop(stack);
			pane.setBottom(buttons);
			buttons.setPadding(new Insets(PLATFORM_GAP));
			buttons.setAlignment(Pos.CENTER);
			buttons.setSpacing(PLATFORM_GAP);
			buttons.getChildren().add(accept);
			buttons.getChildren().add(cancel);
			stack.getChildren().add(text);
			this.setScene(namerScene);
			this.setWidth(NEW_STAGE_WIDTH);
			this.setHeight(NEW_STAGE_HEIGHT);
			this.setTitle(NEW_TITLE);
			this.show();
			this.setResizable(false);
			accept.setPrefWidth(GENERIC_BUTTON_WIDTH);
			accept.setOnAction(e -> create(namer.getText()));
			cancel.setPrefWidth(GENERIC_BUTTON_WIDTH);
			cancel.setOnAction(e -> this.close());
			namer.setOnKeyPressed(e -> handleKey(e));
		}

		public void handleKey(KeyEvent e) {
			if (e.getCode() == KeyCode.ENTER)
				create(namer.getText());
		}

		public boolean validateName(String name) {
			String fileName = namer.getText();
			// project name validation
			if (!fileName.matches("[0-9a-zA-Z\\s]+")) {
				return false;
			}
			if (fileName.length() > 20)
				return false;
			if (!fm.filenameIsAvailable(fileName)) {
				return false;
			}
			return true;
		}

		public void create(String fileName) {
			// open a new project if name is valid.
			if (validateName(fileName)) {
				Project p = new Project(
						(workSpacePath == null ? DEFAULT_WORK_SPACE : workSpacePath) + fileName + ENDING, fileName);
				fm.addProject(p);
				try {
					fm.saveProject(p);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					new ErrorStage(FILE_ERROR_MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
					new ErrorStage(IO_ERROR_MESSAGE);
				}
				currentProject = p;
				pe.addProject(p);
				closeStage();
			} else {
				namer.requestFocus();
				namer.selectAll();
				new ErrorStage(PROJECT_INPUT_ERROR);
			}
		}

		public void closeStage() {
			this.close();
		}
	}

	class SectionCreator extends ProjectNamer {
		public SectionCreator() {
			super.text.setText(NEW_SECTION_TEXT);
		}

		@Override
		public boolean validateName(String name) {
			for (Section s : currentProject.getSectionList()) {
				if (s.getName().equals(name))
					return false;
			}
			if (name.length() > 20)
				return false;
			if (name.isEmpty())
				return false;
			return true;
		}

		@Override
		public void create(String fileName) {
			if (currentProject == null) {
				new ErrorStage(NO_PROJECT_ERROR);
				return;
			}
			if (validateName(fileName)) {
				currentProject.loadSection(fileName);
				fm.updateProject(currentProject);
				try {
					fm.saveProject(currentProject);
				} catch (IOException e) {
					new ErrorStage(IO_ERROR_MESSAGE);
				}
				pe.addSection(currentProject.getSectionList().get(currentProject.getNumOfSections() - 1));
				this.closeStage();
			} else {
				super.namer.requestFocus();
				super.namer.selectAll();
				new ErrorStage(PROJECT_INPUT_ERROR);
			}
		}

	}

	class ErrorStage extends Stage { // stage for error messages.
		private ImageView error = FileManager.makeImageView(ERROR_MESSAGE);
		private Label message = new Label();
		private HBox pane = new HBox();
		private VBox vbox = new VBox();
		private Scene scene = new Scene(vbox);
		private Button ok = new Button(OK);

		public ErrorStage(String errorMessage) { // error message should be inserted in that constructor, the stage will
													// adapt it.
			this.message.setText(errorMessage);
			this.initModality(Modality.APPLICATION_MODAL);
			vbox.getChildren().add(pane);
			vbox.getChildren().add(ok);
			vbox.setAlignment(Pos.BASELINE_CENTER);
			pane.setAlignment(Pos.BASELINE_CENTER);
			pane.getChildren().add(message);
			message.setGraphic(error);
			this.setScene(scene);
			this.setWidth(ERROR_STAGE_WIDTH + errorMessage.length() * 4);
			this.setHeight(ERROR_STAGE_HEIGHT);
			this.setTitle(ERROR_TITLE);
			this.show();
			this.setResizable(false);
			this.setAlwaysOnTop(true);
			ok.requestFocus();
			ok.setPrefWidth(GENERIC_BUTTON_WIDTH);
			ok.setOnAction(e -> this.close());
		}
	}

	class BooleanStage extends Stage { // stage for Yes/No answers to gain a boolean decision from the user.
		private ImageView quest = FileManager.makeImageView(QUESTION_MESSAGE);
		private Label message = new Label(QUEST_TEMPLATE);
		private Button yes = new Button(YES);
		private Button no = new Button(NO);
		private VBox pane = new VBox();
		private HBox buttonPane = new HBox();
		private Scene scene = new Scene(pane);
		private boolean decision;

		public BooleanStage() {
			this.initModality(Modality.APPLICATION_MODAL);
			pane.getChildren().add(message);
			pane.getChildren().add(buttonPane);
			pane.setAlignment(Pos.BOTTOM_CENTER);
			buttonPane.getChildren().add(yes);
			buttonPane.getChildren().add(no);
			buttonPane.setAlignment(Pos.BOTTOM_CENTER);
			buttonPane.setPadding(new Insets(BOOLEAN_STAGE_SPACE));
			buttonPane.setSpacing(BOOLEAN_STAGE_SPACE);
			message.setGraphic(quest);
			this.setScene(scene);
			this.setWidth(QUEST_STAGE_WIDTH);
			this.setHeight(QUEST_STAGE_HEIGHT);
			this.setTitle(QUEST_TITLE);
			this.setResizable(false);
			this.setAlwaysOnTop(true);
			no.requestFocus();
			yes.setPrefWidth(GENERIC_BUTTON_WIDTH);
			no.setPrefWidth(GENERIC_BUTTON_WIDTH);
			yes.setOnAction(e -> handleYes());
			no.setOnAction(e -> handleNo());
			this.showAndWait();
		}

		public void handleYes() {
			decision = true;
			this.close();
		}

		public void handleNo() {
			decision = false;
			this.close();
		}

		public boolean getDecision() {
			return decision;
		}
	}

	class VolumeBar extends Stage{
		private Pane pane = new Pane();
		private HBox hbox = new HBox();
		private Button volumeButton = new Button();
		private Slider slider = new Slider();
		private Text volumeText;
		private Pane textPane;
		private Scene scene = new Scene(pane);
		private ImageView[] volumeLevels = {FileManager.makeImageView(VOLUME_MUTE),FileManager.makeImageView(VOLUME_LOW),FileManager.makeImageView(VOLUME_MEDIUM),FileManager.makeImageView(VOLUME_FULL)};
		private boolean mute = false;
		private int savedVolume;
		
		VolumeBar(){
			this.initStyle(StageStyle.UTILITY);
			this.setScene(scene);
			this.setAlwaysOnTop(true);
			this.setResizable(false);
			volumeButton.setGraphic(volumeLevels[3]);
			volumeButton.setPrefHeight(VOLUME_HEIGHT-10);
			pane.getChildren().add(hbox);
			pane.setPrefHeight(VOLUME_HEIGHT);
			pane.setPrefWidth(VOLUME_WIDTH);
			volumeText = new Text(String.valueOf(volumeLevel));
			volumeText.setTextAlignment(TextAlignment.CENTER);
			volumeText.setFont(Font.font("Arial", 20));
			volumeText.setY(VOLUME_HEIGHT/2);
			textPane = new Pane(volumeText);
			hbox.getChildren().addAll(volumeButton, slider, textPane);
			hbox.setAlignment(Pos.TOP_CENTER);
			hbox.setPadding(new Insets(5));
			slider.setValue(volumeLevel);
			slider.setPrefHeight(VOLUME_HEIGHT);
			slider.setPrefWidth(VOLUME_WIDTH -90);
			slider.setBlockIncrement(10);
			slider.setShowTickMarks(true);
			slider.setMin(0);
			slider.setMax(100);
			this.requestFocus();
			slider.valueProperty().addListener(e -> controlVolume(slider.getValue()));
			volumeButton.setOnAction(e -> setMute());
			controlVolume(slider.getValue());
		}

		public void wakeUp() {
			this.show();
		}
		
		public void controlVolume(double value) {
			mute = false;
			int level = (int) value;
			if (level == 0) {
				volumeButton.setGraphic(volumeLevels[0]);
			}
			else if (level > 0 && level < 34) {
				volumeButton.setGraphic(volumeLevels[1]);
			}
			else if (level >= 34 && level < 67) {
				volumeButton.setGraphic(volumeLevels[2]);
			}
			else if (level >= 67 && level <= 100) {
				volumeButton.setGraphic(volumeLevels[3]);
			}
			buttonsPane.refreshSound(level);
			menu.refreshSound(level);
			volumeLevel = level;
			volumeText.setText(String.valueOf(volumeLevel));
			if (soundGenerator != null) {
				soundGenerator.setGainByVolume(volumeLevel);
				soundGenerator.playVolume();
			}
		}
		
		public void setMute() {
			if (mute==false) {
				savedVolume = volumeLevel;
				slider.setValue(0);
				soundGenerator.setGainByVolume(0);
				mute = true;
			}
			else {
				if (savedVolume == 0)
					savedVolume = 1;
				slider.setValue(savedVolume);
				controlVolume(slider.getValue());
			}
		}
	}

}
