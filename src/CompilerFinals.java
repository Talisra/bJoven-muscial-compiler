
public interface CompilerFinals {
	//stage strings:
	final static String TITLE = "bJoven";
	final static String PLAYER_TITLE = "Player";
	final static String NEW_TITLE = "New Project";
	final static String ERROR_TITLE = "Error";
	final static String QUEST_TITLE = "";
	final static String PACKAGE_NAME = "Project Explorer";
	
	//visual values:
	final static int MINIMIZED_WIDTH = 1040;
	final static int MINIMIZED_HEIGHT = 680;
	final static int BOTTOM_PANEL_HEIGHT = 25;
	final static int EXPLORER_HEIGHT = 1000;
	final static int EXPLORER_WIDTH = 400;
	final static int BUTTON_HEIGHT = 25;
	final static double SPLIT_GAP = 0.2;
	final static int PLATFORM_GAP = 4;
	final static int ICONS_GAP = 2;
	final static int ICONS_SIZE = 25;
	final static int CODE_AREA_WIDTH = 2000;
	final static int CODE_AREA_HEIGHT = 1000;
	
	final static double MAIN_PANE_MMULTIPLIER = 0.982;
	final static double EXPLORER_BIND_VALUE = 0.1;
	final static double NUMBERS_SPACE_FROM_TOP = 3.5;
	
	final static int NEW_STAGE_WIDTH = 300;
	final static int NEW_STAGE_HEIGHT = 125;
	
	final static int ERROR_STAGE_HEIGHT = 120;
	final static int ERROR_STAGE_WIDTH = 200;
	final static int QUEST_STAGE_HEIGHT = 150;
	final static int QUEST_STAGE_WIDTH = 250;
	final static int GENERIC_BUTTON_WIDTH = 60;
	final static int BOOLEAN_STAGE_SPACE = 20;
	
	final static int PLAYER_STAGE_HEIGHT = 250;
	final static int PLAYER_STAGE_WIDTH = 600;
	
	final static int VOLUME_HEIGHT = 60;
	final static int VOLUME_WIDTH = 325;
	
	//button array index:
	final static int BUTTON_NEW = 0;
	final static int BUTTON_OPEN = 1;
	final static int BUTTON_SAVE = 2;
	final static int BUTTON_SAVEALL = 3;
	final static int BUTTON_UNDO = 4;
	final static int BUTTON_REDO = 5;
	final static int BUTTON_COPY = 6;
	final static int BUTTON_CUT = 7;
	final static int BUTTON_PASTE = 8;
	final static int BUTTON_PLAY = 9;
	final static int BUTTON_VOLUME = 10;
	final static int BUTTON_SEARCH = 11;

	
	//Explorer menu:
	final static String CHANGE_WORKSPACE = "Change Workspace";
	final static String OPEN_WORKSPACE = "Open Workspace";
	final static String ADD_PROJ = "Add new project";
	final static String P_CLOSE = "Close";
	final static String P_DELETE = "Delete";
	
	final static String OPEN_SECTION = "Open";
	final static String S_DELETE = "Delete";
	final static String ADD_SECTION = "Add new section";

	
	//initial values:
	final static String KEYWORDS_CSS = "lang-keywords.css";
	final static String ENDING = ".bjm";
	final static String DEFAULT_WORK_SPACE = "C:\\bJoven\\Projects\\";
	final static String WORK_SPACE_FILE = "workspace.wsp";
	final static String IDENTIFYING_STRING = "URW6-Q3JQ-RTNH-S6AV"; //serial code to identify .bjm files. DO NOT CHANGE!

	final static String PROJECT = "Project";
	final static String SECTION = "Section";
	
	final static int INITIAL_LINES = 100;
	final static int MAX_TABS_SIMULTANEOUS = 50;
	
	final static int TOTAL_FILE_BUTTONS = 4;
	final static int TOTAL_EDIT_BUTTONS = 5;
	final static int TOTAL_APP_BUTTONS = 3;
	final static int TOTAL_PLATFORMS = 3;
	
	// icons paths:
	final static String ERROR_ICON = "ico/error.png"; //this icon path should NOT be changed!
	
	final static String PROJECT_ICON = "ico/project.png";
	final static String TREE_ICON = "ico/tree-icon.png";
	
	final static String NEW_FILE_ICON = "ico/new-file.png";
	final static String OPEN_ICON = "ico/open.png";
	final static String SAVE_ICON = "ico/save-big.png";
	final static String SAVEALL_ICON = "ico/multisave-big.png";
	final static String UNDO_ICON = "ico/undo.png";
	final static String REDO_ICON = "ico/redo.png";
	final static String COPY_ICON = "ico/copy.png";
	final static String CUT_ICON = "ico/cut.png";
	final static String PASTE_ICON = "ico/paste.png";
	final static String PLAY_ICON = "ico/play.png";
	
	final static String VOLUME_FULL_SMALL = "ico/volume_full_small.png";
	final static String VOLUME_MEDIUM_SMALL = "ico/volume_medium_small.png";
	final static String VOLUME_LOW_SMALL = "ico/volume_low_small.png";
	final static String VOLUME_MUTE_SMALL = "ico/volume_mute_small.png";
	
	final static String METRONOME_ICON = "ico/metronome.png";
	final static String SEARCH_ICON = "ico/search.png";
	
	final static String TAB_ICON = "ico/tab.png";
	final static String FOLDERS_ICON = "ico/folders.png";
	final static String SECTION_ICON = "ico/class.png";
	
	final static String MENU_SAVE = "ico/save-tiny.png";
	final static String MENU_SAVEALL = "ico/multisave-tiny.png";
	
	final static String EDIT_CUT = "ico/cut16.png";
	final static String EDIT_COPY = "ico/copy16.png";
	final static String EDIT_PASTE = "ico/paste16.png";
	final static String EDIT_UNDO = "ico/undo16.png";
	final static String EDIT_REDO = "ico/redo16.png";
	final static String EDIT_DELETE = "ico/delete16.png";
	
	final static String PLAYER_MUTE = "ico/mute_miniicon.png";
	final static String PLAYER_UNMUTE = "ico/volume_miniicon.png";
	final static String PLAYER_METRONOME = "ico/metronome-icon-tiny.png";
	
	final static String OKAY_MESSAGE  = "ico/accept.png";
	final static String ERROR_MESSAGE = "ico/error-message.png";
	final static String QUESTION_MESSAGE = "ico/quest-message.png";
	
	final static String PLAYER_PAUSE = "ico/player-pause.png";
	final static String PLAYER_PLAY = "ico/player-play.png";
	final static String PLAYER_STOP = "ico/player-stop.png";
	
	final static String VOLUME_FULL = "ico/volume_full.png";
	final static String VOLUME_MEDIUM = "ico/volume_medium.png";
	final static String VOLUME_LOW = "ico/volume_low.png";
	final static String VOLUME_MUTE = "ico/volume_mute.png";
	
	
	//message strings:
	final static String NAMER_MESSAGE = "Enter project name:";
	final static String NEW_SECTION_TEXT = "Section name: ";
	final static String PROJECT_INPUT_ERROR = "Invalid name!";
	final static String FILE_ERROR_MESSAGE = "The requested path cannot be found";
	final static String FILE_NOT_SUPPORTED = "The file is corrupted or is not supported";
	final static String IO_ERROR_MESSAGE = "A problem occured writing to the file";
	final static String WORKSPACE_ERROR_MESSAGE = "Workspace couldn't be saved or loaded properly";
	final static String NO_PROJECT_ERROR = "Select a project before adding a section!";
	final static String QUEST_TEMPLATE = "Are you sure?";
	
	//buttons strings:
	final static String OK = "Ok";
	final static String CANCEL = "Cancel";
	final static String YES = "Yes";
	final static String NO = "No";
	final static String CLOSE_ALL = "Close All";
}
