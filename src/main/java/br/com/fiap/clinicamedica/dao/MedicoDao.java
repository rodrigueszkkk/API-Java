package br.com.fiap.clinicamedica.dao;

import br.com.fiap.clinicamedica.exception.EntidadeNaoEncontradaException;
import br.com.fiap.clinicamedica.model.Medico;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MedicoDao {

    @Inject
    DataSource dataSource;

    public Medico cadastrar(Medico medico) {
        String sql = "INSERT INTO T_MEDICO (NM_MEDICO, DS_ESPECIALIDADE, NR_CRM) VALUES (?, ?, ?)";

        // CORREÇÃO APLICADA AQUI:
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[] { "ID_MEDICO" })) {

            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getEspecialidade());
            ps.setString(3, medico.getCrm());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    medico.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar médico", e);
        }
        return medico;
    }



    public List<Medico> listar() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT ID_MEDICO, NM_MEDICO, DS_ESPECIALIDADE, NR_CRM FROM T_MEDICO";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medico medico = new Medico(
                        rs.getLong("ID_MEDICO"),
                        rs.getString("NM_MEDICO"),
                        rs.getString("DS_ESPECIALIDADE"),
                        rs.getString("NR_CRM")
                );
                medicos.add(medico);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos", e);
        }
        return medicos;
    }

    public Medico pesquisarPorId(long id) {
        String sql = "SELECT ID_MEDICO, NM_MEDICO, DS_ESPECIALIDADE, NR_CRM FROM T_MEDICO WHERE ID_MEDICO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medico(
                            rs.getLong("ID_MEDICO"),
                            rs.getString("NM_MEDICO"),
                            rs.getString("DS_ESPECIALIDADE"),
                            rs.getString("NR_CRM")
                    );
                } else {
                    throw new EntidadeNaoEncontradaException("Médico não encontrado com ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar médico", e);
        }
    }

    public Medico atualizar(Medico medico) {
        String sql = "UPDATE T_MEDICO SET NM_MEDICO = ?, DS_ESPECIALIDADE = ?, NR_CRM = ? WHERE ID_MEDICO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getEspecialidade());
            ps.setString(3, medico.getCrm());
            ps.setLong(4, medico.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new EntidadeNaoEncontradaException("Médico não encontrado para atualização com ID: " + medico.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar médico", e);
        }
        return medico;
    }

    public void remover(long id) {
        String sql = "DELETE FROM T_MEDICO WHERE ID_MEDICO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new EntidadeNaoEncontradaException("Médico não encontrado para remoção com ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover médico", e);
        }
    }
}