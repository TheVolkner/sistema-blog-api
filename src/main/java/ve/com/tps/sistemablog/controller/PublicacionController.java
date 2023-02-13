package ve.com.tps.sistemablog.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ve.com.tps.sistemablog.dto.PagePublicacion;
import ve.com.tps.sistemablog.dto.PublicacionDTO;
import ve.com.tps.sistemablog.services.PublicacionServicio;
import ve.com.tps.sistemablog.util.AppConstantes;

import java.util.List;

@RestController
@RequestMapping("api/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionServicio publicacionServicio;

    @GetMapping
    public ResponseEntity<PagePublicacion> listarPublicaciones(
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

        return new ResponseEntity<>(publicacionServicio.listarPublicaciones(numeroPagina,medidaPagina,ordenarPor,sortDir),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDTO> buscarPublicacionPorId(@PathVariable Integer id){

        PublicacionDTO pDTOlisto = publicacionServicio.buscarPublicacionPorId(id);

        if(pDTOlisto != null){

            return new ResponseEntity<>(pDTOlisto,HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PublicacionDTO> crearPublicacion(@Valid @RequestBody PublicacionDTO publicacion){

        return new ResponseEntity<>(publicacionServicio.crearPublicacion(publicacion), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDTO> modificarPublicacion(@PathVariable Integer id,@Valid @RequestBody PublicacionDTO publicacionDTO){

        publicacionDTO.setId(id);
        PublicacionDTO pDTOmodificado = publicacionServicio.modificarPublicacion(publicacionDTO);

        if(pDTOmodificado != null){

            return new ResponseEntity<>(pDTOmodificado,HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<PublicacionDTO> eliminarPublicacion(@PathVariable Integer id){

        PublicacionDTO pDTOlisto = publicacionServicio.buscarPublicacionPorId(id);

        if(pDTOlisto != null){

            publicacionServicio.eliminarPublicacion(pDTOlisto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.notFound().build();
    }


}
