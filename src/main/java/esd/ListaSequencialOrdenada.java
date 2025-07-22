package esd;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class ListaSequencialOrdenada<T extends Comparable> {


    Comparable[] area;
    int len = 0;
    final int defcap = 8;

    public ListaSequencialOrdenada() {
        area = new Comparable[defcap];
    }

    void expande(int novaCapacidade) {
        Comparable<T>[] novaLista = Arrays.copyOf(area, novaCapacidade);
        area = novaLista;
    }

    public void expande() {
        expande(2 * area.length);
    }

    public boolean esta_vazia() {
        return len == 0;
    }

    public int capacidade() {
        return area.length;
    }

    public void remove(T valor) {
        if (esta_vazia()) {
            throw new IndexOutOfBoundsException("Lista Vazia.");
        }
        int indice = -1;
        for (int i = 0; i < len; i++) {
            if (area[i].compareTo(valor) == 0) {
                indice = i;
                break;
            }
        }
        if (indice == -1) {
            throw new IndexOutOfBoundsException("Valor inexistente na lista.");
        }
        for (int i = indice; i < len - 1; i++) {
            area[i] = area[i + 1];
        }
        area[len - 1] = null;
        len--;
    }

    public T obtem(int indice) {
        if (indice < 0 || indice >= len) {
            throw new IndexOutOfBoundsException("Índice inválido.");
        }
        return (T) area[indice];
    }

    public int comprimento() {
        return len;
    }

    public void limpa() {
        for (int i = 0; i < len; i++) {
            area[i] = null;
        }
        len = 0;
    }

    public void insere(int indice, T elemento) {
        if (indice < 0 || indice > len) {
            throw new IndexOutOfBoundsException("Índice inválido.");
        }
        if (len == capacidade()) {
            expande();
        }
        for (int i = len; i > indice; i--) {
            area[i] = area[i - 1];
        }
        area[indice] = elemento;
        len++;
    }

    public void insere(T elemento) {
        // insere o valor na lista, preservando seu ordenamento

        boolean inserido = false;
        for (int i = 0; i < len; i++) {
            if (elemento.compareTo(area[i]) <= 0) {
                insere(i, elemento);
                inserido = true;
                break;
            }
        }
        if (!inserido) {
            area[len++] = elemento;
        }
    }

    public int procura(T valor) {
        int indiceInicial = 0;
        int indiceFinal = len - 1;

        while (indiceInicial <= indiceFinal) {
            int indiceBusca = indiceInicial + (indiceFinal - indiceInicial) / 2;

            int comparacao = area[indiceBusca].compareTo(valor);

            if (comparacao < 0) {
                indiceInicial = indiceBusca + 1;
            } else if (comparacao > 0) {
                indiceFinal = indiceBusca - 1;
            } else {
                return indiceBusca;
            }
        }

        // Se não encontrar o valor, retorna -1
        return -1;
    }
}