package ve.com.tps.sistemablog.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ve.com.tps.sistemablog.dto.ComentarioDTO;
import ve.com.tps.sistemablog.dto.PageComentarios;
import ve.com.tps.sistemablog.dto.PublicacionDTO;
import ve.com.tps.sistemablog.entities.Comentarios;
import ve.com.tps.sistemablog.entities.Publicacion;
import ve.com.tps.sistemablog.exceptions.ResourceNotFoundException;
import ve.com.tps.sistemablog.repositories.ComentariosRepository;
import ve.com.tps.sistemablog.repositories.PublicacionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComentarioServicioImpl implements ComentariosServicio{

    //MODEL MAPPER PERMITE HACER LAS TRANSFORMACIONES ENTIDAD/DTO Y VICEVERSA RAPIDAMENTE.
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Override
    //AGREGAR UN REGISTRO COMENTARIO SEGUN SU PUBLICACION
    public ComentarioDTO agregarComentario(ComentarioDTO comentariosDTO, PublicacionDTO publicacionDTO) {
        //CREAMOS EL OBJETO DE ENTIDAD PUBLICACION A PARTIR DEL DTO PARA AÑADIRSELO AL COMENTARIO
        Publicacion pEntity = modelMapper.map(publicacionDTO,Publicacion.class);

        //CREAMOS EL OBJETO DE ENTIDAD COMENTARIO
        Comentarios comentariosEntity = modelMapper.map(comentariosDTO,Comentarios.class);
        comentariosEntity.setPublicacion(pEntity);

        //GUARDAMOS EL REGISTRO EN LA BBDD
        Comentarios cSaved = comentariosRepository.save(comentariosEntity);

        //LE INDICAMOS EL ID GUARDADO Y LA PUBLICACION AL DTO, Y LO RETORNAMOS
        comentariosDTO.setId(cSaved.getId());
        return  comentariosDTO;
    }

    @Override
    public void eliminarComentarioPorId(Integer id) {

        //ELIMINAMOS UN REGISTRO SEGUN EL ID RECIBIDO
        comentariosRepository.deleteById(id);
    }

    @Override
    public ComentarioDTO modificarComentario(ComentarioDTO comentariosDTO,PublicacionDTO publicacionDTO) {
        //CREAMOS EL OBJETO DE ENTIDAD PUBLICACION A PARTIR DEL DTO PARA AÑADIRSELO AL COMENTARIO
        Publicacion pEntity = modelMapper.map(publicacionDTO,Publicacion.class);

        //CREAMOS EL OBJETO DE ENTIDAD COMENTARIO
        Comentarios comentariosEntity = modelMapper.map(comentariosDTO,Comentarios.class);
        comentariosEntity.setPublicacion(pEntity);

        //GUARDAMOS EL OBJETO MODIFICADO
        comentariosRepository.save(comentariosEntity);

        //RETORNAMOS RESULTADO
        return  comentariosDTO;

    }

    @Override
    public PageComentarios listarComentariosDePublicacion(Integer id,Integer numeroPagina, Integer medidaPagina,String ordenarPor,String ordenDir) {

        //CREAMOS UN ORDENAMIENTO PARA INDICARLE A LA PAGINACION
        Sort sort = ordenDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending();

        //CREAMOS UN OBJETO PAGE PARA PODER INDICAR LA PAGINA DE INICIO, LA CANTIDAD DE ELEMENTOS POR PAGINA Y EL ORDENAMIENTO
        Pageable pageable = PageRequest.of(numeroPagina,medidaPagina,sort);

        //OBTENEMOS LOS CAMPOS DE LA BBDD ORDENADOS SEGUN LA PAGINACION
        Page<Comentarios> comentariosPage = comentariosRepository.findAll(pageable);

        //OBTENEMOS LA LISTA DE OBJETOS DE ENTIDAD DESDE LA BBDD
        List<Comentarios> listaEntity = comentariosRepository.findByPublicacionId(id);

        if(listaEntity.isEmpty()){

            throw new ResourceNotFoundException("List<Comentarios>","listarComentariosDePublicacion()",id.toString());
        }

        //CREAMOS UNA LISTA DE OBJETOS DTO A RELLENAR
        List<ComentarioDTO> listaDTO = new ArrayList<>();

        //CICLAMOS EL LSITADO DE ENTIDAD Y A PARTIR DE SUS DATOS INSTANCIAMOS LOS DTO
        for (Comentarios c : listaEntity) {

            PublicacionDTO p = modelMapper.map(c.getPublicacion(),PublicacionDTO.class);

            ComentarioDTO cDTO = modelMapper.map(c,ComentarioDTO.class);
            cDTO.setPublicacion(p);
            //AGREGAMOS A LA LISTA DTO EL OBJETO DTO CREADO
            listaDTO.add(cDTO);
        }
        //RETORNAMOS LA LISTA DTO COMPLETA.
        PageComentarios pageC = new PageComentarios();
        pageC.setContenido(listaDTO);
        pageC.setUltima(comentariosPage.isLast());
        pageC.setMedidaPagina(comentariosPage.getSize());
        pageC.setNroPagina(comentariosPage.getNumber());
        pageC.setTotalPaginas(comentariosPage.getTotalPages());
        pageC.setTotalElementos(comentariosPage.getTotalElements());
        return pageC;
    }

    @Override
    public ComentarioDTO buscarComentarioPorIdYPublicacionId(Integer id, Integer idPublicacion) {

        Comentarios cEntity = comentariosRepository.findByIdAndPublicacionId(id,idPublicacion);

        if(cEntity == null){
                throw new ResourceNotFoundException("Comentarios","buscarComentarioPorIdYPublicacionId",id.toString());
        }

        Publicacion pEntity = cEntity.getPublicacion();

        PublicacionDTO p = modelMapper.map(pEntity,PublicacionDTO.class);

        ComentarioDTO cDTO = modelMapper.map(cEntity,ComentarioDTO.class);
        cDTO.setPublicacion(p);

        return cDTO;
    }
}
