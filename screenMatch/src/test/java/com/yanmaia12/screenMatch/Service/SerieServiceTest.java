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
}
