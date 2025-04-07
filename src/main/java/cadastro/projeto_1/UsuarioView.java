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
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class UsuarioView extends JFrame {
    private JTable tabelaUsuarios;
    private JButton btnAdicionar;
    private DefaultTableModel tableModel;
    private Usuario usuarioLogado;
    private UsuarioDAO usuarioDAO;

    public UsuarioView(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.usuarioDAO = new UsuarioDAO();
        
        setTitle("Gerenciamento de Usuários");
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        
        String[] colunas = {"ID", "Nome", "Login", "Tipo", "Matrícula"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaUsuarios = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAdicionar = new JButton("Adicionar Aluno");
        
        
        if (!usuarioLogado.getTipo().equals("professor")) {
            btnAdicionar.setVisible(false);
        }
        
        btnAdicionar.addActionListener(e -> abrirFormularioAdicionar());
        
        buttonPanel.add(btnAdicionar);
        
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        carregarUsuarios();
        
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        });
        
        buttonPanel.add(btnVoltar);
        
    }
    
    private void carregarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            tableModel.setRowCount(0); 
            
            for (Usuario usuario : usuarios) {                String matricula = "";
                if (usuario instanceof Aluno) {
                    matricula = ((Aluno) usuario).getMatricula();
                }
                
                Object[] rowData = {
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getLogin(),
                    usuario.getTipo(),
                    matricula
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + ex.getMessage());
        }
    }
    
    private void abrirFormularioAdicionar() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        JTextField txtNome = new JTextField();
        JTextField txtLogin = new JTextField();
        JTextField txtSenha = new JPasswordField();
        JTextField txtMatricula = new JTextField();
        
        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);
        panel.add(new JLabel("Senha:"));
        panel.add(txtSenha);
        panel.add(new JLabel("Matrícula:"));
        panel.add(txtMatricula);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Adicionar Aluno", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                Aluno novoAluno = new Aluno(
                    0, 
                    txtNome.getText(),
                    txtLogin.getText(),
                    txtSenha.getText(),
                    txtMatricula.getText()
                );
                
                usuarioDAO.cadastrar(novoAluno);
                carregarUsuarios();
                JOptionPane.showMessageDialog(this, "Aluno adicionado com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar aluno: " + ex.getMessage());
            }
        }
    }
}