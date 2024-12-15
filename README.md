# Índice Remissivo com Árvore AVL

## Descrição

Este projeto implementa um sistema de indexação de palavras-chave em um texto, utilizando uma tabela hash e árvores AVL para organização das palavras. Ele lê dois arquivos de entrada:

- Um arquivo de palavras-chave (`palavra-chave.txt`).
- Um arquivo de texto (`texto.txt`).

O programa identifica as palavras-chave presentes no texto, registra as linhas onde aparecem e exporta o índice gerado para um arquivo de saída (`saida.txt`).

## Estrutura do Projeto

- **Tabela Hash:** Estrutura principal que contém árvores AVL, onde cada posição representa uma letra inicial das palavras.
- **Árvore AVL:** Estrutura balanceada que organiza as palavras-chave e as linhas em que aparecem no texto.
- **Palavra Chave:** Classe que representa uma palavra-chave e as linhas onde foi encontrada.

## Funcionalidades

1. **Leitura de Arquivos:**
   - Lê as palavras-chave de `palavra-chave.txt` e as insere na tabela hash.
   - Lê o texto de `texto.txt` e verifica se as palavras correspondem às palavras-chave.

2. **Normalização e Tratamento:**
   - Remove caracteres não alfanuméricos e converte caracteres acentuados para formas sem acento.

3. **Armazenamento:**
   - Insere as palavras-chave em árvores AVL balanceadas.
   - Armazena as linhas do texto onde as palavras-chave aparecem.

4. **Exportação de Dados:**
   - Gera um arquivo `saida.txt` com a lista de palavras-chave e as linhas correspondentes em ordem alfabética.

## Estrutura de Arquivos

- `src/palavra-chave.txt`: Arquivo com palavras-chave, uma por linha.
- `src/texto.txt`: Arquivo com o texto analisado, com palavras separadas por espaços.
- `src/saida.txt`: Arquivo gerado com o índice de palavras e as linhas em que aparecem.

## Como Executar

1. **Preparação dos Arquivos:**
   - Insira `palavra-chave.txt` e `texto.txt` no diretório `src`.
   - Estruture os arquivos conforme descrito:
     - `palavra-chave.txt`: Uma palavra-chave por linha.
     - `texto.txt`: Texto a ser analisado.

2. **Saída:**
   - O arquivo `saida.txt` será gerado no diretório `src` com as palavras-chave e as linhas em que aparecem.

## Exemplo de Saída

Se `palavra-chave.txt` contiver:
exemplo projeto java

E `texto.txt` contiver:
Este é um exemplo de projeto. Outro exemplo utilizando Java. Este projeto é feito em Java.

O arquivo `saida.txt` gerado será:
exemplo [1, 2] java [2, 3] projeto [1, 3]


## Classes e Métodos Principais

1. **Main**
   - `readFile(String path, Charset encoding)`: Lê os arquivos e retorna uma lista de strings.
   - `escreverArvoreEmArquivo(TabelaHashEF tabelaHash, String outputPath)`: Gera o arquivo `saida.txt`.

2. **TabelaHashEF**
   - `add(char letra, PalavraChave palavraChave)`: Insere uma palavra-chave na tabela hash.
   - `addLinha(char letra, String palavra, int linha)`: Registra a linha de ocorrência de uma palavra.
   - `contains(char letra, String palavra)`: Verifica se a palavra existe na tabela hash.

3. **ArvoreAVL**
   - `inserir(PalavraChave palavraChave)`: Insere palavras na árvore AVL.
   - `adicionarLinha(String palavra, int linha)`: Adiciona linha de ocorrência.
   - `escreverEmOrdem(PrintWriter writer)`: Escreve palavras e linhas em ordem alfabética.

4. **PalavraChave**
   - Representa palavras-chave e as linhas associadas.

## Autor

Este projeto foi desenvolvido para fins de aprendizado, destacando o uso de tabelas hash e árvores AVL em Java.
