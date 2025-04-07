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


public class MenuPrincipalView extends JFrame {
    private Usuario usuarioLogado;

    public MenuPrincipalView(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        setTitle("Sistema da Biblioteca - Menu Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Botão livros
        JButton btnLivros = new JButton("Livros");
        btnLivros.addActionListener(e -> {
            new LivroView(usuarioLogado).setVisible(true);
            dispose();
        });
        
        // Botão gerenciar usuários 
        JButton btnUsuarios = new JButton("Gerenciar Usuários");
        btnUsuarios.addActionListener(e -> {
            if (usuarioLogado.getTipo().equals("professor")) {
                new UsuarioView(usuarioLogado).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Você não tem permissão para acessar esta funcionalidade", 
                    "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Botão gerenciar empréstimos
        JButton btnEmprestimos = new JButton("Gerenciar Empréstimos");
        btnEmprestimos.addActionListener(e -> {
            new EmprestimoView(usuarioLogado).setVisible(true);
            dispose();
        });
        
        panel.add(btnLivros);
        panel.add(btnUsuarios); 
        panel.add(btnEmprestimos);
        
        add(panel, BorderLayout.CENTER);
    }
}