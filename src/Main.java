import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        TabelaHashEF tabelaHash = new TabelaHashEF(26);
        List<String> palavrasChave = readFile("src\\palavra-chave.txt", StandardCharsets.UTF_8);

        for (String palavra : palavrasChave) {
            String res = "";
            for (Character c : palavra.toCharArray())
            {
                if(Character.isLetterOrDigit(c) || c.equals('-'))
                    res += c;
            }
            palavra = res;
            char letra = palavra.toLowerCase().charAt(0);

            String str = String.valueOf(letra);
            String letraSemAcento = Normalizer.normalize(str, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            letra = letraSemAcento.charAt(0);

            int posicao = letra - 'a';
            PalavraChave palavraChaveObj = new PalavraChave(palavra.toLowerCase());
            tabelaHash.add(letra, palavraChaveObj);
        }

        List<String> content = readFile("src\\texto.txt", StandardCharsets.UTF_8);

        int cont = 0;

        for (String linha : content) {


            cont++;
            int aux = 0;
            String[] palavras = linha.split(" ");
            for (String palavra : palavras) {
                String res = "";
                for (Character c : palavra.toCharArray())
                {
                    if(Character.isLetterOrDigit(c) || c.equals('-'))
                        res += c;
                }
                palavra = res;
                if (!palavra.isEmpty() && !palavra.equals("-")) {
                    palavras[aux] = palavra;
                    aux++;
                }
            }


            for (String palavra : palavras) {
                String res = "";
                for (Character c : palavra.toCharArray())
                {
                    if(Character.isLetterOrDigit(c) || c.equals('-'))
                        res += c;
                }
                palavra = res;
                if (!palavra.isEmpty()) {
                    char letra = palavra.toLowerCase().charAt(0);
                    int posicao = letra - 'a';

                    if (tabelaHash.contains(letra, palavra.toLowerCase())) {
                        tabelaHash.addLinha(letra, palavra.toLowerCase(), cont);
                    }

                }
            }


        }


        String outputPath = "src\\saida.txt";
        escreverArvoreEmArquivo(tabelaHash, outputPath);
    }

    static List<String> readFile(String path, Charset encoding) throws IOException {
        return Files.readAllLines(Paths.get(path), encoding);
    }

    static void escreverArvoreEmArquivo(TabelaHashEF tabelaHash, String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            for (int i = 0; i < tabelaHash.getTamanho(); i++) {
                if (tabelaHash.getArvore(i) != null) {
                    if(!tabelaHash.getArvore(i).raiz.palavraChave.linhas.isEmpty()) {
                        tabelaHash.getArvore(i).escreverEmOrdem(writer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class PalavraChave {
    String palavra;
    DynamicList<Integer> linhas;

    public PalavraChave(String palavra) {
        this.palavra = palavra;
        this.linhas = new DynamicList<>();
    }

    public String getPalavra() {
        return palavra;
    }

    public DynamicList<Integer> getLinhas() {
        return linhas;
    }
}

class TabelaHashEF {
    ArvoreAVL[] tabela;

    TabelaHashEF(int n) {
        tabela = new ArvoreAVL[n];
    }

    public void add(char letra, PalavraChave palavraChave) {
        int posicao = letra - 'a';
        if (tabela[posicao] == null) {
            tabela[posicao] = new ArvoreAVL();
        }
        tabela[posicao].inserir(palavraChave);
    }

    public int getTamanho() {
        return tabela.length;
    }

    public ArvoreAVL getArvore(int indice) {
        return tabela[indice];
    }


    public void escreverArvoreEmOrdem(PrintWriter writer) {
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                tabela[i].escreverEmOrdem(writer);
            }
        }
    }

    public void addLinha(char letra, String palavra, int linha) {
        String str = String.valueOf(letra);
        String letraSemAcento = Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        letra = letraSemAcento.charAt(0);

        int posicao = letra - 'a';
        if (tabela[posicao] != null) {
            tabela[posicao].adicionarLinha(palavra, linha);
        }
    }

    public boolean contains(char letra, String palavra) {
        String res = "";
        for (Character c : palavra.toCharArray())
        {
            if(Character.isLetterOrDigit(c) || c.equals('-'))
                res += c;
        }
        palavra = res;
        String str = String.valueOf(letra);
        String letraSemAcento = Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        System.out.println(palavra);
        System.out.println(letraSemAcento);
        letra = letraSemAcento.charAt(0);

        int posicao = letra - 'a';
        if (posicao<=25 && posicao>=0) {
            return tabela[posicao] != null && tabela[posicao].buscar(palavra);
        }else{return false;}
    }

    public void mostrarTabela() {
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                tabela[i].emOrdem();
            }
        }
    }
}


class ArvoreAVL {
    NoAVL raiz;

    public boolean buscar(String palavra) {
        return buscar(raiz, palavra);
    }

    public boolean buscar(NoAVL no, String palavra) {
        if (no == null) {
            return false;
        }

        int comparacao = palavra.compareTo(no.palavraChave.getPalavra());

        if (comparacao < 0) {
            return buscar(no.esquerda, palavra);
        } else if (comparacao > 0) {
            return buscar(no.direita, palavra);
        } else {
            return true;
        }
    }

    public void inserir(PalavraChave palavraChave) {
        raiz = inserir(raiz, palavraChave);
    }

    public NoAVL inserir(NoAVL no, PalavraChave palavraChave) {
        if (no == null) {
            return new NoAVL(palavraChave);
        }

        int comparacao = palavraChave.getPalavra().compareTo(no.palavraChave.getPalavra());
        if (comparacao < 0)
            no.esquerda = inserir(no.esquerda, palavraChave);
        else if (comparacao > 0)
            no.direita = inserir(no.direita, palavraChave);

        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
        int balanceamento = obterBalanceamento(no);


        if (balanceamento > 1 && palavraChave.getPalavra().compareTo(no.esquerda.palavraChave.getPalavra()) < 0)
            return rotacaoDireita(no);
        if (balanceamento < -1 && palavraChave.getPalavra().compareTo(no.direita.palavraChave.getPalavra()) > 0)
            return rotacaoEsquerda(no);

        if (balanceamento > 1 && palavraChave.getPalavra().compareTo(no.esquerda.palavraChave.getPalavra()) > 0) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }
        if (balanceamento < -1 && palavraChave.getPalavra().compareTo(no.direita.palavraChave.getPalavra()) < 0) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void adicionarLinha(String palavra, int linha) {
        raiz = adicionarLinha(raiz, palavra, linha);
    }

    private NoAVL adicionarLinha(NoAVL no, String palavra, int linha) {
        if (no == null) {
            PalavraChave palavraChave = new PalavraChave(palavra);
            palavraChave.getLinhas().adicionarFinal(linha);
            return new NoAVL(palavraChave);
        }

        int comparacao = palavra.compareTo(no.palavraChave.getPalavra());

        if (comparacao < 0)
            no.esquerda = adicionarLinha(no.esquerda, palavra, linha);
        else if (comparacao > 0)
            no.direita = adicionarLinha(no.direita, palavra, linha);
        else
            no.palavraChave.getLinhas().adicionarFinal(linha);

        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));

        return no;
    }

    public void escreverEmOrdem(PrintWriter writer) {
        escreverEmOrdem(raiz, writer);
    }

    private void escreverEmOrdem(NoAVL no, PrintWriter writer) {
        if(no != null) {
            if (no.palavraChave.getLinhas() != null) {
                escreverEmOrdem(no.esquerda, writer);
                writer.println(no.palavraChave.getPalavra() + " " + no.palavraChave.getLinhas().toString());
                escreverEmOrdem(no.direita, writer);
            }
        }
    }

    public int altura(NoAVL no) {
        return (no != null) ? no.altura : 0;
    }

    public int obterBalanceamento(NoAVL no) {
        return (no != null) ? altura(no.esquerda) - altura(no.direita) : 0;
    }

    public int max(int a, int b) {
        return Math.max(a, b);
    }

    public NoAVL rotacaoDireita(NoAVL y) {
        NoAVL x = y.esquerda;
        NoAVL T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        y.altura = max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = max(altura(x.esquerda), altura(x.direita)) + 1;

        return x;
    }

    public NoAVL rotacaoEsquerda(NoAVL x) {
        NoAVL y = x.direita;
        NoAVL T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        x.altura = max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = max(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    public void percorrerEmOrdem(NoAVL no) {
        if (no != null) {
            percorrerEmOrdem(no.esquerda);
            System.out.println(no.palavraChave.getPalavra() + " Linha(s): " + no.palavraChave.getLinhas().toString());
            percorrerEmOrdem(no.direita);
        }
    }

    public void emOrdem() {
        percorrerEmOrdem(raiz);
    }
}

class NoAVL {
    public PalavraChave palavraChave;
    public int altura;
    public NoAVL esquerda, direita;

    public NoAVL(PalavraChave palavraChave) {
        this.palavraChave = palavraChave;
        this.altura = 1;
    }
}

class DynamicList<T> {
    No<T> primeiro;
    No<T> ultimo;
    int cont = 0;

    public DynamicList() {
        primeiro = null;
        ultimo = null;
    }

    public int getCont() {
        return cont;
    }

    public void adicionarInicio(T valor) {
        No<T> novo = new No(valor);
        if (primeiro == null) {
            primeiro = novo;
            ultimo = novo;
        } else {
            novo.proximo = primeiro;
            primeiro = novo;
        }
        cont++;
    }

    public void adicionarFinal(T valor) {
        No<T> novo = new No(valor);
        if (primeiro == null) {
            primeiro = novo;
            ultimo = novo;
        } else if (constains(valor)) {
            return;
        } else {
            ultimo.proximo = novo;
            ultimo = novo;
        }
        cont++;
    }

    public void addPosicao(int pos, T valor) {
        if (pos == 0) {
            adicionarInicio(valor);
            return;
        } else if (pos == size()) {
            adicionarFinal(valor);
            return;
        } else {
            if (!posicaoExiste(pos)) {
                throw new ArrayIndexOutOfBoundsException(pos);
            }
            No<T> novo = new No(valor);
            No<T> atual = primeiro;
            for (int i = 0; i < pos - 1; i++) {
                atual = atual.proximo;
            }
            novo.proximo = atual.proximo;
            atual.proximo = novo;
            cont++;
        }
    }

    public void set(int pos, T valor) {
        No<T> aux = primeiro;
        for (int i = 0; i < pos; i++) {
            aux = aux.proximo;
        }
        aux.dado = valor;
    }

    public Object get(int pos) {
        if (!posicaoExiste(pos)) {
            throw new ArrayIndexOutOfBoundsException(pos);
        }
        No<T> atual = primeiro;
        for (int i = 0; i <= size(); i++) {
            if (i == pos) {
                break;
            }
            atual = atual.proximo;
        }
        return (T) atual.dado;
    }

    private boolean posicaoExiste(int pos) {
        if (pos < 0 || pos >= cont) {
            return false;
        }
        return true;
    }

    public void removerInicio() {
        if (primeiro != null) {
            if (primeiro == ultimo) {
                primeiro = null;
                ultimo = null;
            } else {
                primeiro = primeiro.proximo;
            }
            cont--;
        }
    }

    public void removerFinal() {
        if (ultimo != null) {
            if (ultimo == primeiro) {
                primeiro = null;
                ultimo = null;
            } else {
                No<T> aux = primeiro;
                while (aux.proximo != ultimo) {
                    aux = aux.proximo;
                }
                ultimo = aux;
                ultimo.proximo = null;
            }
            cont--;
        }
    }

    public void removerPosicao(int pos) {
        if (pos == 0) {
            removerInicio();
            return;
        } else if (pos == (size() - 1)) {
            removerFinal();
            return;
        } else {
            if (!posicaoExiste(pos)) {
                throw new ArrayIndexOutOfBoundsException(pos);
            }
            No<T> atual = primeiro;
            for (int i = 0; i < pos - 1; i++) {
                atual = atual.proximo;
            }
            atual.proximo = atual.proximo.proximo;
            cont--;
        }

    }

    public void removeNumber(T value) {
        No aux = primeiro;
        No antes = null;
        if (value == primeiro.dado) {
            removerInicio();
            return;
        } else if (value == ultimo.dado) {
            removerFinal();
            return;
        } else {
            while (aux != null) {
                if (aux.dado == value) {
                    break;
                }
                aux = aux.proximo;
            }
            aux.proximo = aux.proximo.proximo;
            cont--;
        }
    }

    public boolean constains(T valor) {
        No<T> aux = primeiro;
        while (aux != null) {
            if (aux.dado.equals(valor)) {
                return true;
            }
            aux = aux.proximo;
        }
        return false;
    }

    public int indexOf(T valor) {
        No<T> aux = primeiro;
        for (int i = 0; i < cont; i++) {
            if (aux.dado.equals(valor)) {
                return i;
            }
            aux = aux.proximo;
        }
        return -1;
    }

    public boolean isEmpty() {
        return cont == 0;
    }

    public void clear() {
        cont = 0;
        primeiro = null;
        ultimo = null;
    }

    public int size() {
        return cont;
    }

    public String toString() {
        No<T> aux = primeiro;
        StringBuilder str = new StringBuilder();
        int i = 0;
        while (aux != null) {
            str.append(aux.dado);
            if (i < cont - 1) {
                str.append(" ");
            }
            aux = aux.proximo;
            i++;
        }
        return str.toString();
    }
}

class No<T> {
    T dado;
    No<T> proximo;

    public No(T dado) {
        this.dado = dado;
        proximo = null;
    }
}