package com.yanmaia12.screenMatch.repository;

import com.yanmaia12.screenMatch.model.Categoria;
import com.yanmaia12.screenMatch.model.Episodio;
import com.yanmaia12.screenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    List<Serie> findByGenero(Categoria categoria);
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemps AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemps, Double avaliacao);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
    List<Episodio> episodioPorTrecho(String trecho);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo LIKE :nomeSerie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> top5EpisodiosDeSerie(String nomeSerie);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo LIKE :nomeSerie AND e.dataLancamento >= :data")
    List<Episodio> episodiosPorDataLancamento(String nomeSerie, LocalDate data);
    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> buscarTop5LancamentosRecentes();
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :nTemp")
    List<Episodio> obterTemporada(Long id, int nTemp);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> top5EpisodiosDeSeriePorId(Long id);

}
