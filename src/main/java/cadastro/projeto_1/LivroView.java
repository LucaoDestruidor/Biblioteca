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

public class LivroView extends JFrame {
    private JTable tabelaLivros;
    private DefaultTableModel tableModel;
    private Usuario usuarioLogado;
    private LivroDAO livroDAO;
    private JTextField txtBusca;

    public LivroView(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.livroDAO = new LivroDAO();
        
        setTitle("Gerenciamento de Livros");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblBusca = new JLabel("Buscar livro:");
        txtBusca = new JTextField();
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpar = new JButton("Limpar");
        
        
        JPanel buscaPanel = new JPanel(new BorderLayout(5, 5));
        buscaPanel.add(lblBusca, BorderLayout.WEST);
        buscaPanel.add(txtBusca, BorderLayout.CENTER);
        
        
        JPanel buscaButtonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buscaButtonPanel.add(btnBuscar);
        buscaButtonPanel.add(btnLimpar);
        
        buscaPanel.add(buscaButtonPanel, BorderLayout.EAST);
        topPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Configuração da tabela
        String[] colunas = {"ID", "Título", "Autor", "ISBN", "Disponível", "Reservado"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaLivros = new JTable(tableModel);
        tabelaLivros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        
     
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnReservar = new JButton("Reservar");
        JButton btnVoltar = new JButton("Voltar");
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        if (!usuarioLogado.getTipo().equals("bibliotecario")) {
            btnAdicionar.setVisible(false);
            btnEditar.setVisible(false);
            btnRemover.setVisible(false);
        }
        
        
        btnBuscar.addActionListener(e -> buscarLivros());
        btnLimpar.addActionListener(e -> {
            txtBusca.setText("");
            carregarLivros();
        });
        txtBusca.addActionListener(e -> buscarLivros());
        
        btnAdicionar.addActionListener(e -> abrirFormularioAdicionar());
        btnEditar.addActionListener(e -> editarLivro());
        btnRemover.addActionListener(e -> removerLivro());
        btnReservar.addActionListener(e -> reservarLivro());
        btnVoltar.addActionListener(e -> {
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        });
        
        
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnReservar);
        buttonPanel.add(btnVoltar);
        
        
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        carregarLivros();
    }
    
    private void buscarLivros() {
        String termoBusca = txtBusca.getText().trim();
        
        try {
            List<Livro> livros;
            
            if (termoBusca.isEmpty()) {
                livros = livroDAO.listarTodos();
            } else {
                livros = livroDAO.buscarPorTitulo(termoBusca);
                
                if (livros.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Nenhum livro encontrado com o termo: " + termoBusca, 
                        "Busca", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            atualizarTabela(livros);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar livros: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarLivros() {
        try {
            List<Livro> livros = livroDAO.listarTodos();
            atualizarTabela(livros);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar livros: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0); 
        
        for (Livro livro : livros) {
            Object[] rowData = {
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.isDisponivel() ? "Sim" : "Não",
                livro.isReservado() ? "Sim" : "Não"
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void abrirFormularioAdicionar() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtIsbn = new JTextField();
        
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Autor:"));
        panel.add(txtAutor);
        panel.add(new JLabel("ISBN:"));
        panel.add(txtIsbn);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Adicionar Livro", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                if (txtTitulo.getText().trim().isEmpty() || 
                    txtAutor.getText().trim().isEmpty() || 
                    txtIsbn.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Todos os campos são obrigatórios");
                }
                
                if (livroDAO.isbnExiste(txtIsbn.getText())) {
                    throw new IllegalArgumentException("Já existe um livro com este ISBN");
                }
                
                Livro novoLivro = new Livro(
                    0, 
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    txtIsbn.getText(),
                    true,  
                    false  
                );
                
                livroDAO.cadastrar(novoLivro);
                carregarLivros();
                JOptionPane.showMessageDialog(this, 
                    "Livro adicionado com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao adicionar livro: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, 
                    ex.getMessage(), 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void editarLivro() {
    int selectedRow = tabelaLivros.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Selecione um livro para editar", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int livroId = (int) tableModel.getValueAt(selectedRow, 0);
    try {
        Livro livro = livroDAO.buscarPorId(livroId);
        boolean reservadoOriginal = livro.isReservado();
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtTitulo = new JTextField(livro.getTitulo());
        JTextField txtAutor = new JTextField(livro.getAutor());
        JTextField txtIsbn = new JTextField(livro.getIsbn());
        JCheckBox chkDisponivel = new JCheckBox("Disponível", livro.isDisponivel());
        JCheckBox chkReservado = new JCheckBox("Reservado", livro.isReservado());
        
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Autor:"));
        panel.add(txtAutor);
        panel.add(new JLabel("ISBN:"));
        panel.add(txtIsbn);
        panel.add(new JLabel("Status:"));
        panel.add(chkDisponivel);
        panel.add(new JLabel("Reserva:"));
        panel.add(chkReservado);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Editar Livro", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            livro.setTitulo(txtTitulo.getText());
            livro.setAutor(txtAutor.getText());
            livro.setIsbn(txtIsbn.getText());
            livro.setDisponivel(chkDisponivel.isSelected());
            livro.setReservado(chkReservado.isSelected());
            
            
            if (reservadoOriginal && !livro.isReservado()) {
                EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
                emprestimoDAO.removerReservaPorLivro(livroId);
            }
            
            livroDAO.atualizar(livro);
            carregarLivros();
            JOptionPane.showMessageDialog(this, 
                "Livro atualizado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Erro ao editar livro: " + ex.getMessage(), 
            "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
    
   private void removerLivro() {
    int selectedRow = tabelaLivros.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Selecione um livro para remover", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int livroId = (int) tableModel.getValueAt(selectedRow, 0);
    int confirm = JOptionPane.showConfirmDialog(
        this, "Tem certeza que deseja remover este livro?", 
        "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Primeiro remove os empréstimos/reservas associados
            EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
            emprestimoDAO.removerReservaPorLivro(livroId);
            
            // Depois remove o livro
            livroDAO.excluir(livroId);
            
            carregarLivros();
            JOptionPane.showMessageDialog(this, 
                "Livro removido com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao remover livro: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    private void reservarLivro() {
    int selectedRow = tabelaLivros.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Selecione um livro para reservar", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int livroId = (int) tableModel.getValueAt(selectedRow, 0);
    String disponivel = (String) tableModel.getValueAt(selectedRow, 4);
    String reservado = (String) tableModel.getValueAt(selectedRow, 5);
    
    if (disponivel.equals("Não") || reservado.equals("Sim")) {
        JOptionPane.showMessageDialog(this, 
            "Este livro não está disponível para reserva", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        
        livroDAO.marcarComoIndisponivel(livroId);
        livroDAO.marcarComoReservado(livroId, true);
        
        
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        Emprestimo reserva = new Emprestimo(
            0, 
            livroId,
            usuarioLogado.getId(),
            null, 
            null, 
            null, 
            "reservado", 
            new java.math.BigDecimal("0.00") 
        );
        
        emprestimoDAO.registrarEmprestimo(reserva);
        
        carregarLivros();
        JOptionPane.showMessageDialog(this, 
            "Livro reservado com sucesso!", 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Erro ao reservar livro: " + ex.getMessage(), 
            "Erro", JOptionPane.ERROR_MESSAGE);
    }

        
        try {
            // Atualizar o status do livro
            livroDAO.marcarComoIndisponivel(livroId);
            
            
            carregarLivros();
            JOptionPane.showMessageDialog(this, 
                "Livro reservado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao reservar livro: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}