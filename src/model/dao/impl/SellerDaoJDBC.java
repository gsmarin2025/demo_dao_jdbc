package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn; 
	
	public SellerDaoJDBC(Connection conn) {//Vou deixar ter uma dependencia com a Conexao com o construtor
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null; //usado para Ler os dados retornados pelo banco depois de um SELECT.


		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "  
					+ "ON seller.DepartmentId = department.Id "  
					+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery(); //o resultado cai dentro do result set, o result set traz os dados em formato de tabela
			if(rs.next()) { // testar se veio algum resultado com esse next, se nao retornar nada ele pula o if e retorna nullo
				
				// Esse trecho de baixo transforma o resultado dessa linha (que está no ResultSet rs) em um objeto Java:
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId")); //Pegar Id do Departamento
				dep.setName(rs.getString("DepName"));//Pega nome do Departamento
				Seller obj  = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new  DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
