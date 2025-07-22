package esd;

@SuppressWarnings("unchecked")
public class TabHash<K extends Comparable<K>, T> {

    public class Par implements Comparable<Par> {
        K chave;
        T valor;
        TabHash<K, T> novo_atributo;

        public K obtemChave() {
            return chave;
        }

        public T obtemValor() {
            return valor;
        }

        public Par(K chave, T valor) {
            this.chave = chave;
            this.valor = valor;
        }

        @Override
        public int compareTo(Par outro) {
            return this.chave.compareTo(outro.chave); // Usa a chave como critério de ordenação
        }

    }

    ListaSequencial<ListaSequencial<Par>> tab = new ListaSequencial<ListaSequencial<Par>>();
    int len = 0;// chaves
    double fatorCarga = 0.75;
    final int defcap = 30; // linhas inical
    int linhas = defcap;

    public TabHash() {
        for (int j = 0; j < defcap; j++) {
            tab.adiciona(new ListaSequencial<>());
        }
    }

    int calc_hash(K chave) {
        // retorna um inteiro entre 0 e defcap - cada inteiro representa uma linha
        return Math.abs((chave.hashCode())) % tab.comprimento();
    }

    int calc_hash(K chave, int tam) {
        // retorna um inteiro entre 0 e defcap - cada inteiro representa uma linha
        return Math.abs((chave.hashCode())) % tam;
    }

    // expande a tabela hash, de forma que dobre a quantidade de linhas
    // os pares deve ser redistribuídos na tabela (rehashing)
    void expande() {
        // 1. Expande a tabela: como é formada por uma ListaSequencial, deve-se criar
        // uma nova ListaSequencial contendo o dobro de linhas (listas vazias) da tabela
        // atual
        ListaSequencial<ListaSequencial<Par>> newTab = new ListaSequencial<>();
        for (int j = 0; j < 2 * linhas; j++) {
            newTab.adiciona(new ListaSequencial<>());
        }

        // 2. para cada par da tabela atual, deve-se recalcular o hash de
        // sua chave, e adicioná-lo à nova lista
        for (int j = 0; j < linhas; j++) {
            ListaSequencial<Par> linhaTab = tab.obtem(j);
            for (int a = 0; a < linhaTab.comprimento(); a++) {
                Par par = linhaTab.obtem(a);
                K parChave = par.obtemChave();
                T parValor = par.obtemValor();
                int newHash = calc_hash(parChave, newTab.comprimento());
                ListaSequencial<Par> linhaNewTab = newTab.obtem(newHash);
                Par existente = obtem_par(linhaNewTab, parChave);

                if (existente != null) {
                    existente.valor = parValor;
                } else {
                    linhaNewTab.adiciona(new Par(parChave, parValor));
                }
            }
        }
        linhas *= 2;
        // 3. ao final, substituir a tabela atual pela nova tabela
        tab = newTab;
    }

    public ListaSequencial<Par> obtem_linha(K chave) {
        int hash = calc_hash(chave);
        return tab.obtem(hash);
    }

    public T obtem(K chave) {
        if (len == 0)
            throw new IndexOutOfBoundsException("hashTab vazia");
        int hash = calc_hash(chave);
        ListaSequencial<Par> linha = tab.obtem(hash);

        if (obtem_par(linha, chave) != null)
            return obtem_par(linha, chave).obtemValor();
        else {
            throw new IndexOutOfBoundsException("hash não econtrada");
        }
    }

    public T obtem(K chave, ListaSequencial<ListaSequencial<Par>> newTab) {
        if (len == 0)
            throw new IndexOutOfBoundsException("hashTab vazia");
        int hash = calc_hash(chave);
        ListaSequencial<Par> linha = newTab.obtem(hash);

        if (obtem_par(linha, chave) != null)
            return obtem_par(linha, chave).obtemValor();
        else {
            throw new IndexOutOfBoundsException("hash não econtrada");
        }
    }

    public T obtem_ou_default(K chave, T defval) {

        int hash = calc_hash(chave);
        ListaSequencial<Par> linha = tab.obtem(hash);

        if (obtem_par(linha, chave) != null)
            return obtem_par(linha, chave).obtemValor();
        else {
            return defval;
        }
    }

    public Par obtem_par(ListaSequencial<Par> linha, K chave) {
        for (int a = 0; a < linha.comprimento(); a++) {
            if (linha.obtem(a).obtemChave().equals(chave)) {
                return linha.obtem(a);
            }
        }
        return null;
    }

    public void adiciona(K chave, T valor) {
        int FatorCargaAtual = len / linhas;
        if (FatorCargaAtual > fatorCarga) expande();

        ListaSequencial<Par> linha = obtem_linha(chave);
        Par existente = obtem_par(linha, chave);

        if (existente != null) {
            existente.valor = valor;
        } else {
            linha.adiciona(new Par(chave, valor));
            len++;
        }
    }

    public void remove(K chave) {
        ListaSequencial<Par> linha = obtem_linha(chave);
        for (int i = 0; i < linha.comprimento(); i++) {
            if (linha.obtem(i).obtemChave().equals(chave)) {
                linha.remove(i);
                len--;
                return;
            }
        }
    }

    public boolean contem(K chave) {
        ListaSequencial<Par> linha = obtem_linha(chave);
        for (int i = 0; i < linha.comprimento(); i++) {
            if (linha.obtem(i).obtemChave().equals(chave)) {
                return true;
            }
        }
        return false;
    }

    public boolean esta_vazia() {
        return len == 0;
    }

    public ListaSequencial<T> valores() {
        ListaSequencial<T> saida = new ListaSequencial<>();
        for (int a = 0; a < tab.comprimento(); a++) {
            for (int b = 0; b < tab.obtem(a).comprimento(); b++) {
                saida.adiciona(tab.obtem(a).obtem(b).obtemValor());
            }
        }
        return saida;
    }

    public ListaSequencial<K> chaves() {
        ListaSequencial<K> saida = new ListaSequencial<>();
        for (int a = 0; a < tab.comprimento(); a++) {
            for (int b = 0; b < tab.obtem(a).comprimento(); b++) {
                saida.adiciona(tab.obtem(a).obtem(b).obtemChave());
            }
        }
        return saida;
    }

    public ListaSequencial<Par> items() {
        ListaSequencial<Par> saida = new ListaSequencial<>();
        for (int a = 0; a < tab.comprimento(); a++) {
            for (int b = 0; b < tab.obtem(a).comprimento(); b++) {
                saida.adiciona(tab.obtem(a).obtem(b));
            }
        }
        return saida;
    }

    public int comprimento() {
        return len;
    }

}
