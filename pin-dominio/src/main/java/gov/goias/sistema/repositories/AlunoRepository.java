package gov.goias.sistema.repositories;

import gov.goias.sistema.entidades.Aluno;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface AlunoRepository extends CrudRepository<Aluno, Integer> {
    Optional<Aluno> findById(Integer id);
}
