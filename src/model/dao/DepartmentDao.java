package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	// esse void Ou seja, ele faz alguma ação, mas não devolve nada para quem chamou.


	void insert(Department obj); //responsavel por inserir no banco de dados esse objeto que inseri
	void update(Department obj); //responsavel por atualizar no banco de dados esse objeto que inseri
	void deleteById(Department obj); //responsavel por deletarPorId no banco de dados esse objeto que inseri
	Department findById(Integer id); //Pega o Id e consultar no banco de dados um objeto com esse Id
	List<Department> findAll(); //retorna todos os departamentos
}
