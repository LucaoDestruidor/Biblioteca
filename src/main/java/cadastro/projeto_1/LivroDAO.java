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

public class LivroDAO {
    
    public void cadastrar(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, autor, isbn, disponivel, reservado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setBoolean(4, livro.isDisponivel());
            stmt.setBoolean(5, livro.isReservado());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    livro.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("isbn"),
                    rs.getBoolean("disponivel"),
                    rs.getBoolean("reservado")
                );
                livros.add(livro);
            }
        }
        
        return livros;
    }
    
    public List<Livro> buscarPorTitulo(String titulo) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE LOWER(titulo) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + titulo + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getBoolean("disponivel"),
                        rs.getBoolean("reservado")
                    );
                    livros.add(livro);
                }
            }
        }
        
        return livros;
    }
    
    public Livro buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM livros WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getBoolean("disponivel"),
                        rs.getBoolean("reservado")
                    );
                }
            }
        }
        
        return null;
    }
    
    
    public void atualizar(Livro livro) throws SQLException {
    String sql = "UPDATE livros SET titulo = ?, autor = ?, isbn = ?, disponivel = ?, reservado = ? WHERE id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, livro.getTitulo());
        stmt.setString(2, livro.getAutor());
        stmt.setString(3, livro.getIsbn());
        stmt.setBoolean(4, livro.isDisponivel());
        stmt.setBoolean(5, livro.isReservado());
        stmt.setInt(6, livro.getId());
        
        stmt.executeUpdate();
    }
}
    
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public void marcarComoDisponivel(int livroId, boolean reservado) throws SQLException {
        String sql = "UPDATE livros SET disponivel = ?, reservado = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, true); 
            stmt.setBoolean(2, reservado); 
            stmt.setInt(3, livroId);
            
            stmt.executeUpdate();
        }
    }
    
    public void marcarComoIndisponivel(int livroId) throws SQLException {
        String sql = "UPDATE livros SET disponivel = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, false);
            stmt.setInt(2, livroId);
            
            stmt.executeUpdate();
        }
    }
    
    public void marcarComoReservado(int livroId, boolean reservado) throws SQLException {
    String sql = "UPDATE livros SET reservado = ? WHERE id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setBoolean(1, reservado);
        stmt.setInt(2, livroId);
        stmt.executeUpdate();
       }
    }   
    

    public boolean isbnExiste(String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM livros WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            
        
        return false;
    }
            
}
    }
}
