package com.yanmaia12.screenMatch.dto;

import java.time.LocalDate;

public record EpisodioDTO(Long id,
                          Integer temporada,
                          String titulo,
                          Integer numeroEpisodio,
                          Double avaliacao,
                          LocalDate dataLancamento) {
}
