package br.com.fiap.clinicamedica.bean;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies; // Opcional, mas recomendado

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ModelMapperProducer {

    @Produces
    @ApplicationScoped
    public ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();


        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}