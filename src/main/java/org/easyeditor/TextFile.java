package org.easyeditor;

import javafx.scene.control.Alert;

import java.io.*;

public class TextFile {

    private File file = null;
    private String textInFile="";

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTextInFile() {
        return textInFile;
    }

    public void setTextInFile(String textInFile) {
        this.textInFile = textInFile;
    }

    public void load(File file) {
        if (file != null) {
            this.file = file;
            textInFile = loadContent(file);
        }
    }

    public void saveFile() throws IOException {
        FileWriter fileWriter = new FileWriter(this.file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        fileWriter.write(textInFile);
        bufferedWriter.close();
    }

    private String loadContent(File file) {
        String line = null;
        String fullText = "";
        try {
            FileReader fileReader = new FileReader(file.toString());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                fullText = fullText.concat(line + '\n');
            }
            bufferedReader.close();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Error reading file '" + file.toString() + "'");
            alert.show();
        }
        return fullText;
    }

}
