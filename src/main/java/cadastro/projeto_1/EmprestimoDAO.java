/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastro.projeto_1;

/**
 *
 * @author Lucas
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EmprestimoDAO {
    public void registrarEmprestimo(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimos (livro_id, usuario_id, data_emprestimo, " +
                     "data_devolucao_prevista, data_devolucao_real, status, multa) " +
                     "VALUES (?, ?, NOW(), ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, emprestimo.getLivroId());
            stmt.setInt(2, emprestimo.getUsuarioId());
            
            if (emprestimo.getDataDevolucaoPrevista() != null) {
                stmt.setDate(3, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            if (emprestimo.getDataDevolucaoReal() != null) {
                stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucaoReal()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setString(5, emprestimo.getStatus());
            stmt.setBigDecimal(6, emprestimo.getMulta());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getInt(1));
                }
            }
        }
    }
    
    
    
    
    
    public void marcarComoDevolvido(int emprestimoId) throws SQLException {
        String sql = "UPDATE emprestimos SET status = 'devolvido', data_devolucao_real = CURRENT_DATE WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, emprestimoId);
            stmt.executeUpdate();
        }
    }

    
   
    
    
    
    
    public List<Emprestimo> listarReservas() throws SQLException {
        List<Emprestimo> reservas = new ArrayList<>();
        String sql = "SELECT e.*, u.nome, u.tipo FROM emprestimos e " +
                     "JOIN usuarios u ON e.usuario_id = u.id " +
                     "WHERE e.status = 'reservado'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprestimo reserva = new Emprestimo(
                    rs.getInt("id"),
                    rs.getInt("livro_id"),
                    rs.getInt("usuario_id"),
                    rs.getTimestamp("data_emprestimo").toLocalDateTime(),
                    rs.getDate("data_devolucao_prevista") != null ? 
                        rs.getDate("data_devolucao_prevista").toLocalDate() : null,
                    rs.getDate("data_devolucao_real") != null ? 
                        rs.getDate("data_devolucao_real").toLocalDate() : null,
                    rs.getString("status"),
                    rs.getBigDecimal("multa")
                );
                
                reserva.setUsuarioNome(rs.getString("nome"));
                reserva.setUsuarioTipo(rs.getString("tipo"));
                
                reservas.add(reserva);
            }
        }
        
        return reservas;
    }
    
    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.*, u.nome, u.tipo FROM emprestimos e " +
                     "JOIN usuarios u ON e.usuario_id = u.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo(
                    rs.getInt("id"),
                    rs.getInt("livro_id"),
                    rs.getInt("usuario_id"),
                    rs.getTimestamp("data_emprestimo").toLocalDateTime(),
                    rs.getDate("data_devolucao_prevista") != null ? 
                        rs.getDate("data_devolucao_prevista").toLocalDate() : null,
                    rs.getDate("data_devolucao_real") != null ? 
                        rs.getDate("data_devolucao_real").toLocalDate() : null,
                    rs.getString("status"),
                    rs.getBigDecimal("multa")
                );
                
                emprestimo.setUsuarioNome(rs.getString("nome"));
                emprestimo.setUsuarioTipo(rs.getString("tipo"));
                
                emprestimos.add(emprestimo);
            }
        }
        
        return emprestimos;
        
        
        
        
    }

        
        public void removerReservaPorLivro(int livroId) throws SQLException {
    String sql = "DELETE FROM emprestimos WHERE livro_id = ? AND status = 'reservado'";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, livroId);
        stmt.executeUpdate();
    }
}
        
}
        
        
        
