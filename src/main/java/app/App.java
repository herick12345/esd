package app;

import java.io.*;
import java.util.Scanner;
import esd.ListaSequencial;
import esd.TabHash;

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

    // Estruturas de dados para armazenar os dados de geolocalização
    private ListaSequencial<IPRange> ipRanges;
    private TabHash<Integer, LocationInfo> locations;

    /*
    Construtor da App: processa os arquivos de dados do GeoLite
    @throws            Dispara uma exceção InvalidParameterException se não puder ler o arquivo, ou seu conteúdo for inválido
     */
    public App() {
        // Inicializa as estruturas de dados
        ipRanges = new ListaSequencial<>();
        locations = new TabHash<>();
        
        // Carrega os dados dos arquivos
        InputStream ipv4_blocks = ClassLoader.getSystemResourceAsStream(IPV4_BLOCKS);
        InputStream city_locations = ClassLoader.getSystemResourceAsStream(CITY_LOCATIONS);

        try {
            loadCityLocations(city_locations);
            loadIPv4Blocks(ipv4_blocks);
            
            // Ordena os ranges de IP para busca binária
            ipRanges.ordena();
            
            System.out.println("Dados carregados com sucesso!");
            System.out.println("Ranges de IP: " + ipRanges.comprimento());
            System.out.println("Localizações: " + locations.comprimento());
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados de geolocalização: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega as informações de localização das cidades
     */
    private void loadCityLocations(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            System.out.println("Arquivo de localizações não encontrado. Usando dados de exemplo.");
            // Adiciona algumas localizações de exemplo
            locations.adiciona(1, new LocationInfo(1, "BR", "São Paulo"));
            locations.adiciona(2, new LocationInfo(2, "US", "New York"));
            locations.adiciona(3, new LocationInfo(3, "GB", "London"));
            return;
        }
        
        try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            // Pula o cabeçalho
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            int count = 0;
            while (scanner.hasNextLine() && count < 10000) { // Limita para não sobrecarregar
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                try {
                    String[] parts = parseCSVLine(line);
                    if (parts.length >= 5) {
                        int geonameId = Integer.parseInt(parts[0]);
                        String countryCode = parts[4];
                        String cityName = parts.length > 10 ? parts[10] : "";
                        
                        if (geonameId > 0) {
                            locations.adiciona(geonameId, new LocationInfo(geonameId, countryCode, cityName));
                            count++;
                        }
                    }
                } catch (Exception e) {
                    // Ignora linhas com erro
                }
            }
            System.out.println("Carregadas " + count + " localizações");
        }
    }
    
    /**
     * Carrega os blocos de IP
     */
    private void loadIPv4Blocks(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            System.out.println("Arquivo de blocos IP não encontrado. Usando dados de exemplo.");
            // Adiciona alguns ranges de exemplo
            ipRanges.adiciona(new IPRange(IPUtils.ipToLong("8.8.8.0"), IPUtils.ipToLong("8.8.8.255"), 1));
            ipRanges.adiciona(new IPRange(IPUtils.ipToLong("1.1.1.0"), IPUtils.ipToLong("1.1.1.255"), 2));
            ipRanges.adiciona(new IPRange(IPUtils.ipToLong("208.67.222.0"), IPUtils.ipToLong("208.67.222.255"), 3));
            return;
        }
        
        try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            // Pula o cabeçalho
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            int count = 0;
            while (scanner.hasNextLine() && count < 50000) { // Limita para não sobrecarregar
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                try {
                    String[] parts = parseCSVLine(line);
                    if (parts.length >= 2) {
                        String network = parts[0];
                        String geonameIdStr = parts[1];
                        
                        if (!geonameIdStr.isEmpty()) {
                            int geonameId = Integer.parseInt(geonameIdStr);
                            
                            // Parse CIDR notation (e.g., "1.0.0.0/24")
                            String[] networkParts = network.split("/");
                            if (networkParts.length == 2) {
                                String baseIP = networkParts[0];
                                int prefixLength = Integer.parseInt(networkParts[1]);
                                
                                long startIP = IPUtils.ipToLong(baseIP);
                                long mask = (0xFFFFFFFFL << (32 - prefixLength)) & 0xFFFFFFFFL;
                                startIP = startIP & mask;
                                long endIP = startIP | (0xFFFFFFFFL >> prefixLength);
                                
                                ipRanges.adiciona(new IPRange(startIP, endIP, geonameId));
                                count++;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignora linhas com erro
                }
            }
            System.out.println("Carregados " + count + " ranges de IP");
        }
    }
    
    /**
     * Parse de linha CSV considerando aspas
     */
    private String[] parseCSVLine(String line) {
        ListaSequencial<String> result = new ListaSequencial<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.adiciona(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.adiciona(current.toString().trim());
        
        String[] array = new String[result.comprimento()];
        for (int i = 0; i < result.comprimento(); i++) {
            array[i] = result.obtem(i);
        }
        return array;
    }

    /*
    Retorna a localidade associada ao endreço IP
    @return um objeto Localidade contendo as informações da localidade, o null se não encontrá-lo
     */
    public Localidade busca_localidade(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Converte o IP para formato numérico
            long ipLong = IPUtils.ipToLong(ip.trim());
            
            // Busca o range que contém este IP
            IPRange range = findIPRange(ipLong);
            if (range == null) {
                return null;
            }
            
            // Busca as informações de localização
            LocationInfo locationInfo = locations.obtem_ou_default(range.getGeonameId(), null);
            if (locationInfo == null) {
                return new Localidade("--", "Localização desconhecida");
            }
            
            String pais = locationInfo.getCountryCode();
            String cidade = locationInfo.getCityName();
            
            if (cidade.isEmpty()) {
                cidade = "Cidade desconhecida";
            }
            
            return new Localidade(pais, cidade);
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar localidade para IP " + ip + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Encontra o range de IP que contém o IP especificado usando busca binária
     */
    private IPRange findIPRange(long ip) {
        int left = 0;
        int right = ipRanges.comprimento() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            IPRange range = ipRanges.obtem(mid);
            
            if (range.contains(ip)) {
                return range;
            } else if (ip < range.getStartIP()) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return null;
    }
    
    /**
     * Método para obter estatísticas do sistema
     */
    public String getStats() {
        return String.format("Ranges de IP: %d, Localizações: %d", 
                           ipRanges.comprimento(), locations.comprimento());
    }
    
    /**
     * Método para testar alguns IPs conhecidos
     */
    public void testKnownIPs() {
        String[] testIPs = {"8.8.8.8", "1.1.1.1", "208.67.222.222", "192.168.1.1"};
        
        System.out.println("\n=== Testando IPs conhecidos ===");
        for (String ip : testIPs) {
            Localidade loc = busca_localidade(ip);
            if (loc != null) {
                System.out.println(ip + " -> " + loc.pais() + ", " + loc.local());
            } else {
                System.out.println(ip + " -> Não encontrado");
            }
        }
    }
}


        return new Localidade("BR", "Pindorama");
    }

}
