package banco;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSONArray array = new JSONArray();
		ArrayList<String> produtos = new ArrayList();
		Conexao conexao = new Conexao();
		Connection con;
		try {
			con = conexao.getConnection();
			if (con != null) {
				System.out
						.println("You made it, take control your database now!");
			} else {
				System.out.println("Failed to make connection!");
			}
			con.setAutoCommit(false);

			PreparedStatement st = con
					.prepareStatement("SELECT nompro, estatu, precus, preven FROM arqest");
			st.execute();
			ResultSet rs = st.getResultSet();
			FileWriter writer = new FileWriter("json.json");

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("produto", rs.getString("nompro"));
				obj.put("custo", rs.getString("precus"));
				obj.put("estoque", rs.getString("estatu"));
				obj.put("venda", rs.getString("preven"));
				array.add(obj);

			}
			writer.append(array.toJSONString());
			st.close();
			writer.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (IOException e) {

		}

		System.out.println("Conex�o Beleza");

		for (String string : produtos) {

		}
	}
}
