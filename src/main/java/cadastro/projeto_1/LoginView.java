/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastro.projeto_1;

/**
 *
 * @author Lucas
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnLogin;

    public LoginView() {
        setTitle("Login - Sistema de Biblioteca");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        panel.add(txtLogin);
        
        panel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        panel.add(txtSenha);
        
        btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = txtLogin.getText();
                String senha = new String(txtSenha.getPassword());
                autenticarUsuario(login, senha);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnLogin);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
   private void autenticarUsuario(String login, String senha) {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.autenticar(login, senha);
            
            if (usuario != null) {
                
                dispose();
                
                SwingUtilities.invokeLater(() -> {
                    new MenuPrincipalView(usuario).setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha incorretos");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao autenticar: " + ex.getMessage());
        }
    }
}