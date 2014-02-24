package banco;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main2 {

	private static Connection getConnection(String ip, String banco,
			String usuario, String senha) throws SQLException {
		return DriverManager.getConnection("jdbc:postgresql://" + ip + ":5432/"
				+ banco, usuario, senha);
	}

	@SuppressWarnings("unchecked")
	public static JSONArray fazerConsulta(String ip, String banco,
			String usuario, String senha, String tabela) {
		JSONArray array = new JSONArray();
		try {
			Connection con;
			con = getConnection(ip, banco, usuario, senha);
			con.setAutoCommit(false);

			PreparedStatement st = con
					.prepareStatement("SELECT nompro, estatu, precus, preven FROM "
							+ tabela);
			st.execute();
			ResultSet rs = st.getResultSet();

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("produto", rs.getString("nompro"));
				obj.put("custo", rs.getString("precus"));
				obj.put("estoque", rs.getString("estatu"));
				obj.put("venda", rs.getString("preven"));
				array.add(obj);

			}
			st.close();
		} catch (SQLException e) {
			String erro = "";
			if (e.getMessage().contains("database")) {
				erro = "ERRO FATAL: banco " + banco + " não existe!";
			}
			else if (e.getMessage().contains("role")) {
				erro = "ERRO FATAL: usuario " + usuario + " não existe!";
			}
			else if (e.getMessage().contains("relation")) {
				erro = "ERRO FATAL: tabela " + tabela + " não existe!";
			}
			else if (e.getMessage().contains("password")) {
				erro = "ERRO FATAL: senha incorreta!";
			}
			 System.out.println(e.getMessage());

			JOptionPane.showMessageDialog(null, "Problema na conexão\n" + erro);
			// JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
		}
		return array;
	}

	public static void criarJson(JSONArray array) throws IOException {
		FileWriter writer = new FileWriter("produtos.json");
		writer.append(array.toJSONString());
		writer.close();
	}

	public static void main(String[] args) {
		String ip;
		do {
			ip = JOptionPane.showInputDialog(null,
					"Informe o IP do banco de dados:");
		} while (ip.equals(""));
		String banco;

		do {
			banco = JOptionPane.showInputDialog(null,
					"Informe o nome do banco de dados:");
		} while (banco.equals(""));

		String tabela;
		do {
			tabela = JOptionPane.showInputDialog(null, "Informe a tabela:");
		} while (tabela.equals(""));

		String usuario;
		do {
			usuario = JOptionPane.showInputDialog(null, "Informe o usuario:");
		} while (usuario.equals(""));

		String senha = JOptionPane.showInputDialog(null, "Informe a senha:");
		JSONArray array = fazerConsulta(ip, banco, usuario, senha, tabela);
		try {
			criarJson(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Arquivo Gerado com sucesso.");
	}
}
