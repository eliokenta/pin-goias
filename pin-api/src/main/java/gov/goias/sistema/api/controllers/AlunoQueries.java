package gov.goias.sistema.api.controllers;

import gov.goias.sat2.representation.DataTableResponse;
import gov.goias.sistema.api.mappers.AlunoModelMapper;
import gov.goias.sistema.api.view.model.Aluno;
import gov.goias.sistema.exception.NaoEncontradoException;
import gov.goias.sistema.negocio.AlunoService;
import io.swagger.annotations.*;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/aluno")
public class AlunoQueries {

    @Context
    protected HttpServletRequest request;

    @Autowired
    AlunoService service;

    @Autowired
    private AlunoService alunoService;

    private DozerBeanMapper mapper = AlunoModelMapper.getMapper();

    /**
     * Consulta a Aluno
     *
     * @param id Identificador do aluno
     * @return HTTP 200 - OK
     * Retorna Aluno
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Obtem o aluno.", notes = "Obtem o aluno a partir do ID.", extensions = {
            @Extension(name = "x-mask", properties = {
                    @ExtensionProperty(name = "nascimento", value = "dd/MM/yyyy")
            })
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK.", response = Aluno.class),
            @ApiResponse(code = 404, message = "Aluno não encontrado."),
            @ApiResponse(code = 500, message = "Erro interno.")
    })
    public Response consultar(@PathParam("id") Integer id) {
        Aluno alunoVo = new Aluno();
        gov.goias.sistema.entidades.Aluno aluno = alunoService.consultar(id).orElseThrow(() -> new NaoEncontradoException("Aluno não encontrado."));

        mapper.map(aluno, alunoVo);
        return Response.ok(alunoVo).build();
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("search[value]") final String searchValue,
                         @QueryParam("limit")   final Integer limit,
                         @QueryParam("offset")  final Integer offset) {
        final List<Aluno> res = new ArrayList<Aluno>();
        final String[] columns = new String[]{"id", "nome","email"};
        try {
            final Integer qtTotal = new Long(service.contarTodos()).intValue();

            final Map<String, String> searchParams = new HashMap<>();

            if (!searchValue.isEmpty()) {
                searchParams.put(columns[1], searchValue);
            }

            final Integer page = new Double(Math.ceil(start / length)).intValue();
            final Page<Aluno> list = service.listarTodos(new PageRequest(page, length)).map(a -> Aluno.from(a)) ; //searchValue.isEmpty() ? service.listarTodos(new PageRequest(page, length)) : service.listarTodos(new PageRequest(page, length)))); //service.queryFirst10ByName(searchValue, new PageRequest(page, length));
            final Integer qtFiltrada = new Long(list.getTotalElements()).intValue();
            if (qtFiltrada > 0) {
                list.forEach(a -> res.add(a.asMapofValues(
                        (Object v) -> String.format("row_%s", v),
                        "DT_RowId",
                        "id",
                        columns
                )));
            }
            dtr.setRecordsFiltered(qtFiltrada);
            dtr.setData(res);
            dtr.setRecordsTotal(qtTotal);
        } catch (Exception e) {
            //log.error(e);
            //dtr.setError(GoiasResourceMessage.getMessage("msg_erro_dessconhecido"));
        }

       return Response.status(Response.Status.OK).entity(dtr).build();
    }

}