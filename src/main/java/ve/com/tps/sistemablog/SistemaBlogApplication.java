package ve.com.tps.sistemablog;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemaBlogApplication {

	//CREAMOS UN BEAN PARA MODEL MAPPER, QUE NOS AYUDARÁ CON LAS TRANSFORMACIONES ENTIDAD-DTO.
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SistemaBlogApplication.class, args);
	}

}
