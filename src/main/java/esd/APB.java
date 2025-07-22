package esd;

public class APB<T extends Comparable<T>> {
    public class NodoAPB {
        private T valor;
        private NodoAPB esq = null;
        private NodoAPB dir = null;
        private NodoAPB pai = null;

        NodoAPB(T val, NodoAPB nodoPai) {
            valor = val;
            pai = nodoPai;
        }

    }

    public NodoAPB raiz = null;
    int len;

    public APB(T valor) {
        raiz = new NodoAPB(valor, null);
        len++;
    }

    public APB() {
    }

    // a seguir devem ser escritos os métodos da classe APB
    public void adiciona(T valor) {
        if (raiz == null) {
            raiz = new NodoAPB(valor, null);
        } else {
            adicionaPelaRaiz(raiz, valor);
        }
        len++;
    }

    private void adicionaPelaRaiz(NodoAPB nodoRaiz, T valor) {
        if (nodoRaiz.valor.compareTo(valor) > 0) {
            if (nodoRaiz.esq != null)
                adicionaPelaRaiz(nodoRaiz.esq, valor);
            else {
                nodoRaiz.esq = new NodoAPB(valor, nodoRaiz);
            }
        }
        if (nodoRaiz.valor.compareTo(valor) < 0) {
            if (nodoRaiz.dir != null)
                adicionaPelaRaiz(nodoRaiz.dir, valor);
            else {
                nodoRaiz.dir = new NodoAPB(valor, nodoRaiz);
            }
        }
    }

    public T procura(T valor) {
        if (procura_aux(valor, raiz))
            return valor;
        return null;
    }

    private boolean procura_aux(T valor, NodoAPB nodo) {
        if (nodo.valor.compareTo(valor) == 0)
            return true;
        if (nodo.valor.compareTo(valor) > 0 && nodo.esq != null)
            return procura_aux(valor, nodo.esq);
        if (nodo.valor.compareTo(valor) < 0 && nodo.dir != null)
            return procura_aux(valor, nodo.dir);
        return false;
    }

    public void remove(T valor) {
        NodoAPB nodoRemovido = obter_nodo(valor, raiz);
        ListaSequencial<T> menoresEsquerda = menores_que(valor);

        if (nodoRemovido.pai == null) {
            NodoAPB novaRaiz = new NodoAPB(nodoRemovido.esq.valor, null);
            if (nodoRemovido.dir != null) {
                nodoRemovido.dir.pai = novaRaiz;
                novaRaiz.dir = nodoRemovido.dir;
            }
            adiciona_lista(menoresEsquerda);
            return;
        }

        if (eh_esq(nodoRemovido)) {
            nodoRemovido.pai.esq = nodoRemovido.dir;
            adiciona_lista(menoresEsquerda);
        }
    }

    private void adiciona_lista(ListaSequencial<T> lista) {

        for (int a = 0; a < lista.comprimento(); a++) {
            adiciona(lista.obtem(a));
        }
    }

    public boolean eh_esq(NodoAPB nodo) {
        return nodo.valor.compareTo(nodo.pai.valor) < 0;
    }

    public T obtem(T valor) {
        NodoAPB nodo = obter_nodo(valor, raiz);
        return (nodo != null) ? nodo.valor : null;
    }

    // Visita recursivamente a subárvore esquerda
    // Visita o nó atual
    // Visita recursivamente a subárvore direita

    public ListaSequencial<T> emOrdem(NodoAPB raiz) {
        ListaSequencial<T> saida = new ListaSequencial<>();
        Preencher_in_order_aux(saida, raiz);
        return saida;
    }

    public ListaSequencial<T> emOrdem() {
        ListaSequencial<T> saida = new ListaSequencial<>();
        Preencher_in_order_aux(saida, raiz);
        return saida;
    }

    public void liste_in_order_aux(NodoAPB raiz) {
        if (raiz == null)
            return;
        liste_in_order_aux(raiz.esq);
        System.out.print(raiz.valor + " ");
        liste_in_order_aux(raiz.dir);
    }

    public void Preencher_in_order_aux(ListaSequencial<T> lista, NodoAPB raiz) {
        if (raiz == null)
            return;
        Preencher_in_order_aux(lista, raiz.esq);
        lista.adiciona(raiz.valor);
        Preencher_in_order_aux(lista, raiz.dir);
    }

    public void liste_in_order() {
        liste_in_order_aux(raiz);
    }

    public ListaSequencial<T> preOrdem(NodoAPB raiz) {
        ListaSequencial<T> saida = new ListaSequencial<>();
        preencher_pre_order_aux(saida, raiz);
        return saida;
    }

    public ListaSequencial<T> preOrdem() {
        ListaSequencial<T> saida = new ListaSequencial<>();
        preencher_pre_order_aux(saida, raiz);
        return saida;
    }

    private void liste_pre_order_Aux(NodoAPB nodo) {
        if (nodo == null)
            return;
        System.out.print(nodo.valor + " ");
        liste_pre_order_Aux(nodo.esq);
        liste_pre_order_Aux(nodo.dir);
    }

    public void preencher_pre_order_aux(ListaSequencial<T> lista, NodoAPB raiz) {
        if (raiz == null)
            return;
        lista.adiciona(raiz.valor);
        preencher_pre_order_aux(lista, raiz.esq);
        preencher_pre_order_aux(lista, raiz.dir);
    }

    public void liste_pre_order() {
        liste_pre_order_Aux(raiz);
    }

    public void preencher_post_order_aux(ListaSequencial<T> lista, NodoAPB raiz) {
        if (raiz == null)
            return;
        preencher_post_order_aux(lista, raiz.esq);
        preencher_post_order_aux(lista, raiz.dir);
        lista.adiciona(raiz.valor);

    }

    public ListaSequencial<T> posOrdem() {
        ListaSequencial<T> saida = new ListaSequencial<>();
        preencher_post_order_aux(saida, raiz);
        return saida;
    }

    public ListaSequencial<T> emLargura() {
        ListaSequencial<T> saida = new ListaSequencial<>();
        preencher_em_largura(saida, raiz);
        return saida;
    }

    public void liste_em_largura() {
        if (raiz == null)
            return;

        Fila<NodoAPB> fila = new Fila<>();
        fila.adiciona(raiz);

        while (!fila.estaVazia()) {
            NodoAPB atual = fila.remove();
            System.out.print(atual.valor + " ");

            if (atual.esq != null)
                fila.adiciona(atual.esq);
            if (atual.dir != null)
                fila.adiciona(atual.dir);
        }
    }

    public void preencher_em_largura(ListaSequencial<T> lista, NodoAPB raiz) {
        if (raiz == null)
            return;

        Fila<NodoAPB> fila = new Fila<>();
        fila.adiciona(raiz);

        while (!fila.estaVazia()) {
            NodoAPB atual = fila.remove();
            lista.adiciona(atual.valor);

            if (atual.esq != null)
                fila.adiciona(atual.esq);
            if (atual.dir != null)
                fila.adiciona(atual.dir);
        }
    }

    public void preenche_lista(ListaSequencial<T> lista, NodoAPB raiz) {
        if (raiz != null) {
            lista.adiciona(raiz.valor);
            if (raiz.dir != null)
                preenche_lista(lista, raiz.dir);
            if (raiz.esq != null)
                preenche_lista(lista, raiz.esq);
        }
    }

    public void preenche_lista(ListaSequencialOrdenada<T> lista, NodoAPB raiz) {

        if (raiz != null) {
            lista.insere(raiz.valor);
            if (raiz.dir != null)
                preenche_lista(lista, raiz.dir);
            if (raiz.esq != null)
                preenche_lista(lista, raiz.esq);
        }

    }

    public void preenche_lista(Lista<T> lista, NodoAPB raiz) {

        if (raiz != null) {
            lista.adiciona(raiz.valor);
            if (raiz.dir != null)
                preenche_lista(lista, raiz.dir);
            if (raiz.esq != null)
                preenche_lista(lista, raiz.esq);
        }

    }

    // menor da arvore
    public T menor() {
        NodoAPB atual = raiz;
        while (atual.esq != null) {
            atual = atual.esq;
        }
        return atual.valor;
    }

    // maior da arvore
    public T maior() {
        NodoAPB atual = raiz;
        while (atual.dir != null) {
            atual = atual.dir;
        }
        return atual.valor;
    }

    // primeiro menor da arvore menor que T?
    public T menor_que(T valor) {
        return menor_que_aux(valor, raiz);

    }

    public T menor_que(T valor, NodoAPB raiz) {
        return menor_que_aux(valor, raiz);

    }

    public T menor_que_aux(T valor, NodoAPB nodo) {
        if (nodo == null)
            return null;

        if (nodo.valor.compareTo(valor) > 0) {
            return menor_que_aux(valor, nodo.esq);
        } else {
            T candidato = menor_que_aux(valor, nodo.dir);
            return (candidato != null) ? candidato : nodo.valor;
        }
    }

    public T maior_que(T valor) {
        return maior_que_aux(valor, raiz);
    }

    public T maior_que(T valor, NodoAPB raiz) {
        return maior_que_aux(valor, raiz);
    }

    public T maior_que_aux(T valor, NodoAPB nodo) {
        if (nodo.valor.compareTo(valor) == 0)
            return valor;
        if (nodo.valor.compareTo(valor) > 0) {
            if (nodo.esq != null)
                return maior_que_aux(valor, nodo.esq);
            else
                return nodo.valor;
        }

        if (nodo.valor.compareTo(valor) < 0 && nodo.dir != null)
            return maior_que_aux(valor, nodo.dir);

        return null;
    }

    public ListaSequencial<T> menores_que(T valor) {
        ListaSequencial<T> saida = new ListaSequencial<>();
        menores_que_aux(valor, saida, raiz);
        return saida;

    }

    public void menores_que_aux(T valor, ListaSequencial<T> lista, NodoAPB nodo) {
        if (nodo == null)
            return;
        if (nodo.valor.compareTo(valor) <= 0) {
            lista.adiciona(nodo.valor);
            menores_que_aux(valor, lista, nodo.esq);
            menores_que_aux(valor, lista, nodo.dir);
        } else {
            menores_que_aux(valor, lista, nodo.esq);
            menores_que_aux(valor, lista, nodo.dir);}
    }

    // não é o método mais eficiente!
    // o ideal é achar o primeiro maior que e adicionar toda a subarvore à direita.
    // public Lista<T> maioresQue(T valor) {
    // T volante = valor;
    // Lista<T> saida = new Lista<>();
    // saida.adiciona(valor);

    // while (maior_que(valor) != null) {
    // volante = menor_que(volante);
    // saida.adiciona(volante);
    // }
    // return saida;
    // };

    // uma forma mais eficiente
    public ListaSequencial<T> maiores_que(T valor) {
        ListaSequencial<T> saida = new ListaSequencial<>();
        maiores_que_aux(valor, saida, raiz);

        return saida;

    }

        public void maiores_que_aux(T valor, ListaSequencial<T> lista, NodoAPB nodo) {
        if (nodo == null)
            return;
        if (nodo.valor.compareTo(valor) >= 0) {
            lista.adiciona(nodo.valor);
            maiores_que_aux(valor, lista, nodo.esq);
            maiores_que_aux(valor, lista, nodo.dir);
        } else {
            maiores_que_aux(valor, lista, nodo.esq);
            maiores_que_aux(valor, lista, nodo.dir);}
    }

    public NodoAPB obter_nodo(T valor, NodoAPB raiz) {
        if (valor == null || raiz == null)
            return null;

        NodoAPB saida = raiz;

        if (raiz.valor.compareTo(valor) == 0)
            return saida;

        else {
            if ((raiz.valor.compareTo(valor) > 0))
                return obter_nodo(valor, raiz.esq);
            else
                return obter_nodo(valor, raiz.dir);
        }
    }

    public int altura() {
        return alturaAux(raiz);
    }

    public int altura(APB<T> arvore) {
        return alturaAux(arvore.raiz);
    }

    public int altura(NodoAPB nodo) {
        return alturaAux(nodo);
    }

    private int alturaAux(NodoAPB nodo) {
        if (nodo == null)
            return 0;
        if (nodo.esq == null && nodo.dir == null) {
            return 1;
        } else {
            return 1 + Math.max(alturaAux(nodo.esq), alturaAux(nodo.dir));
        }
    }

    public int comprimento() {
        return len;
    }

    // algoritmo balanceia(arvore):
    // entrada: arvore (modificada ao final do algoritmo)
    // valor de retorno: raiz da árvore após balanceamento
    // variáveis: Fb (inteiro)

    // se arvore esquerda nao nula entao
    // arvore esquerda <- balanceia(arvore esquerda)

    // se arvore direita nao nula entao
    // arvore direita <- balanceia(arvore direita)

    // Fb <- calcula fator de balanceamento da arvore

    // enquanto Fb < -1 faça
    // reduz à direita e calcula novo Fb
    // fimEnquanto

    // enquanto Fb > 1 faça
    // reduz à esquerda e calcula novo Fb
    // fimEnquanto

    // retorna arvore

    // outra implementação do método comprimento pra ser usada no balanceamento da
    // arvore
    public int comprimento(NodoAPB raiz) {
        if (raiz == null)
            return 0;
        else {
            int saida = 1;
            // até dois ifs está de boas
            if (raiz.esq != null)
                saida += comprimento(raiz.esq);
            if (raiz.dir != null)
                saida += comprimento(raiz.dir);

            return saida;
        }
    }

    public T achar_mediana(NodoAPB raiz) {
        ListaSequencialOrdenada<T> lista = new ListaSequencialOrdenada<>();
        preenche_lista(lista, raiz);
        return lista.obtem(lista.comprimento() / 2);
    }

    public APB<T> balanceiaAux(NodoAPB raiz) {
        T mediana = achar_mediana(raiz);
        APB<T> saida = new APB(mediana);

        ListaSequencialOrdenada<T> lista = new ListaSequencialOrdenada<>();
        preenche_lista(lista, raiz);

        for (int a = 0; a < lista.comprimento(); a++) {
            if (lista.obtem(a) != mediana)
                saida.adiciona(lista.obtem(a));
        }
        return saida;

    }

    // não é a implementação mais eficiente de balanceia
    public void balanceia() {
        APB<T> novaAPB = balanceiaAux(raiz);
        this.raiz = novaAPB.raiz;

    }

    public void limpa() {
        limpaAux(raiz);
    }

    private void limpaAux(NodoAPB raiz) {
        if (raiz != null)
            raiz.valor = null;
        if (raiz.esq != null)
            limpaAux(raiz.esq);
        if (raiz.dir != null)
            limpaAux(raiz.dir);
    }

    public boolean esta_vazia() {
        return this.comprimento() == 0;
    }

    public int tamanho() {
        return comprimento();
    }

    public ListaSequencial<T> faixa(T min, T max) {
        ListaSequencial<T> todos = emOrdem();
        ListaSequencial<T> saida = new ListaSequencial<>();
        for (int i = 0; i < todos.comprimento(); i++) {
            T valor = todos.obtem(i);
            if (valor.compareTo(min) >= 0 && valor.compareTo(max) <= 0) {
                saida.adiciona(valor);
            }
        }
        return saida;
    }

    // public void imprimeAux(NodoAPB nodo) {
    // if (nodo != null) {
    // System.out.println(nodo.valor);
    // imprimeAux(nodo.esq);
    // imprimeAux(nodo.dir);
    // }
    // }

    // public void imprimeBreadth() {
    // Fila<Fila<NodoAPB>> linhas = new Fila<>();
    // Fila<NodoAPB> linhaAtual = new Fila();
    // linhaAtual.adiciona(raiz);

    // linhas.adiciona(linhaAtual);

    // if (testaTudoNullProximaLinha(linhaAtual) == false) {
    // linhaAtual = generateProximaLinhaLista(linhaAtual);
    // linhas.adiciona(linhaAtual);
    // }
    // linhaAtual = generateProximaLinhaLista(linhaAtual);
    // linhas.adiciona(linhaAtual);

    // int espacosEmBranco = quantiCaracteres(linhaAtual) / 2;

    // for (int a = 0; a < linhas.comprimento(); a++) {
    // imprimeLinha(linhas.get(a), espacosEmBranco / (a + 2));
    // System.out.println("\n");
    // }

    // }

    // public void imprimeLinha(Fila<NodoAPB> fila, int espacos) {
    // for (int a = 0; a < fila.comprimento() - 1; a++) {
    // addEspacosEmBranco(espacos);
    // System.out.print(fila.get(a).valor + ", ");
    // }
    // addEspacosEmBranco(espacos);
    // System.out.print(fila.get(fila.comprimento() - 1).valor);
    // }

    // public int quantiCaracteres(Fila<NodoAPB> fila) {
    // String concatenador = "";
    // for (int a = 0; a < fila.comprimento() - 1; a++) {
    // concatenador += fila.get(a).valor;
    // }
    // return concatenador.length() + 3 * fila.comprimento();
    // }

    // public void addEspacosEmBranco(int espacos) {
    // for (int a = 0; a < espacos; a++) {
    // System.out.print(" ");
    // }
    // }

    // public Fila<NodoAPB> generateProximaLinhaLista(Fila<NodoAPB> filaCima) {
    // Fila<NodoAPB> saida = new Fila<>();
    // for (int a = 0; a < filaCima.comprimento(); a++) {
    // saida.adiciona(filaCima.get(a).esq);
    // saida.adiciona(filaCima.get(a).dir);
    // }
    // return saida;
    // }

    // public boolean testaTudoNullProximaLinha(Fila<NodoAPB> fila) {
    // for (int a = 0; a < fila.comprimento(); a++) {
    // if (fila.get(a).esq.valor != null || fila.get(a).dir.valor != null)
    // return false;
    // }
    // return true;
    // }

    // private Fila<Fila<NodoAPB>> gerarListaLinhas() {
    // Fila<Fila<NodoAPB>> linhas = new Fila<>();
    // Fila<NodoAPB> linhaAtual = new Fila();
    // linhaAtual.adiciona(raiz);

    // linhas.adiciona(linhaAtual);

    // if (testaTudoNullProximaLinha(linhaAtual) == false) {
    // linhaAtual = generateProximaLinhaLista(linhaAtual);
    // linhas.adiciona(linhaAtual);
    // }
    // linhaAtual = generateProximaLinhaLista(linhaAtual);
    // linhas.adiciona(linhaAtual);
    // return linhas;
    // }

    // private Fila<Fila<Integer>> listaEspacamentos(Fila<Fila<NodoAPB>> linhas) {
    // Pilha<Fila<Integer>> inverteFila = new Pilha<>();
    // int a = linhas.comprimento() -1;

    // Fila<Integer> ultimaLinhaEspacos = new Fila<>();
    // Fila<NodoAPB> ultimaLinhaNodos = linhas.get(a);
    // aux.

    // }

}