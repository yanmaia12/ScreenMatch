package com.yanmaia12.screenMatch.Service;

import com.yanmaia12.screenMatch.model.DadosSerie;
import com.yanmaia12.screenMatch.service.ConverteDados;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConverteDadosTest {
    private final ConverteDados converteDados = new ConverteDados();

    @Test
    void deveConverterJsonParaObjetoDadosSerie(){
        String json = """
                {
                "Title": "Matrix",
                "totalSeasons": 1,
                "imdbRating": "8.3",
                "Genre": "Sci-Fi",
                "Actors": "Keanu Reaves",
                "Poster": "http://link.com/poster.jpg",
                "Plot": "A computer hacker learns from mysterious rebels about the true nature of his reality."
                }
                """;
        DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);

        Assertions.assertNotNull(dadosSerie);
        Assertions.assertEquals("Matrix", dadosSerie.titulo());
        Assertions.assertEquals(1, dadosSerie.totalTemporadas());
        Assertions.assertEquals("8.3", dadosSerie.avaliacao());
        Assertions.assertEquals("Sci-Fi", dadosSerie.genero());
        Assertions.assertEquals("Keanu Reaves", dadosSerie.atores());
        Assertions.assertEquals("http://link.com/poster.jpg", dadosSerie.poster());
        Assertions.assertEquals("A computer hacker learns from mysterious rebels about the true nature of his reality.", dadosSerie.sinopse());


    }


}
