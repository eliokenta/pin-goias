package gov.goias.sistema.api.routes.v1;

import gov.goias.sistema.api.mappers.AlunoModelMapper;
import gov.goias.sistema.api.rep.v1.Aluno;
import gov.goias.sistema.negocio.AlunoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/v1/aluno")
@Api(description = "Operações na coleção de alunos na versão 1")
public class AlunoV1 {
    private static final Logger log = Logger.getLogger(AlunoV1.class);

    final int tamanhoPagina = 10;

    @Context
    private UriInfo uriInfo;

    @Autowired
    AlunoService alunos;

    @Context
    private HttpServletRequest request;

    private DozerBeanMapper mapper = AlunoModelMapper.getV1Mapper();

    /**
     * Consulta gov.goias.sistema.rep.v1.Aluno
     * @param id id de gov.goias.sistema.rep.v1.Aluno
     * @return HTTP 200 - OK<br>
     * 		   Retorna gov.goias.sistema.rep.v1.Aluno
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Obtem o aluno.", notes = "Obtem o aluno a partir do ID.", extensions = {
            @Extension(name = "x-mask", properties = {
                @ExtensionProperty(name = "nascimento", value = "dd/MM/yyyy")
            })
    })
    public Aluno consultar20(@PathParam("id") Integer id) {
        gov.goias.sistema.entidades.Aluno aluno = alunos.consultar(id);
        Aluno alunoVo = new Aluno();
        mapper.map(aluno, alunoVo);
        return	alunoVo;
    }

    /**
     * Atualiza um gov.goias.sistema.rep.v1.Aluno
     * @param id id de gov.goias.sistema.rep.v1.Aluno
     */
    @PUT
    @Path("/alterar/{id}")
    @Consumes({Aluno.json, Aluno.xml, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(value = "Atualiza o registro do aluno.", notes = "Atualiza o registro do aluno a partir do ID.")
    public void alterar20(Aluno recurso, @PathParam("id") Integer id) {
        gov.goias.sistema.entidades.Aluno a = mapper.map(recurso, gov.goias.sistema.entidades.Aluno.class);
        alunos.consultar(id);
        alunos.alterar(a);
    }

    /**
     * Remove um gov.goias.sistema.rep.v1.Aluno
     * @param id id de gov.goias.sistema.rep.v1.Aluno
     */
    @DELETE
    @Path("/excluir/{id}")
    @Produces({Aluno.json, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Exclui o aluno.", notes = "Exclui o aluno a partir do ID.")
    public void excluir20(@PathParam("id") Integer id)  {
        alunos.excluir(alunos.consultar(id));
    }

}