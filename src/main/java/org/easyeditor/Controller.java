package org.easyeditor;


import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


import com.sun.scenario.effect.Blend;
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

    private Model model = new Model();
    private DBHandler dbHandler = new DBHandler();

    @FXML
    void onClose() {
        System.exit(0);
    }

    @FXML
    void onNew() {
        areaText.setText("");
        this.model.clear();
        statusBar.getChildren().clear();
    }

    @FXML
    void onSave() {
        if (!areaText.getText().equals(model.getOpenFile().getTextInFile())) {
            if (this.model.getOpenFile().getFile() == null)
                onSaveAs();
            else {
                String text = areaText.getText();
                try {
                    model.saveFile(text);
                    dbHandler.add(this.model.getOpenFile().getFile(), new Timestamp(System.currentTimeMillis()));
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Error saving file '" + this.model.getOpenFile().getFile().toString() + "'");
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
                this.model.saveFile(text,file);
                statusBar.getChildren().clear();
                statusBar.getChildren().add(new Label(file.toString()));
                this.model.getOpenFile().load(file);
                this.model.getOpenFile().setTextInFile(areaText.getText());
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
        this.model.loadFile(file);
        areaText.setText(this.model.getOpenFile().getTextInFile());
        statusBar.getChildren().clear();
        statusBar.getChildren().add(new Label(file.toString()));
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
                        this.model.getOpenFile().load(file);
                        statusBar.getChildren().clear();
                        statusBar.getChildren().add(new Label(file.toString()));
                        areaText.setText(this.model.getOpenFile().getTextInFile());
                    }

                });
                mHistory.getItems().add(mi);
            }
        }catch (SQLException ex){
            ex.getMessage();
        }
    }

}
