package gov.goias.sistema.negocio;

import gov.goias.sistema.entidades.Aluno;
import gov.goias.sistema.repositories.AlunoRepository;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AlunoService {
    @Inject
    private AlunoRepository alunoRepository;

    public Aluno consultar(Integer id)
    {
        return alunoRepository.findOne(id);
    }

    public Aluno alterar(Aluno aluno)
    {
        return alunoRepository.save(aluno);
    }

    public void excluir(Aluno aluno)
    {
        alunoRepository.delete(aluno);
    }

}
