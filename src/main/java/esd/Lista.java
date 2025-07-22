package esd;

import java.util.Random;

public class Lista<T extends Comparable<T>> {
    class Node {
        T valor = null;
        Node proximo;
        Node antecessor;

        Node() {
            this.antecessor = this;
            this.proximo = this;
        }

        Node(T valor) {
            // inicializa o nodo, que deve inicialmente ser seu próprio sucessor e
            // antecessor
            this.valor = valor;
            this.antecessor = this;
            this.proximo = this;
        }

        void conecta(Node sucessor) {
            // insere este nodo antes de sucessor
            antecessor = sucessor.antecessor;
            proximo = sucessor;
            sucessor.antecessor = this;
            antecessor.proximo = this;
        }

        void desconecta() {
            // desconecta este nodo, desfazendo as referências de seu antecessor e sucessor
            antecessor.proximo = proximo;
            proximo.antecessor = antecessor;
            proximo = null;
            antecessor = null;
        }
    }

    Node guarda;
    int len;

    // operações de Lista
    public Lista() {
        guarda = new Node();
        len = 0;
    }

    Node obterNodo(int indice) {
        if (indice < 0 || indice >= len)
            throw new IndexOutOfBoundsException("Indíce inválido");

        Node atual = guarda;
        for (int a = 0; a <= indice; a++) {
            atual = atual.proximo;
        }
        return atual;
    }

    // insere valor na posição dada por "indice"
    // se "indice" > comprimento da lista, dispara exceção
    // IndexOutOfBoundException
    public void insere(int indice, T valor) {
        if (indice < 0 || indice > len)
            throw new IndexOutOfBoundsException("Índice fora dos limites");

        Node criado = new Node(valor);

        if (len == 0) {
            criado.proximo = guarda;
            criado.antecessor = guarda;
            guarda.proximo = criado;
            guarda.antecessor = criado;
            len++;
            return;
        }

        criado.conecta(obterNodo(indice));
        len++;

    }

    // adiciona no fim
    public void adiciona(T valor) {
        Node novo = new Node(valor);
        novo.conecta(guarda);
        len++;
    }

    // obtém o valor que está na posição dada por "indice"
    // se "indice" >= comprimento da lista, dispara exceção
    // IndexOutOfBoundException
    public T obtem(int indice) {
        return obterNodo(indice).valor;
    }

    public int procura(T valor) {
        Node atual = guarda;
        int indice = 0;
        while (atual.proximo.valor != null) {
            atual = atual.proximo;
            if (atual.valor == valor) {
                return indice;
            }
            indice++;
        }
        return -1;
    }

    public T obtem_primeiro() {
        return obtem(0);

    }

    public T obtem_ultimo() {
        return obtem(len - 1);
    }

    public boolean esta_vazia() {
        return len == 0;
    }

    public int comprimento() {
        return len;
    }

    public void remove(int indice) {
        obterNodo(indice).desconecta();
        len--;
    }

    public void remove_ultimo() {
        remove(len - 1);

    }

    // MÉTODOS DA PROVA

    public void inverte() {
        // algoritmo para inverter
        Node atual = guarda;
        Node proximoInverte = atual.antecessor;
        Node tempFrente;
        Node tempTraz;
        while (proximoInverte.valor != null) {
            tempFrente = atual.proximo;
            tempTraz = atual.antecessor;
            atual.antecessor = tempFrente;
            atual.proximo = tempTraz;
            atual = proximoInverte;
            proximoInverte = atual.antecessor;
        }
    }

    public void embaralha() {
        // instruções do algoritmo
        // embaralha o vetor v, sendo n o comprimento desse vetor
        // para i de n-1 até 1 faça
        // j <- número inteiro aleatório tal que 0 <= j <= i-1
        // faz a permuta de v[j] e v[i]

        Random gerador = new Random();
        int indiceAleatorio;
        Node referencia;
        Node permutar = guarda.proximo;
        Node atual = guarda.proximo;

        for (int i = len - 1; i >= 1; i--) {

            indiceAleatorio = gerador.nextInt(0, i);
            for (int j = 0; j <= i - 1; j++) {
                atual = atual.proximo;
                if (j == indiceAleatorio) {
                    permutar = atual;
                }
            }
            referencia = atual.proximo;

            T temp = permutar.valor;
            permutar.valor = referencia.valor;
            referencia.valor = temp;

            atual = guarda;
        }

    }

    public void substitui(int indice, T valor) {
        obterNodo(indice).valor = valor;
    }

    public void ordena() {
        Lista<T> listaOrdenada = mergeSort(this, 0, len - 1);

        Node atual = listaOrdenada.guarda.proximo;
        int indice = 0;

        while (atual != listaOrdenada.guarda) {
            this.obterNodo(indice).valor = atual.valor;
            atual = atual.proximo;
            indice++;
        }
    }


    public Lista<T> mergeSort(Lista<T> lista, int pos1, int pos2) {
        if (pos2 == pos1 + 1) {
            Node prim = lista.obterNodo(pos1);
            Node seg = lista.obterNodo(pos2);
            Lista<T> saida = new Lista<>();
            if (prim.valor.compareTo(seg.valor) > 0) {
                saida.adiciona(seg.valor);
                saida.adiciona(prim.valor);
            } else {
                saida.adiciona(prim.valor);
                saida.adiciona(seg.valor);
            }
            return saida;
        }

        if (pos2 == pos1) {
            T unico = lista.obterNodo(pos2).valor;
            Lista<T> saida = new Lista<>();
            saida.adiciona(unico);
            return saida;
        }

        if (pos2 < pos1)
            return null;

        if (pos2 > pos1 + 1) {
            int meio = (pos1 + pos2) / 2; // Corrigido aqui
            Lista<T> esquerda = mergeSort(lista, pos1, meio);
            Lista<T> direita = mergeSort(lista, meio + 1, pos2);
            return mergeMS(esquerda, direita);
        }

        return lista;
    }

    public Lista<T> mergeMS(Lista<T> prim, Lista<T> seg) {
        Lista<T> saida = new Lista<>();
        Node pontPrim = prim.guarda.proximo;
        Node pontSeg = seg.guarda.proximo;

        while (pontPrim != prim.guarda && pontSeg != seg.guarda) {
            if (pontPrim.valor.compareTo(pontSeg.valor) <= 0) {
                saida.adiciona(pontPrim.valor);
                pontPrim = pontPrim.proximo;
            } else {
                saida.adiciona(pontSeg.valor);
                pontSeg = pontSeg.proximo;
            }
        }

        // Adiciona os elementos restantes
        while (pontPrim != prim.guarda) {
            saida.adiciona(pontPrim.valor);
            pontPrim = pontPrim.proximo;
        }

        while (pontSeg != seg.guarda) {
            saida.adiciona(pontSeg.valor);
            pontSeg = pontSeg.proximo;
        }

        return saida;
    }



}
