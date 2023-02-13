package ve.com.tps.sistemablog.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ve.com.tps.sistemablog.dto.ComentarioDTO;
import ve.com.tps.sistemablog.dto.PageComentarios;
import ve.com.tps.sistemablog.dto.PublicacionDTO;
import ve.com.tps.sistemablog.exceptions.ResourceNotFoundException;
import ve.com.tps.sistemablog.services.ComentariosServicio;
import ve.com.tps.sistemablog.services.PublicacionServicio;
import ve.com.tps.sistemablog.util.AppConstantes;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/comentarios")
public class ComentariosController {

    @Autowired
    private ComentariosServicio comentariosServicio;

    @Autowired
    private PublicacionServicio publicacionServicio;

    @GetMapping("/publicacion/{id}")
    public ResponseEntity<PageComentarios> listarComentarioPorPublicacion
            (@PathVariable Integer id,

             //MANDAMOS COMO PARAMETROS DE LA URL LOS CAMPOS PARA CONFIGURAR LA PAGINACION Y ORDENAMIENTO (PAGINATION AND SORTING)


             //ENVIANDO EL NUMERO DE PAGINA POR DEFECTO AL INICIAR

             @RequestParam(value = "pageNumber",defaultValue = AppConstantes.PAGE_NUMBER,required = false) Integer numeroPagina,

             //LA CANTIDAD DE ELEMENTOS POR PAGINA A DESPLEGAR

             @RequestParam(value = "pageSize", defaultValue = AppConstantes.PAGE_SIZE,required = false) Integer medidaPagina,

             //EL PARAMETRO POR EL CUAL SE ORDENARAN LOS CAMPOS, EN ESE CASO POR ID.


             @RequestParam(value = "sortBy",defaultValue = AppConstantes.SORT_BY,required = false) String ordenarPor,

             //DIRECCION DE ORDEN (ASC ES ASCENDENTE Y DESC ES DESCENDENTE)

             @RequestParam(value = "sortDir",defaultValue = AppConstantes.SORT_DIRECTION,required = false) String sortDir
    ){

        PublicacionDTO pDTO = publicacionServicio.buscarPublicacionPorId(id);

        if(pDTO == null){

            throw new ResourceNotFoundException("PublicacionDTO","listarComentarioPorPublicacion()",id.toString());
        }

        return new ResponseEntity<>(comentariosServicio.listarComentariosDePublicacion(id,numeroPagina,medidaPagina,ordenarPor,sortDir), HttpStatus.OK);
    }

    @GetMapping("/{id}/publicacion/{idPost}")
    public ResponseEntity<ComentarioDTO> buscarComentarioPorIdYPublicacionId(@PathVariable Integer id, @PathVariable Integer idPost){

        ComentarioDTO cDTO = comentariosServicio.buscarComentarioPorIdYPublicacionId(id,idPost);

        if(cDTO == null){

            throw new ResourceNotFoundException("ComentarioDTO","buscarComentarioPorIdyPublicacionId()",id.toString());
        }

        return new ResponseEntity<>(cDTO,HttpStatus.OK);
    }

    @PostMapping("/publicacion/{id}")
    public ResponseEntity<ComentarioDTO> agregarComentario(@PathVariable Integer id, @Valid @RequestBody ComentarioDTO comentarioDTO){

        PublicacionDTO pDTO = publicacionServicio.buscarPublicacionPorId(id);

        if(pDTO == null){

            throw new ResourceNotFoundException("PublicacionDTO","agregarComentario()",id.toString());
        }

        comentarioDTO.setPublicacion(pDTO);
        ComentarioDTO cAdded = comentariosServicio.agregarComentario(comentarioDTO,pDTO);

        return new ResponseEntity<>(cAdded,HttpStatus.OK);
    }

    @PutMapping("/{id}/publicacion/{idPost}")
    public ResponseEntity<ComentarioDTO> modificarComentario(@PathVariable Integer id,@PathVariable Integer idPost, @Valid @RequestBody ComentarioDTO comentarioDTO){

        PublicacionDTO pDTO = publicacionServicio.buscarPublicacionPorId(idPost);

        if(pDTO == null){

            throw new ResourceNotFoundException("PublicacionDTO","modificarComentario()",id.toString());
        }

        ComentarioDTO cDTO = comentariosServicio.buscarComentarioPorIdYPublicacionId(id,idPost);

        if(cDTO == null){

            throw new ResourceNotFoundException("ComentarioDTO","modificarComentario()",id.toString());
        }

        comentarioDTO.setId(cDTO.getId());
        comentarioDTO.setPublicacion(pDTO);

        ComentarioDTO cSaved = comentariosServicio.modificarComentario(comentarioDTO,pDTO);

        return new ResponseEntity<>(cSaved,HttpStatus.OK);
    }

    @DeleteMapping("/{id}/publicacion/{idPost}")
    public ResponseEntity<?> eliminarComentario(@PathVariable Integer id,@PathVariable Integer idPost){

        PublicacionDTO pDTO = publicacionServicio.buscarPublicacionPorId(idPost);

        if(pDTO == null){

            throw new ResourceNotFoundException("PublicacionDTO","modificarComentario()",id.toString());
        }

        ComentarioDTO cDTO = comentariosServicio.buscarComentarioPorIdYPublicacionId(id,idPost);

        if(cDTO == null){

            throw new ResourceNotFoundException("ComentarioDTO","modificarComentario()",id.toString());
        }

        comentariosServicio.eliminarComentarioPorId(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
