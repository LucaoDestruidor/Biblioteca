/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastro.projeto_1;

/**
 *
 * @author Lucas
 */
;



import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import javax.swing.UIManager;

public class  Main{
    
    
    public static void main(String[] args) {
    try {
        
        File driverFile = new File("lib/mysql-connector-java-8.0.xx.jar");
        URLClassLoader child = new URLClassLoader(
            new URL[]{driverFile.toURI().toURL()},
            Main.class.getClassLoader()
        );
        Thread.currentThread().setContextClassLoader(child);
    } catch (Exception e) {
        e.printStackTrace();
    }
    
 

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar com  tela de login
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }
}