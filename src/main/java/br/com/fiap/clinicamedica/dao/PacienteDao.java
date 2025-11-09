package br.com.fiap.clinicamedica.dao;

import br.com.fiap.clinicamedica.exception.EntidadeNaoEncontradaException;
import br.com.fiap.clinicamedica.model.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PacienteDao {

    @Inject
    DataSource dataSource;

    public Paciente cadastrar(Paciente paciente) {
        String sql = "INSERT INTO T_PACIENTE (NM_PACIENTE, NR_IDADE, DS_EMAIL, NR_TELEFONE) VALUES (?, ?, ?, ?)";

        // CORREÇÃO: Removida a chave { extra no final desta linha
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[] { "ID_PACIENTE" })) {

            ps.setString(1, paciente.getNome());
            ps.setInt(2, paciente.getIdade());
            ps.setString(3, paciente.getEmail());
            ps.setString(4, paciente.getTelefone());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar paciente", e);
        }
        return paciente;
    }

    public List<Paciente> listar() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT ID_PACIENTE, NM_PACIENTE, NR_IDADE, DS_EMAIL, NR_TELEFONE FROM T_PACIENTE";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente(
                        rs.getLong("ID_PACIENTE"),
                        rs.getString("NM_PACIENTE"),
                        rs.getInt("NR_IDADE"),
                        rs.getString("DS_EMAIL"),
                        rs.getString("NR_TELEFONE")
                );
                pacientes.add(paciente);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes", e);
        }
        return pacientes;
    }

    public Paciente pesquisarPorId(long id) {
        String sql = "SELECT ID_PACIENTE, NM_PACIENTE, NR_IDADE, DS_EMAIL, NR_TELEFONE FROM T_PACIENTE WHERE ID_PACIENTE = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getLong("ID_PACIENTE"),
                            rs.getString("NM_PACIENTE"),
                            rs.getInt("NR_IDADE"),
                            rs.getString("DS_EMAIL"),
                            rs.getString("NR_TELEFONE")
                    );
                } else {
                    throw new EntidadeNaoEncontradaException("Paciente não encontrado com ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar paciente", e);
        }
    }

    public Paciente atualizar(Paciente paciente) {
        String sql = "UPDATE T_PACIENTE SET NM_PACIENTE = ?, NR_IDADE = ?, DS_EMAIL = ?, NR_TELEFONE = ? WHERE ID_PACIENTE = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNome());
            ps.setInt(2, paciente.getIdade());
            ps.setString(3, paciente.getEmail());
            ps.setString(4, paciente.getTelefone());
            ps.setLong(5, paciente.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new EntidadeNaoEncontradaException("Paciente não encontrado para atualização com ID: " + paciente.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar paciente", e);
        }
        return paciente;
    }

    public void remover(long id) {
        String sql = "DELETE FROM T_PACIENTE WHERE ID_PACIENTE = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new EntidadeNaoEncontradaException("Paciente não encontrado para remoção com ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover paciente", e);
        }
    }
}