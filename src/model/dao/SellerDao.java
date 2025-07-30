package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	// esse void Ou seja, ele faz alguma ação, mas não devolve nada para quem chamou.

	void insert(Seller obj); //responsavel por inserir no banco de dados esse objeto que inseri
	void update(Seller obj); //responsavel por atualizar no banco de dados esse objeto que inseri
	void deleteById(Integer id); //responsavel por deletarPorId no banco de dados esse objeto que inseri
	Seller findById(Integer id); //Pega o Id e consultar no banco de dados um objeto com esse Id
	List<Seller> findAll(); //retorna todos os Seller
	List<Seller> findByDepartment(Department department); //buscar por departamento
}
