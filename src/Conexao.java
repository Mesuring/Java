package bd;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;
public class Conexao {

    static java.sql.Connection contato =  null;

    public static Conexao getConexion() {
        String url = "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;databaseName=pra";

        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Não foi Possível a Conexão... Revisar Driver" + e.getMessage(),
            "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
        try {
            contato = DriverManager.getConnection(url, "BD23334","BD23334");
        } catch (SQLServerException e) {
            JOptionPane.showMessageDialog(null,"Erro"+ e.getMessage(),
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
        return contato;
    }
    public static ResultSet Consulta(String consulta) {
        Conexao con = getConexion();
        Statement declara;
        try {
            declara = con.createStatement();
            ResultSet resposta = declara.executeQuery(consulta);
            return resposta;
        }catch (SQLServerException e) {
            JOptionPane.showMessageDialog(null,"Erro"+ e.getMessage(),
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
