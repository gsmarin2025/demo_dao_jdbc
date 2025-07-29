package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		PreparedStatement st = null;
		try {
		 st = conn.prepareStatement(
		
		"INSERT INTO seller "
		+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "  
		+"VALUES "  
		+"(?, ?, ?, ?, ?)",
		Statement.RETURN_GENERATED_KEYS); //retorna id do novo vendedor inserido
		
		 st.setString(1, obj.getName());
		 st.setString(2, obj.getEmail());
		 st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
		 st.setDouble(4, obj.getBaseSalary());
		 st.setInt(5, obj.getDepartment().getId());
		 
		 int rowsAffected = st.executeUpdate();
		 
		 if(rowsAffected >0) {
			 ResultSet rs = st.getGeneratedKeys();
			 if(rs.next()) {
				 int id = rs.getInt(1);//pega id gerado
				 obj.setId(id);
			 }
			 DB.closeResultSet(rs);
		 }
		 else {
			 throw new DbException("Unexpected error! No rows affected!");
			 
		 }
	}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
		 st = conn.prepareStatement(
		
		"UPDATE seller " 
		+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
		+ "WHERE Id = ? ");
		 
		 st.setString(1, obj.getName());
		 st.setString(2, obj.getEmail());
		 st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
		 st.setDouble(4, obj.getBaseSalary());
		 st.setInt(5, obj.getDepartment().getId());
		 st.setInt(6, obj.getId());
		 
		 st.executeUpdate();
	}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
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
				Department dep = instantiateDepartment(rs);
				Seller obj  = instantiateSeller(rs,dep);
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
			obj.setId(rs.getInt("Id"));
			obj.setName(rs.getString("Name"));
			obj.setEmail(rs.getString("Email"));
			obj.setBaseSalary(rs.getDouble("BaseSalary"));
			obj.setBirthDate(rs.getDate("BirthDate"));
			obj.setDepartment(dep);
			return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId")); //Pegar Id do Departamento
		dep.setName(rs.getString("DepName"));//Pega nome do Departamento
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null; //usado para Ler os dados retornados pelo banco depois de um SELECT.


		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "  
					+ "ON seller.DepartmentId = department.Id " 
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>(); //criacao da lista
			Map<Integer, Department> map = new HashMap<>();//MAP
			
			while(rs.next()) { //Percorre o result set enquanto tiver um proximo
				
				Department dep = map.get(rs.getInt("DepartmentId")); 
				
				if(dep == null) {
					dep = instantiateDepartment(rs); //se ele for nulo eu vou instanciar ele
					map.put(rs.getInt("DepartmentId"), dep);//guarda no Map
				}
				
				Seller obj  = instantiateSeller(rs,dep);
				list.add(obj); //adiciona na lista
			}
			return list;
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
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement st = null;
		ResultSet rs = null; //usado para Ler os dados retornados pelo banco depois de um SELECT.


		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "  
					+ "ON seller.DepartmentId = department.Id " 
					+ "WHERE DepartmentId = ? " 
					+ "ORDER BY Name");
					
			
			st.setInt(1, department.getId());
			rs = st.executeQuery(); //o resultado cai dentro do result set, o result set traz os dados em formato de tabela
			
			List<Seller> list = new ArrayList<>(); //criacao da lista
			Map<Integer, Department> map = new HashMap<>();//MAP
			
			while(rs.next()) { //Percorre o result set enquanto tiver um proximo
				
				Department dep = map.get(rs.getInt("DepartmentId")); 
				
				if(dep == null) {
					dep = instantiateDepartment(rs); //se ele for nulo eu vou instanciar ele
					map.put(rs.getInt("DepartmentId"), dep);//guarda no Map
				}
				
				Seller obj  = instantiateSeller(rs,dep);
				list.add(obj); //adiciona na lista
			}
			return list;
		}
		catch (SQLException e) {
			throw new  DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
