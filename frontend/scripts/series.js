import getDados from "./getDados.js";

const params = new URLSearchParams(window.location.search);
const serieId = params.get('id');
const listaTemporadas = document.getElementById('temporadas-select');
const fichaSerie = document.getElementById('temporadas-episodios');
const fichaDescricao = document.getElementById('ficha-descricao');

// Função para carregar temporadas
function carregarTemporadas() {
    getDados(`/series/${serieId}/temporadas/todas`)
        .then(data => {
            const temporadasUnicas = [...new Set(data.map(temporada => temporada.temporada))];
            listaTemporadas.innerHTML = ''; // Limpa as opções existentes

            const optionDefault = document.createElement('option');
            optionDefault.value = '';
            optionDefault.textContent = 'SELECIONE A TEMPORADA'
            listaTemporadas.appendChild(optionDefault); 
           
            temporadasUnicas.forEach(temporada => {
                const option = document.createElement('option');
                option.value = temporada;
                option.textContent = temporada;
                listaTemporadas.appendChild(option);
            });

            const optionTodos = document.createElement('option');
            optionTodos.value = 'todas';
            optionTodos.textContent = 'TODAS AS TEMPORADAS'
            listaTemporadas.appendChild(optionTodos); 

            // >>> ADICIONAMOS A NOSSA NOVA OPÇÃO AQUI <<<
            const optionTop = document.createElement('option');
            optionTop.value = 'top'; // Isso fará o JS chamar a rota /series/id/temporadas/top
            optionTop.textContent = 'MELHORES EPISÓDIOS (TOP 5)'
            listaTemporadas.appendChild(optionTop);
        })
        .catch(error => {
            console.error('Erro ao obter temporadas:', error);
        });
}

// Função para carregar episódios de uma temporada
function carregarEpisodios() {
    getDados(`/series/${serieId}/temporadas/${listaTemporadas.value}`)
        .then(data => {
            fichaSerie.innerHTML = ''; 

            // >>> SE FOR OS MELHORES EPISÓDIOS, DESENHAMOS UMA LISTA ESPECIAL <<<
            if (listaTemporadas.value === 'top') {
                const ul = document.createElement('ul');
                ul.className = 'episodios-lista';

                const listaHTML = data.map(serie => `
                    <li>
                        <b>T${String(serie.temporada).padStart(2, '0')} E${String(serie.numeroEpisodio).padStart(2, '0')}</b> - ${serie.titulo}
                        <span class="nota-badge">NOTA: ${Number(serie.avaliacao).toFixed(1)}</span>
                    </li>
                `).join('');
                ul.innerHTML = listaHTML;
                
                const paragrafo = document.createElement('p');
                paragrafo.textContent = `Top 5 Episódios`;
                fichaSerie.appendChild(paragrafo);
                fichaSerie.appendChild(ul);

            } else {
                // >>> LÓGICA PADRÃO: AGRUPAR POR TEMPORADA <<<
                const temporadasUnicas = [...new Set(data.map(temporada => temporada.temporada))];
                
                temporadasUnicas.forEach(temporada => {
                    const ul = document.createElement('ul');
                    ul.className = 'episodios-lista';

                    const episodiosTemporadaAtual = data.filter(serie => serie.temporada === temporada);

                    const listaHTML = episodiosTemporadaAtual.map(serie => `
                        <li>
                            ${serie.numeroEpisodio} - ${serie.titulo}
                        </li>
                    `).join('');
                    ul.innerHTML = listaHTML;
                    
                    const paragrafo = document.createElement('p');
                    paragrafo.textContent = `Temporada ${temporada}`;
                    fichaSerie.appendChild(paragrafo);
                    fichaSerie.appendChild(ul);
                });
            }
        })
        .catch(error => {
            console.error('Erro ao obter episódios:', error);
        });
}

// Função para carregar informações da série
function carregarInfoSerie() {
    getDados(`/series/${serieId}`)
        .then(data => {
            fichaDescricao.innerHTML = `
                <img src="${data.poster}" alt="${data.titulo}" />
                <div>
                    <h2>${data.titulo}</h2>
                    <div class="descricao-texto">
                        <p><b>Média de avaliações:</b> ${Number(data.avaliacao).toFixed(1)}</p>
                        <p><b>Sinopse:</b> ${data.sinopse}</p>
                        <p><b>Estrelando:</b> ${data.atores}</p>
                    </div>
                </div>
            `;
        })
        .catch(error => {
            console.error('Erro ao obter informações da série:', error);
        });
}

// Adiciona ouvinte de evento para o elemento select
listaTemporadas.addEventListener('change', carregarEpisodios);

// Carrega as informações da série e as temporadas quando a página carrega
carregarInfoSerie();
carregarTemporadas();
