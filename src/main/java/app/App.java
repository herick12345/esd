package app;

import java.io.*;

/*
Para testar seu programa, execute-e e então acesse o seguinte link:
http://localhost:8080/
 */

/** Classe que representa a aplicação. O nome dela obrigatoriamente deve ser App
 * @author Marcelo M. Sobral
 * @version 1.0
 */
public class App {

    // os arquivos geolite com os blocos IPv4 e as desccrições das localidades
    final String IPV4_BLOCKS = "GeoLite2-City-Blocks-IPv4.csv";
    final String CITY_LOCATIONS = "GeoLite2-City-Locations-pt-BR.csv";

    // adicione aqui atributos necessários para esta aplicação
    // Se achar necessário, você pode criar novas classes ...

    /*
    Construtor da App: processa os arquivos de dados do GeoLite
    @throws            Dispara uma exceção InvalidParameterException se não puder ler o arquivo, ou seu conteúdo for inválido
     */
    public App() {
        // acessa os arquivos geolite ... que estão na pasta "resources"
        // você pode modificar a forma e o momento em que acessa esses arquivos ... eles são necessários
        // para ler as informações de geolocalização para dentro da estrutura de dados que você for usar
        InputStream ipv4_blocks = ClassLoader.getSystemResourceAsStream(IPV4_BLOCKS);
        InputStream city_locations = ClassLoader.getSystemResourceAsStream(CITY_LOCATIONS);

        // processa o arquivo para obter as configurações de geolocalização
    }

    /*
    Retorna a localidade associada ao endreço IP
    @return um objeto Localidade contendo as informações da localidade, o null se não encontrá-lo
     */
    public Localidade busca_localidade(String ip) {
        // implemente aqui a busca da localidade a partir do endereço IP

        return new Localidade("BR", "Pindorama");
    }

}
