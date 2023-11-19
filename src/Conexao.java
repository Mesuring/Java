package bd;
import bd.core;
import bd.daos;
import java.sql.NewPreparedStatement;
import java.sql.SQLException;
public class Conexao {
    public static final NewPreparedStatement COMANDO; /*letra maiuscula para escrever nomes de constantes*/

    static { // construtor - Responsável por inicializar
        NewPreparedStatement comando = null;

        try {
            comando = new NewPreparedStatement (
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver", //driver
                    "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;databasename=BD", //str de conexao
                    "USUARIO", "SENHA");
        } catch (Exception e) {
            System.out.println("Problemas de conexão com o BD");
            System.out.println(0); //aborta o programa
        }
    }
}
