package com.yanmaia12.screenMatch.service;

import com.yanmaia12.screenMatch.dto.EpisodioDTO;
import com.yanmaia12.screenMatch.dto.SerieDTO;
import com.yanmaia12.screenMatch.model.*;
import com.yanmaia12.screenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SerieService {

    private final SerieRepository serieRepository;
    private final ConsumoApi consumoApi;
    private final ConverteDados converteDados = new ConverteDados();
    public static final String ENDERECO = "https://www.omdbapi.com/?t=";
    @Value("${omdb.apikey}")
    private String apikey;

    public SerieService(SerieRepository serieRepository, ConsumoApi consumoApi) {
        this.serieRepository = serieRepository;
        this.consumoApi = consumoApi;
    }

    public List<SerieDTO> obterTodasSeries() {
        return converterListaSerie(serieRepository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converterListaSerie(serieRepository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converterListaSerie(serieRepository.buscarTop5LancamentosRecentes());
    }

    public SerieDTO obterSeriePorId(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()) {
            return converterSerie(serie.get());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios()
                    .stream()
                    .map(e -> new EpisodioDTO(e.getId(), e.getTemporada(), e.getTitulo(),
                            e.getNumeroEpisodio(), e.getAvaliacao(), e.getDataLancamento()))
                    .toList();
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporada(Long id, int nTemp) {
        return serieRepository.obterTemporada(id, nTemp).stream()
                .map(e -> new EpisodioDTO(e.getId(), e.getTemporada(), e.getTitulo(),
                        e.getNumeroEpisodio(), e.getAvaliacao(), e.getDataLancamento()))
                .toList();
    }

    public List<SerieDTO> obterSeriesGenero(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);
        return converterListaSerie(serieRepository.findByGenero(categoria));
    }

    public List<EpisodioDTO> obterTop5EpisodiosSeriePorId(Long id) {
        return serieRepository.top5EpisodiosDeSeriePorId(id).stream()
                .map(e -> new EpisodioDTO(e.getId(), e.getTemporada(), e.getTitulo(),
                        e.getNumeroEpisodio(), e.getAvaliacao(), e.getDataLancamento()))
                .toList();
    }

    public SerieDTO buscarAdicionarSerie(String nomeSerie){
        Optional<Serie> serieExiste = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieExiste.isPresent()){
            return converterSerie(serieExiste.get());
        }
        Serie serie = getSerie(nomeSerie);
        adicionarEpisodios(serie);
        serieRepository.save(serie);

        return converterSerie(serie);
    }

    public List<SerieDTO> converterListaSerie(List<Serie> listaSerie) {
        return listaSerie
                .stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(),
                        s.getAvaliacao(), s.getGenero(),
                        s.getAtores(), s.getPoster(), s.getSinopse()))
                .toList();
    }


    public SerieDTO converterSerie(Serie s) {
        return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(),
                s.getAvaliacao(), s.getGenero(),
                s.getAtores(), s.getPoster(), s.getSinopse());
    }

    private Serie getSerie(String nomeSerie) {
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&apikey=" + apikey);
        DadosSerie dados = converteDados.obterDados(json, DadosSerie.class);
        if (dados == null || dados.titulo() == null){
            throw new RuntimeException("Série não encontrada no OMDB");
        }
        return new Serie(dados);
    }

    private void adicionarEpisodios(Serie serie){
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
            var json = consumoApi.obterDados(ENDERECO + serie.getTitulo().replace(" ", "+") + "&season=" + i  + "&apikey=" +     apikey);
            DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        List<Episodio> episodios = temporadas.stream().flatMap(e -> e.episodios().stream().map(d -> new Episodio(e.numero(), d))).toList();
        serie.setEpisodios(episodios);
    }

}
