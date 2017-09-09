/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customUnits;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author cohen
 */
public class PopupWindows {

    public static void showAboutWindow(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(PopupWindows.class.getResource("AboutPopUp.fxml"));
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root, 450, 200));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showNamingRulesWindow(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(PopupWindows.class.getResource("NamingRulesPopUp.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Naming Rules");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showHowToUseWindow(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(PopupWindows.class.getResource("HowToUsePopUp.fxml"));
            Stage stage = new Stage();
            stage.setTitle("How To Use");
            stage.setScene(new Scene(root, 600, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
