package br.com.fiap.clinicamedica.dao;

import br.com.fiap.clinicamedica.exception.EntidadeNaoEncontradaException;
import br.com.fiap.clinicamedica.model.Consulta;
import br.com.fiap.clinicamedica.model.Medico;
import br.com.fiap.clinicamedica.model.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ConsultaDao {

    @Inject
    DataSource dataSource;

    @Inject
    PacienteDao pacienteDao;
    @Inject
    MedicoDao medicoDao;

    public Consulta cadastrar(Consulta consulta) {
        Paciente paciente = pacienteDao.pesquisarPorId(consulta.getPaciente().getId());
        Medico medico = medicoDao.pesquisarPorId(consulta.getMedico().getId());

        String sql = "INSERT INTO T_CONSULTA (DT_CONSULTA, HR_CONSULTA, DS_MOTIVO, ID_PACIENTE, ID_MEDICO) VALUES (?, ?, ?, ?, ?)";

        // CORREÇÃO APLICADA AQUI:
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[] { "ID_CONSULTA" })) {

            ps.setDate(1, Date.valueOf(consulta.getDataConsulta()));
            ps.setString(2, consulta.getHoraConsulta());
            ps.setString(3, consulta.getMotivo());
            ps.setLong(4, paciente.getId());
            ps.setLong(5, medico.getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getLong(1));
                }
            }

            consulta.setPaciente(paciente);
            consulta.setMedico(medico);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar consulta", e);
        }
        return consulta;
    }


    public List<Consulta> listar() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT " +
                "c.ID_CONSULTA, c.DT_CONSULTA, c.HR_CONSULTA, c.DS_MOTIVO, " +
                "p.ID_PACIENTE, p.NM_PACIENTE, p.NR_IDADE, p.DS_EMAIL, p.NR_TELEFONE, " +
                "m.ID_MEDICO, m.NM_MEDICO, m.DS_ESPECIALIDADE, m.NR_CRM " +
                "FROM T_CONSULTA c " +
                "JOIN T_PACIENTE p ON c.ID_PACIENTE = p.ID_PACIENTE " +
                "JOIN T_MEDICO m ON c.ID_MEDICO = m.ID_MEDICO";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                consultas.add(mapResultSetToConsulta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas", e);
        }
        return consultas;
    }

    public Consulta pesquisarPorId(long id) {
        String sql = "SELECT " +
                "c.ID_CONSULTA, c.DT_CONSULTA, c.HR_CONSULTA, c.DS_MOTIVO, " +
                "p.ID_PACIENTE, p.NM_PACIENTE, p.NR_IDADE, p.DS_EMAIL, p.NR_TELEFONE, " +
                "m.ID_MEDICO, m.NM_MEDICO, m.DS_ESPECIALIDADE, m.NR_CRM " +
                "FROM T_CONSULTA c " +
                "JOIN T_PACIENTE p ON c.ID_PACIENTE = p.ID_PACIENTE " +
                "JOIN T_MEDICO m ON c.ID_MEDICO = m.ID_MEDICO " +
                "WHERE c.ID_CONSULTA = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToConsulta(rs);
                } else {
                    throw new EntidadeNaoEncontradaException("Consulta não encontrada com ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar consulta", e);
        }
    }

    public Consulta atualizar(Consulta consulta) {
        Paciente paciente = pacienteDao.pesquisarPorId(consulta.getPaciente().getId());
        Medico medico = medicoDao.pesquisarPorId(consulta.getMedico().getId());

        String sql = "UPDATE T_CONSULTA SET DT_CONSULTA = ?, HR_CONSULTA = ?, DS_MOTIVO = ?, ID_PACIENTE = ?, ID_MEDICO = ? WHERE ID_CONSULTA = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(consulta.getDataConsulta()));
            ps.setString(2, consulta.getHoraConsulta());
            ps.setString(3, consulta.getMotivo());
            ps.setLong(4, paciente.getId());
            ps.setLong(5, medico.getId());
            ps.setLong(6, consulta.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new EntidadeNaoEncontradaException("Consulta não encontrada para atualização com ID: " + consulta.getId());
            }

            consulta.setPaciente(paciente);
            consulta.setMedico(medico);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar consulta", e);
        }
        return consulta;
    }

    public void remover(long id) {
        String sql = "DELETE FROM T_CONSULTA WHERE ID_CONSULTA = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new EntidadeNaoEncontradaException("Consulta não encontrada para remoção com ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover consulta", e);
        }
    }

    private Consulta mapResultSetToConsulta(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente(
                rs.getLong("ID_PACIENTE"),
                rs.getString("NM_PACIENTE"),
                rs.getInt("NR_IDADE"),
                rs.getString("DS_EMAIL"),
                rs.getString("NR_TELEFONE")
        );

        Medico medico = new Medico(
                rs.getLong("ID_MEDICO"),
                rs.getString("NM_MEDICO"),
                rs.getString("DS_ESPECIALIDADE"),
                rs.getString("NR_CRM")
        );

        return new Consulta(
                rs.getLong("ID_CONSULTA"),
                rs.getDate("DT_CONSULTA").toLocalDate(),
                rs.getString("HR_CONSULTA"),
                rs.getString("DS_MOTIVO"),
                paciente,
                medico
        );
    }
}