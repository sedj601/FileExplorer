/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javax.swing.filechooser.FileSystemView;
import org.controlsfx.control.PopOver;

/**
 *
 * @author blj0011
 */
public class FXMLDocumentController implements Initializable {
      
    
    @FXML VBox vbSidePanel;
    @FXML ListView lvFiles;
    
    ObservableList<File> data =FXCollections.observableArrayList ();

    public FXMLDocumentController() {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        // returns pathnames for files and directory
        paths = File.listRoots();

        // for each pathname in pathname array
        FontAwesomeIconView usbIcon = new FontAwesomeIconView(FontAwesomeIcon.USB);
        
        for(File path:paths)
        {
            // prints file and directory paths
//            System.out.println("Drive Name: "+ path);
//            System.out.println("Description: "+ fsv.getSystemTypeDescription(path));
            vbSidePanel.getChildren().add(new StackPane(getView(usbIcon, path, fsv.getSystemTypeDescription(path))));
        }
        
        lvFiles.setItems(data);
        lvFiles.setCellFactory(lv -> new ListCell<File>(){
            @Override
            public void updateItem(File item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {         
                    setText(item.getName());
                    if(Files.isDirectory(item.toPath()))
                    {
                        setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FOLDER));                            
                    }
                    else
                    {
                        setGraphic(null);
                    }
                    
                    setOnMouseClicked(mouseEvent ->{
                        if(Files.isDirectory(item.toPath()))
                        {
                            data.setAll(listNonHiddenFilesForFolder(item));
                        }
                        else
                        {
                            mouseEvent.consume();
                        }
                    });
                }                
            }
        });
    }    
    
    public Button getView(FontAwesomeIconView view, File path, String driveDescription)
    {
        Button usbButton = GlyphsDude.createIconButton(FontAwesomeIcon.USB, path.toString(), "48.0", "20.0", ContentDisplay.TOP);
        usbButton.setOnAction(actionEvent -> {
            data.setAll(listNonHiddenFilesForFolder(path));
        });
        
        return usbButton;
    }
    
    public List<File> listNonHiddenFilesForFolder(final File folder) {
        File[] listOfFiles = folder.listFiles();
        
        List<File> nonHiddenFiles = new ArrayList(Arrays.asList(listOfFiles));
        return nonHiddenFiles.stream().filter(p -> {
            try
            {
                return !Files.isHidden(p.toPath());
            }
            catch(IOException ex) 
            {                
                return false;
            }                               
        }).collect(Collectors.toList());
    }
    
}
