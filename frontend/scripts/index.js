import getDados from "./getDados.js";

// Mapeia os elementos DOM que você deseja atualizar
const elementos = {
    top5: document.querySelector('[data-name="top5"]'),
    lancamentos: document.querySelector('[data-name="lancamentos"]'),
    series: document.querySelector('[data-name="series"]')
};

// Função para criar a lista de filmes

// Função para criar a lista de filmes
function criarListaFilmes(elemento, dados) {
    // Verifique se há um elemento <ul> dentro da seção
    const ulExistente = elemento.querySelector('ul');

    // Se um elemento <ul> já existe dentro da seção, remova-o
    if (ulExistente) {
        elemento.removeChild(ulExistente);
    }

    const ul = document.createElement('ul');
    ul.className = 'lista';
    const listaHTML = dados.map((filme) => `
        <li>
            <a href="/detalhes.html?id=${filme.id}">
                <img src="${filme.poster}" alt="${filme.titulo}">
            </a>
        </li>
    `).join('');

    ul.innerHTML = listaHTML;
    elemento.appendChild(ul);
}

// Função genérica para tratamento de erros
function lidarComErro(mensagemErro) {
    console.error(mensagemErro);
}

const categoriaSelect = document.querySelector('[data-categorias]');
const sectionsParaOcultar = document.querySelectorAll('.section'); // Adicione a classe CSS 'hide-when-filtered' às seções e títulos que deseja ocultar.

categoriaSelect.addEventListener('change', function () {
    const categoria = document.querySelector('[data-name="categoria"]');
    const categoriaSelecionada = categoriaSelect.value;

    if (categoriaSelecionada === 'todos') {

        for (const section of sectionsParaOcultar) {
            section.classList.remove('hidden')
        }
        categoria.classList.add('hidden');

    } else {

        for (const section of sectionsParaOcultar) {
            section.classList.add('hidden')
        }

        categoria.classList.remove('hidden')
        // Faça uma solicitação para o endpoint com a categoria selecionada
        getDados(`/series/categoria/${categoriaSelecionada}`)
            .then(data => {
                criarListaFilmes(categoria, data);
            })
            .catch(error => {
                lidarComErro("Ocorreu um erro ao carregar os dados da categoria.");
            });
    }
});

// Array de URLs para as solicitações
geraSeries();
function geraSeries() {
    const urls = ['/series/top5', '/series/lancamentos', '/series'];

    // Faz todas as solicitações em paralelo
    Promise.all(urls.map(url => getDados(url)))
        .then(data => {
            criarListaFilmes(elementos.top5, data[0]);
            console.log("O que o Java tá mandando pros Lançamentos:", data[1]);
            criarListaFilmes(elementos.lancamentos, data[1]);
            criarListaFilmes(elementos.series, data[2]);
        })
        .catch(error => {
            lidarComErro("Ocorreu um erro ao carregar os dados.");
        });

}

// ==========================================
// LÓGICA DO MODAL DE BUSCA/ADIÇÃO DE SÉRIES
// ==========================================
const btnAbrirBusca = document.getElementById('btn-abrir-busca');
const modalBusca = document.getElementById('modal-busca');
const btnFecharModal = document.getElementById('btn-fechar-modal');
const formBusca = document.getElementById('form-busca');
const inputNomeSerie = document.getElementById('input-nome-serie');
const mensagemModal = document.getElementById('mensagem-modal');

// Abre o modal nativo do HTML
btnAbrirBusca.addEventListener('click', () => {
    modalBusca.showModal();
    inputNomeSerie.focus();
});

// Fecha o modal
btnFecharModal.addEventListener('click', () => {
    modalBusca.close();
    mensagemModal.className = 'mensagem-modal hidden'; // Limpa mensagens antigas
    inputNomeSerie.value = '';
});

// Envia os dados para o Java
formBusca.addEventListener('submit', async (e) => {
    e.preventDefault(); // Impede a página de recarregar
    
    const nomeDaSerie = inputNomeSerie.value.trim();
    if(!nomeDaSerie) return;

    const btnBuscar = document.getElementById('btn-buscar');
    btnBuscar.textContent = 'PROCESSANDO...';
    btnBuscar.disabled = true;

    try {
        const resposta = await fetch(`http://localhost:8080/series/adicionar/${encodeURIComponent(nomeDaSerie)}`, {
            method: 'POST'
        });

        mensagemModal.classList.remove('hidden');

        if (resposta.ok) { 
            // Agora lemos a resposta como JSON em vez de texto!
            const serie = await resposta.json();
            
            mensagemModal.textContent = "SÉRIE PRONTA! ABRINDO DOSSIÊ...";
            mensagemModal.className = 'mensagem-modal mensagem-sucesso';
            
            // Espera 1.5 segundos para o usuário ler a mensagem e redireciona!
            setTimeout(() => {
                // OBS: Confirme se o nome do seu arquivo HTML de detalhes é esse mesmo
                window.location.href = `detalhes.html?id=${serie.id}`; 
            }, 1500); 

        } else { 
            const erroTexto = await resposta.text();
            mensagemModal.textContent = "ERRO: " + (erroTexto || "SÉRIE NÃO ENCONTRADA.");
            mensagemModal.className = 'mensagem-modal mensagem-erro';
        }
    } catch (error) {
        mensagemModal.classList.remove('hidden');
        mensagemModal.textContent = "FALHA NA COMUNICAÇÃO COM O SERVIDOR.";
        mensagemModal.className = 'mensagem-modal mensagem-erro';
    } finally {
        btnBuscar.textContent = 'PESQUISAR E ADICIONAR >>';
        btnBuscar.disabled = false;
    }
});
