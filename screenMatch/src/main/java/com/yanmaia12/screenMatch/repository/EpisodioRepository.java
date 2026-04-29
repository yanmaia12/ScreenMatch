package com.yanmaia12.screenMatch.repository;

import com.yanmaia12.screenMatch.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodioRepository extends JpaRepository<Episodio, Long> {
    List<Episodio> findTop5ByOrderByAvaliacaoDesc();
}
