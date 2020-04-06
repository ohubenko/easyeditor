package org.easyeditor;


import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

public class Controller {

    @FXML
    private HBox statusBar;

    @FXML
    private TextArea areaText;

    @FXML
    private Menu mHistory;

    private String textInFile;

    private File file = null;
    private DBHandler dbHandler = new DBHandler();

    @FXML
    void onClose() {
        System.exit(0);
    }

    @FXML
    void onNew() {
        areaText.setText("");
        this.file = null;
        statusBar.getChildren().clear();
    }

    @FXML
    void onSave() {
        if (!areaText.getText().equals(textInFile)) {
            if (this.file == null)
                onSaveAs();
            else {
                String text = areaText.getText();
                try {
                    saveFile(this.file, text);
                    dbHandler.add(this.file, new Timestamp(System.currentTimeMillis()));
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Error saving file '" + this.file.toString() + "'");
                    alert.show();
                }
            }

        }
    }

    @FXML
    void onSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("EasyEditor-File Saver");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialDirectory(new File("./"));
        File file = fileChooser.showSaveDialog(statusBar.getScene().getWindow());
        String text = areaText.getText();
        if (file != null) {
            try {
                saveFile(file, text);
                statusBar.getChildren().clear();
                statusBar.getChildren().add(new Label(file.toString()));
                this.file = file;
                this.textInFile = areaText.getText();
                dbHandler.add(file, new Timestamp(System.currentTimeMillis()));
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Error saving file '" + file.toString() + "'");
                alert.show();
            }
        }

    }

    @FXML
    void onLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("EasyEditor-FileChooser");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialDirectory(new File("./"));
        File file = fileChooser.showOpenDialog(statusBar.getScene().getWindow());
        System.out.println(fileChooser.getInitialDirectory());
        if (file != null) {
            areaText.setText(loadContent(file));
            statusBar.getChildren().clear();
            statusBar.getChildren().add(new Label(file.toString()));
            this.file = file;
        }

    }

    @FXML
    void onPrint() {
        TextFlow printArea = new TextFlow(new Text(areaText.getText()));
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(areaText.getScene().getWindow())) {
            PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();
            printArea.setMaxWidth(pageLayout.getPrintableWidth());
            if (printerJob.printPage(printArea)) {
                printerJob.endJob();
            } else {
                System.out.println("Failed to print");
            }
        } else {
            System.out.println("Canceled");
        }
    }

    @FXML
    void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("About");
        alert.setContentText("This is a simple text editor for Term Paper.\n" +
                "Dev by Gubenko O.V.");
        alert.show();
    }

    @FXML
    void onHistory() {
        System.out.println("OK");
        ResultSet rs = dbHandler.getLastFiles();
        try{
            mHistory.getItems().clear();
            while(rs.next()){
                String fileName = rs.getString("namefile");
                MenuItem mi = new MenuItem();
                mi.setText(fileName);
                mi.setOnAction(event -> {
                    File file = new File(fileName);
                    if (file != null) {
                        areaText.setText(loadContent(file));
                        statusBar.getChildren().clear();
                        statusBar.getChildren().add(new Label(file.toString()));
                        this.file = file;
                    }

                });
                mHistory.getItems().add(mi);
            }
        }catch (SQLException ex){
            ex.getMessage();
        }
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
        this.textInFile = fullText;
        return fullText;
    }

    private void saveFile(File file, String text) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        fileWriter.write(text);
        bufferedWriter.close();
    }

}
