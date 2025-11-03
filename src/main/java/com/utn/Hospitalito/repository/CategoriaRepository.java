package com.utn.Hospitalito.repository;

import com.utn.Hospitalito.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Categoria.
 * Proporciona operaciones CRUD sobre las categorías de camas.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}//interface CategoriaRepository
