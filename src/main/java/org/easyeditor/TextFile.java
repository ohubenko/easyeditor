package org.easyeditor;


import javafx.scene.control.Alert;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TextFile {

    private File file = null;
    private String textInFile = "";

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
            this.textInFile = loadContent(this.file);
        }
    }

    public void saveFile() throws IOException {
        FileWriter fileWriter = new FileWriter(this.file, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        fileWriter.write(textInFile);
        bufferedWriter.close();
    }

    private String loadContent(File file) {
        String line = null;
        String fullText = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                    StandardCharsets.UTF_8));
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
