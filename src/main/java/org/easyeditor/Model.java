package org.easyeditor;

import java.io.File;
import java.io.IOException;

public class Model {

    private TextFile openFile = new TextFile();

    public TextFile getOpenFile() {
        return openFile;
    }

    public void clear() {
        this.openFile = null;
    }

    public void loadFile(File file) {
        openFile.load(file);
    }

    public void saveFile(String text) throws IOException {
        this.openFile.setTextInFile(text);
        this.openFile.saveFile();
    }

    public void saveFile(String text, File file) throws IOException {
        this.openFile.setTextInFile(text);
        this.openFile.setFile(file);
        this.openFile.saveFile();
    }
}
