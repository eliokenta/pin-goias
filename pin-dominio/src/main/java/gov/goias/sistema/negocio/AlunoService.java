package gov.goias.sistema.negocio;

import gov.goias.sistema.entidades.Aluno;
import gov.goias.sistema.exception.InfraException;
import gov.goias.sistema.repositories.AlunoRepository;
import javaslang.control.Try;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
public class AlunoService {
    @Inject
    private AlunoRepository alunoRepository;

    public Optional<Aluno> consultar(Integer id) {
        return Try.of(() -> alunoRepository.findById(id)).onFailure(e -> new InfraException(e)).get();
    }

    public Aluno alterar(Aluno aluno) {
        return Try.of(() -> alunoRepository.save(aluno)).onFailure(e -> new InfraException(e)).get();
    }

    public void excluir(Integer id) {
        try {
            alunoRepository.findById(id).ifPresent(a -> alunoRepository.delete(a));
        } catch (Exception e) {
            throw new InfraException(e);
        }
    }

}
