import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileManager implements CompilerFinals {
	private static ImageView errorImage;
	private String workPath = "";
	private ArrayList<Project> workSpace;
	private int initVolume = 100;
	private boolean metroState = false;

	public FileManager() {
		FileInputStream inputStream = null;
		Image image;
		try {
			inputStream = new FileInputStream(ERROR_ICON);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		image = new Image(inputStream);
		errorImage = new ImageView(image);
		workSpace = new ArrayList<>();
	}

	public static ImageView makeImageView(String filePath) {
		FileInputStream input;
		try {
			input = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			return errorImage; // any bad filepaths for images will get the error icon.
		}
		Image img = new Image(input);
		ImageView imageView = new ImageView(img);
		return imageView;
	}
	
	public String getWorkPath() {
		return workPath;
	}

	public void setWorkPath(String newPath) {
		workPath = newPath;
	}
	
	public boolean filenameIsAvailable(String filename) {
		for (Project p : workSpace) {
			if (p.getName().equals(filename+ENDING))
				return false;
		}
		return true;
	}

	public void deleteProject(Project p) {
		workSpace.remove(p);
		p.delete();
	}
	
	public void deleteSection(Section s) {
		Project parentProject = s.getParent();
		for (Project p: workSpace) {
			if (p.getName().equals(parentProject.getName())){
				parentProject.removeSection(s);
			}
		}
	}
	
	public void saveWorkSpace() throws FileNotFoundException, IOException {
		RandomAccessFile workSave = new RandomAccessFile(new File(WORK_SPACE_FILE), "rw");
		workSave.writeUTF(IDENTIFYING_STRING);
		workSave.writeInt(initVolume);
		workSave.writeBoolean(metroState);
		workSave.writeUTF(workPath);
		workSave.writeInt(workSpace.size());
		for (Project p : workSpace) {
			workSave.writeUTF(p.getAbsolutePath());
		}
		workSave.close();
	}

	public void loadWorkSpace() throws FileNotFoundException, IOException, ProjectNotFoundException {
		RandomAccessFile load = new RandomAccessFile(new File(WORK_SPACE_FILE), "rw");
		String id = load.readUTF();
		if (!id.equals(IDENTIFYING_STRING)) {
			load.close();
			throw new FileNotFoundException("Settings file is corrupted or is not supported.");
		}
		initVolume = load.readInt();
		metroState = load.readBoolean();
		workPath = load.readUTF();
		int numOfProjects = load.readInt();
		for (int i = 0; i < numOfProjects; i++)
			addProject(loadProject(load.readUTF()));
		load.close();
	}

	public void saveProject(Project p) throws IOException, FileNotFoundException {
		RandomAccessFile save = new RandomAccessFile(p, "rw");
		save.writeUTF(IDENTIFYING_STRING);  // the program will decide if a file is readable only if this string exists
											// in the start
		save.writeUTF(p.getName());
		save.writeInt(p.getNumOfSections());
		for (Section c : p.getSectionList()) {
			save.writeUTF(c.getName());
			save.writeUTF(c.getArgs());
		}
		save.close();
	}

	public ArrayList<Project> getWorkSpace() {
		return workSpace;
	}

	public Project loadProject(String path) throws ProjectNotFoundException {
		try {
		RandomAccessFile load = new RandomAccessFile(new File(path), "rw");
		if (!load.readUTF().equals(IDENTIFYING_STRING)) {
			load.close();
			throw new ProjectNotFoundException();
		}
		Project p = new Project(path, load.readUTF());
		int classes = load.readInt();
		for (int i = 0; i < classes; i++) {
			p.loadSection(load.readUTF());
			p.getSectionList().get(i).setArgs(load.readUTF());
		}
		load.close();
		return p;
		}catch(IOException e) {
			throw new ProjectNotFoundException();
		}
	}

	public void addProject(Project p) { // addProject must be used everytime a project is added to the workspace. also
										// when opening a new project.
		if (!workSpace.contains(p))
			workSpace.add(p);
	}
	
	public void updateProject(Project p){
		String projectName = p.getName();
		for (int i=0 ; i<workSpace.size() ; i++) {
			if (workSpace.get(i).getName().equals(projectName)) {
				workSpace.set(i, p);
				return;
			}
		}
	}
	
	
	public void setInitVolume(int volume) {
		initVolume = volume;
	}
	
	public void setMetroState(boolean state) {
		metroState = state;
	}
	
	public int getInitVolume() {
		return initVolume;
	}
	
	public boolean getMetroState() {
		return metroState;
	}
	
	public Project convertFileToProject(File f) throws ProjectNotFoundException {
		Project p = loadProject(f.getPath());
		return p;
	}

	public void resetWorkspace() { //recreate the workspace when there is a problem
		// creates the directory for the saved files if not exists
		
		// gets the main drive to open a workspace.
		String workspacePath = null;
		File[] paths;
		paths = File.listRoots();
		//assumes the first drive is the drive for workspace
		workspacePath = paths[0].toString() + "bJoven";
		
		File bjovenDir = new File(workspacePath);
		bjovenDir.mkdir();
		workspacePath += "\\Projects";
		File projectDir = new File(workspacePath);
		projectDir.mkdir();
		this.workPath = workspacePath;
	}
}

class ProjectNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
}

class Project extends File {
	private static final long serialVersionUID = 1L;
	//
	private String fileName;
	private ArrayList<Section> sections = new ArrayList<>();
	
	public Project(String path, String fileName) {
		super(path);
		this.fileName = fileName;
	}

	public void loadSection(String sectionName) {
		sections.add(new Section(sectionName, this,sections.size()));
	}

	public ArrayList<Section> getSectionList() {
		return sections;
	}

	public void removeSection(Section s) {
		for (Section sect : sections) {
			if (sect.equals(s)){
				sections.remove(sect);
				return;
			}
		}
	}
	
	public int getNumOfSections() {
		return sections.size();
	}
	
	public String getFileName() {
		return fileName;
	}

	
	@Override
	public String toString() { // toString determine the TreeViews names
		return fileName;
	}
}

class Section {	
	private String name;
	private String args = "";
	private Project parent;
	private int indexAtProjArray;
	
	public Section(String sectionName, Project owner, int index) {
		name = sectionName;
		parent = owner;
		indexAtProjArray = index;
	}

	public int getIndexAtProjArray() {
		return indexAtProjArray;
	}
	
	public Project getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}

	public void setArgs(String code) {
		args = code;
	}

	public String getArgs() {
		return args;
	}

	@Override
	public String toString() { // toString determine the TreeViews names
		return name;
	}
}
