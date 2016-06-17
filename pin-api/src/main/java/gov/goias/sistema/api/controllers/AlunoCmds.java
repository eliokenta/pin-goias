package gov.goias.sistema.api.controllers;

import gov.goias.sistema.api.view.model.Aluno;
import gov.goias.sistema.api.mappers.AlunoModelMapper;
import gov.goias.sistema.exception.NaoEncontradoException;
import gov.goias.sistema.negocio.AlunoService;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/aluno")
@Api(description = "Operações CRUD de Aluno")
public class AlunoCmds {
    private static final Logger log = Logger.getLogger(AlunoCmds.class);

    final int tamanhoPagina = 10;

    @Context
    private UriInfo uriInfo;

    @Autowired
    private AlunoService alunoService;

    @Context
    private HttpServletRequest request;

    private DozerBeanMapper mapper = AlunoModelMapper.getMapper();

    /**
     * Armazena um Aluno no sistema.
     *
     * @param aluno Informações do aluno
     */
    @PUT
    @Path("/salvar")
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Armazena o registro do aluno.", notes = "Armazena o registro do aluno na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Erro interno.")
    })
    public Response salvar(Aluno aluno) {
        gov.goias.sistema.entidades.Aluno a = mapper.map(aluno, gov.goias.sistema.entidades.Aluno.class);
        alunoService.salvar(a);

        return Response.ok().build();
    }

    /**
     * Atualiza um Aluno no sistema.
     *
     * @param id Identificador do aluno
     */
    @PUT
    @Path("/alterar/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Atualiza o registro do aluno.", notes = "Atualiza o registro do aluno a partir do ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Erro interno.")
    })
    public Response alterar(Aluno aluno, @PathParam("id") Integer id) {
        gov.goias.sistema.entidades.Aluno a = mapper.map(aluno, gov.goias.sistema.entidades.Aluno.class);
        a.setId(id);
        alunoService.alterar(a);

        return Response.ok().build();
    }

    /**
     * Remove um Aluno no sistema
     *
     * @param id Identificador do aluno
     */
    @DELETE
    @Path("/excluir/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Exclui o aluno.", notes = "Exclui o aluno a partir do ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Erro interno.")
    })
    public Response excluir(@PathParam("id") Integer id) {
        alunoService.excluir(id);

        return Response.ok().build();
    }

}