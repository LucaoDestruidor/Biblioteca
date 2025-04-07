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


public class EmprestimoView extends JFrame {
    private JTable tabelaEmprestimos;
    private DefaultTableModel tableModel;
    private Usuario usuarioLogado;
    private EmprestimoDAO emprestimoDAO;

    public EmprestimoView(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.emprestimoDAO = new EmprestimoDAO();
        
        setTitle("Gerenciamento de Empréstimos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        String[] colunas = {"ID", "Livro ID", "Usuário", "Tipo", "Data Empréstimo", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEmprestimos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaEmprestimos);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        
        carregarEmprestimos();
        
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnVoltar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        
    }
    
    private void carregarEmprestimos() {
        try {
            List<Emprestimo> emprestimos = emprestimoDAO.listarTodos();
            tableModel.setRowCount(0); 
            
            for (Emprestimo emprestimo : emprestimos) {
                Object[] rowData = {
                    emprestimo.getId(),
                    emprestimo.getLivroId(),
                    emprestimo.getUsuarioNome(),
                    emprestimo.getUsuarioTipo(),
                    emprestimo.getDataEmprestimo().toLocalDate().toString(),
                    emprestimo.getStatus()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + ex.getMessage());
        }
    }
}