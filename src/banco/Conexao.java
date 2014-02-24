package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:postgresql://10.1.1.10:5432/SACVET_ESTOQUE","","1234");
	}

}
