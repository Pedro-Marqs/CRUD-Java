package br.com.aplcurso.dao;

import br.com.aplcurso.model.Estado;
import br.com.aplcurso.utils.SingleConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class EstadoDAO implements GenericDAO {

    private Connection conexao;

    public EstadoDAO() throws Exception {
        conexao = SingleConnection.getConnection();
    }

    @Override
    public Boolean cadastrar(Object objeto) {
        Estado oEstado = (Estado) objeto;
        Boolean retorno = false;
        if (oEstado.getId() == 0) {
            retorno = this.inserir(oEstado);
        } else {
            retorno = this.alterar(oEstado);
        }
        return retorno;
    }

    @Override
    public Boolean inserir(Object objeto) {
        Estado oEstado = (Estado) objeto;
        PreparedStatement stmt = null;
        String sql = "insert into estado (nomeEstado, siglaEstado)" + "values (?,?)";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oEstado.getNomeEstado());
            stmt.setString(2, oEstado.getSiglaEstado());
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception ex) {
            try {
                System.out.println("Problemas ao cadastrar o Estado! Erro: " + ex.getMessage());
                ex.printStackTrace();
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Boolean alterar(Object objeto) {
        Estado oEstado = (Estado) objeto;
        PreparedStatement stmt = null;
        String sql = "update estado set nomeEstado=?, siglaEstado=? "
                + "where id=?";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oEstado.getNomeEstado());
            stmt.setString(2, oEstado.getSiglaEstado());
            stmt.setInt(3, oEstado.getId());
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception ex) {
            try {
                System.out.println("Problemas ao alterar o Estado! Erro: " + ex.getMessage());
                ex.printStackTrace();
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro:" + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }
    
    //AAAAAAAAAAAAAAAAAAAAAAAAAAA
//    @Override
//    public Boolean excluir(int numero) {
//        PreparedStatement stmt = null;
//
//        String sqlu = "SELECT COUNT(*) as quantidade_usuario FROM estado WHERE usario.idestado=estado.id";
//        try (PreparedStatement stmtu = conexao.prepareStatement(sqlu)) {
//            stmt.setInt(1, numero);
//            ResultSet rs = stmtu.executeQuery();
//            while (rs.next()) {
//                if (rs.getInt("quantidade_usuario") < 1) {
//                    String sql = "delete from estado where id=?";
//                    try {
//                        stmt = conexao.prepareStatement(sql);
//                        stmt.setInt(1, numero);
//                        stmt.execute();
//                        conexao.commit();
//                        return true;
//                    } catch (Exception ex) {
//                        try {
//                            System.out.println("Problemas ao excluir o Estado! Erro: " + ex.getMessage());
//                            ex.printStackTrace();
//                            conexao.rollback();
//                        } catch (SQLException e) {
//                            System.out.println("Erro:" + e.getMessage());
//                            e.printStackTrace();
//                        }
//                        return false;
//                    }
//                } else{
//                    response.getWriter().write("3");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

    @Override
    public Boolean excluir(int numero) {
        PreparedStatement stmt = null;
        String sql = "delete from estado where id=?";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, numero);
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception ex) {
            try {
                System.out.println("Problemas ao excluir o Estado! Erro: " + ex.getMessage());
                ex.printStackTrace();
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro:" + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Object carregar(int numero) {
        String sql = "Select * from estado where id=?";
        Estado oEstado = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                oEstado = new Estado();
                oEstado.setId(rs.getInt("id"));
                oEstado.setNomeEstado(rs.getString("nomeEstado"));
                oEstado.setSiglaEstado(rs.getString("siglaEstado"));
            }

            return oEstado;

        } catch (SQLException ex) {
            System.out.println("Erro ao listar estados: " + ex.getMessage());
            ex.printStackTrace();
        }

        return oEstado;
    }

    @Override
    public List<Object> listar() {
        List<Object> resultado = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "Select * from estado order by id";
        try {
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Estado oEstado = new Estado();
                oEstado.setId(rs.getInt("id"));
                oEstado.setNomeEstado(rs.getString("nomeEstado"));
                oEstado.setSiglaEstado(rs.getString("siglaEstado"));
                resultado.add(oEstado);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao listar estados: " + ex.getMessage());
            ex.printStackTrace();
        }
        return resultado;
    }

    public boolean siglaExiste(String siglaEstado) {
        String sql = "SELECT COUNT(*) as quantidade_sigla FROM estado WHERE siglaEstado = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, siglaEstado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("quantidade_sigla") > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean usuarioExiste(Integer id) {
        String sql = "SELECT COUNT(*) as quantidade_usuario FROM estado, usuario WHERE usuario.idestado=estado.id AND estado.id=?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("quantidade_usuario") > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
