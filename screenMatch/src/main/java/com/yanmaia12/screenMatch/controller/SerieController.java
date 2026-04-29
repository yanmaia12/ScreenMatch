package com.yanmaia12.screenMatch.controller;

import com.yanmaia12.screenMatch.dto.EpisodioDTO;
import com.yanmaia12.screenMatch.dto.SerieDTO;
import com.yanmaia12.screenMatch.service.SerieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    private final SerieService serieService;
    public SerieController(SerieService serieService){
        this.serieService = serieService;
    }

    @GetMapping
    public List<SerieDTO> obterSeries(){
        return serieService.obterTodasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series(){
        return serieService.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return serieService.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterSeriePorId(@PathVariable Long id){
        return serieService.obterSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{nTemp}")
    public List<EpisodioDTO> obterTemporada(@PathVariable Long id, @PathVariable int nTemp){
        return serieService.obterTemporada(id, nTemp);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> obterSeriesGenero(@PathVariable String genero){
        return serieService.obterSeriesGenero(genero);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> obterTop5EpisodiosSeriePorId(@PathVariable Long id){
        return serieService.obterTop5EpisodiosSeriePorId(id);
    }

    @PostMapping("/adicionar/{nomeSerie}")
    public ResponseEntity<?> adicionarSerie(@PathVariable String nomeSerie){
        try {
            SerieDTO serie = serieService.buscarAdicionarSerie(nomeSerie);
            return ResponseEntity.ok(serie);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
