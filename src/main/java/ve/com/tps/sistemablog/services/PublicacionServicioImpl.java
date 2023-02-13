package ve.com.tps.sistemablog.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ve.com.tps.sistemablog.dto.PagePublicacion;
import ve.com.tps.sistemablog.dto.PublicacionDTO;
import ve.com.tps.sistemablog.entities.Publicacion;
import ve.com.tps.sistemablog.exceptions.ResourceNotFoundException;
import ve.com.tps.sistemablog.repositories.ComentariosRepository;
import ve.com.tps.sistemablog.repositories.PublicacionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionServicioImpl implements PublicacionServicio{

    //MODEL MAPPER PERMITE HACER LAS TRANSFORMACIONES ENTIDAD/DTO Y VICEVERSA RAPIDAMENTE.
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Override
    //CREAMOS UN REGISTRO NUEVO EN LA TABLA PUBLICACION.
    public PublicacionDTO crearPublicacion(PublicacionDTO publicacion) {

        //Transformamos de Objeto DTO a Entidad
        Publicacion post = new Publicacion();
        post.setTitulo(publicacion.getTitulo());
        post.setContenido(publicacion.getContenido());
        post.setDescripcion(publicacion.getDescripcion());

        Publicacion pSaved = publicacionRepository.save(post);

        //Le asignamos el ID del objeto de entidad al DTO, y lo devolvemos.
        publicacion.setId(pSaved.getId());

        return publicacion;
    }

    @Override
    //LISTAMOS TODOS LAS PUBLICACIONES DESDE LA TABLA EN LA BBDD.
    public PagePublicacion listarPublicaciones(Integer numeroPagina, Integer medidaPagina,String ordenarPor,String ordenDir) {

        //CREAMOS UN ORDENAMIENTO PARA INDICARLE A LA PAGINACION
        Sort sort = ordenDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending();

        //CREAMOS UN OBJETO PAGE PARA PODER INDICAR LA PAGINA DE INICIO, LA CANTIDAD DE ELEMENTOS POR PAGINA Y EL ORDENAMIENTO
        Pageable pageable = PageRequest.of(numeroPagina,medidaPagina,sort);

        //OBTENEMOS LOS CAMPOS DE LA BBDD ORDENADOS SEGUN LA PAGINACION
        Page<Publicacion> pagePublicacion = publicacionRepository.findAll(pageable);

        //OBTENEMOS LA LISTA DE TODAS LAS PUBLICACIONES.
        List<Publicacion> listaEntidades = pagePublicacion.getContent();

        //TRANSFORMAMOS DE ENTIDAD A DTO
        List<PublicacionDTO> listaDTO = new ArrayList<>();

        for(Publicacion p : listaEntidades){
            //CADA OBJETO DE ENTIDAD SE TRANSFORMA A DTO.
            //EL METODO MAP DE MODEL MAPPER RECIBE DOS PARAMETROS -> UNO EL OBJETO A TRANSFORMAR
            //Y EL OTRO EL TIPO DE LA CLASE A LA CUAL SE VA A CONVERTIR.
            PublicacionDTO pDTO = modelMapper.map(p,PublicacionDTO.class);
            listaDTO.add(pDTO);
        }
        //CREAMOS UN OBJETO TIPO PAGEPUBLICACION, Y LO RELLENAMOS CON LOS CAMPOS DE LA BBDD
        //Y PARAMETROS DE PAGINACION
        PagePublicacion pagePub = new PagePublicacion();
        pagePub.setContenido(listaDTO);
        pagePub.setMedidaPagina(pagePublicacion.getSize());
        pagePub.setNroPagina(pagePublicacion.getNumber());
        pagePub.setTotalElementos(pagePublicacion.getTotalElements());
        pagePub.setTotalPaginas(pagePublicacion.getTotalPages());
        pagePub.setUltima(pagePublicacion.isLast());

        return pagePub;
    }



    @Override
    //BUSCAMOS UN CAMPO DE LA TABLA PUBLICACION SEGUN COLUMNA ID
    public PublicacionDTO buscarPublicacionPorId(Integer id) {

        //OBTENEMOS EL RESULTADO OPCIONAL DE LA CONSULTA
        Optional<Publicacion> pEntidad = publicacionRepository.findById(id);

        //SI EL RESULTADO VINO VACIO, RETORNAMOS LA EXCEPCION PERSONALIZADA
        if(pEntidad.isEmpty()){

            throw new ResourceNotFoundException("Publicacion","buscarPublicacionPorId()",id.toString());
        }

        //SI HAY RESULTADO, PROCEDEMOS A CREAR EL DTO NUEVO A PARTIR DE LOS DATOS DEL OBJETO RECIBIDO
        //Y DEVOLVEMOS.
        return new ModelMapper().map(pEntidad,PublicacionDTO.class);
    }

    @Override
    //MODIFICAMOS UN CAMPO PUBLICACION DE LA TABLA
    public PublicacionDTO modificarPublicacion(PublicacionDTO publicacion) {

        //OBTENEMOS EL RESULTADO OPCIONAL DE LA CONSULTA
        Optional<Publicacion> pEntidad = publicacionRepository.findById(publicacion.getId());

        //SI EL RESULTADO VINO VACIO, RETORNAMOS LA EXCEPCION PERSONALIZADA
        if(pEntidad.isEmpty()){

            throw new ResourceNotFoundException("Publicacion","modificarPublicacion()",publicacion.getId().toString());
        }

        //SI HAY RESULTADOS, PROCEDEMOS A CREAR EL OBJETO DE ENTIDAD A PARTIR DEL OBJETO DTO.
        Publicacion pToSave = modelMapper.map(publicacion,Publicacion.class);

        //GUARDAMOS EL OBJETO DE ENTIDAD CON SU TABLA EN LA BBDD
        Publicacion pSaved = publicacionRepository.save(pToSave);

        //CREAMOS UN DTO NUEVO Y LLENAMOS CON LOS DATOS DE ENTIDAD
        //Y RETORNAMOS
        return modelMapper.map(pSaved,PublicacionDTO.class);

    }

    @Override
    //ELIMINAMOS UN CAMPO PUBLICACION DE LA TABLA.
    public void eliminarPublicacion(PublicacionDTO publicacion) {

        //OBTENEMOS EL RESULTADO OPCIONAL DE LA CONSULTA
        Optional<Publicacion> pEntidad = publicacionRepository.findById(publicacion.getId());

        //SI EL RESULTADO VINO VACIO, RETORNAMOS LA EXCEPCION PERSONALIZADA
        if(pEntidad.isEmpty()){

            throw new ResourceNotFoundException("Publicacion","eliminarPublicacion()",publicacion.getId().toString());
        }
        //SI HAY RESULTADOS, PROCEDEMOS A ELIMINAR EL REGISTRO SEGÚN EL ID DE LA PUBLICACION DEL OBJETO DTO.
        publicacionRepository.deleteById(publicacion.getId());
    }
}
