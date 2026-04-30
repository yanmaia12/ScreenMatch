package com.yanmaia12.screenMatch.Service;

import com.yanmaia12.screenMatch.dto.SerieDTO;
import com.yanmaia12.screenMatch.model.Serie;
import com.yanmaia12.screenMatch.repository.SerieRepository;
import com.yanmaia12.screenMatch.service.ConsumoApi;
import com.yanmaia12.screenMatch.service.SerieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SerieServiceTest {
    @Mock
    private SerieRepository serieRepository;
    @Mock
    private ConsumoApi consumoApi;

    @InjectMocks
    private SerieService serieService;

    @Test
    void deveRetornarSerieDTOQuandoExistirNoBanco(){
        Long idBuscado = 1L;
        Serie serieTeste = new Serie();

        Mockito.when(serieRepository.findById(idBuscado)).thenReturn(Optional.of(serieTeste));

        SerieDTO serieDTO = serieService.obterSeriePorId(idBuscado);

        Assertions.assertNotNull(serieDTO, "Não pode ser null");
        Mockito.verify(serieRepository, Mockito.times(1)).findById(idBuscado);
    }

    @Test
    void  deveRetornarNullQuandoIdNaoExistir(){
        Long idBuscado = 2L;

        Mockito.when(serieRepository.findById(idBuscado)).thenReturn(Optional.empty());

        SerieDTO serieDTO = serieService.obterSeriePorId(idBuscado);

        Assertions.assertNull(serieDTO, "O resultado deve ser null");
        Mockito.verify(serieRepository, Mockito.times(1)).findById(idBuscado);
    }

    @Test
    void deveRetornarListaDeSerieDTOQuandoExistiremSeriesNoBanco(){
        Serie serie1 = new Serie();
        Serie serie2 = new Serie();
        List<Serie> listaSerieTest = List.of(serie1, serie2);


        Mockito.when(serieRepository.findAll()).thenReturn(listaSerieTest);
        List<SerieDTO> listaSerie = serieService.obterTodasSeries();

        Assertions.assertEquals(2, listaSerie.size());

        Mockito.verify(serieRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deveRetornarTop5SeriesQuandoExistirem(){
        Serie serie1 = new Serie();
        Serie serie2 = new Serie();
        Serie serie3 = new Serie();
        Serie serie4 = new Serie();
        Serie serie5 = new Serie();

        List<Serie> listaSerieTeste = List.of(serie1, serie2, serie3, serie4, serie5);

        Mockito.when(serieRepository.findTop5ByOrderByAvaliacaoDesc()).thenReturn(listaSerieTeste);

        List<SerieDTO> listaSerie = serieService.obterTop5Series();

        Assertions.assertEquals(5, listaSerie.size());
        Mockito.verify(serieRepository, Mockito.times(1)).findTop5ByOrderByAvaliacaoDesc();
    }

    @Test
    void deveRetornarNullQuandoChamaTop5SeriesQueNaoExistem(){
        List<Serie> listaSerieTeste = new ArrayList<>();

        Mockito.when(serieRepository.findTop5ByOrderByAvaliacaoDesc()).thenReturn(listaSerieTeste);

        List<SerieDTO> listaSerie = serieService.obterTop5Series();

        Assertions.assertEquals(0, listaSerie.size());
        Mockito.verify(serieRepository, Mockito.times(1)).findTop5ByOrderByAvaliacaoDesc();
    }
}
