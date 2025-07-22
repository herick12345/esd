package app;

import esd.ListaSequencial;
import esd.TabHash;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class App {
    
    // Estruturas de dados para armazenar os dados do GeoLite
    private ListaSequencial<IPRange> ipRanges;
    private TabHash<Integer, Localidade> locations;
    private boolean dadosCarregados = false;
    
    public App() {
        ipRanges = new ListaSequencial<>();
        locations = new TabHash<>();
        
        System.out.println("Inicializando App...");
        
        // Tenta carregar os dados reais primeiro
        if (!carregarDadosReais()) {
            System.out.println("Arquivos GeoLite não encontrados. Carregando dados de exemplo...");
            carregarDadosExemplo();
        }
        
        System.out.println("App inicializado com " + ipRanges.comprimento() + " ranges de IP e " + 
                         locations.comprimento() + " localizações.");
    }
    
    /**
     * Tenta carregar os dados reais dos arquivos GeoLite
     */
    private boolean carregarDadosReais() {
        try {
            // Primeiro carrega as localizações
            InputStream locStream = getClass().getResourceAsStream("/GeoLite2-City-Locations-pt-BR.csv");
            if (locStream == null) {
                return false;
            }
            
            BufferedReader locReader = new BufferedReader(new InputStreamReader(locStream, StandardCharsets.UTF_8));
            String linha;
            boolean primeiraLinha = true;
            
            while ((linha = locReader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Pula o cabeçalho
                }
                
                String[] campos = linha.split(",");
                if (campos.length >= 5) {
                    try {
                        int geonameId = Integer.parseInt(campos[0]);
                        String countryCode = campos[4].replace("\"", "");
                        String cityName = campos[10].replace("\"", ""); // Nome da cidade em português
                        
                        Localidade localidade = new Localidade(countryCode, cityName);
                        locations.adiciona(geonameId, localidade);
                    } catch (NumberFormatException e) {
                        // Ignora linhas com formato inválido
                    }
                }
            }
            locReader.close();
            
            // Depois carrega os blocos de IP
            InputStream blockStream = getClass().getResourceAsStream("/GeoLite2-City-Blocks-IPv4.csv");
            if (blockStream == null) {
                return false;
            }
            
            BufferedReader blockReader = new BufferedReader(new InputStreamReader(blockStream, StandardCharsets.UTF_8));
            primeiraLinha = true;
            
            while ((linha = blockReader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Pula o cabeçalho
                }
                
                String[] campos = linha.split(",");
                if (campos.length >= 2) {
                    try {
                        String network = campos[0];
                        int geonameId = Integer.parseInt(campos[1]);
                        
                        // Converte CIDR para range de IPs
                        String[] parts = network.split("/");
                        if (parts.length == 2) {
                            long baseIP = IPUtils.ipToLong(parts[0]);
                            int prefixLength = Integer.parseInt(parts[1]);
                            long mask = (0xFFFFFFFFL << (32 - prefixLength)) & 0xFFFFFFFFL;
                            long startIP = baseIP & mask;
                            long endIP = startIP | (0xFFFFFFFFL >> prefixLength);
                            
                            IPRange range = new IPRange(startIP, endIP, geonameId);
                            ipRanges.adiciona(range);
                        }
                    } catch (Exception e) {
                        // Ignora linhas com formato inválido
                    }
                }
            }
            blockReader.close();
            
            // Ordena os ranges por IP inicial para busca eficiente
            ipRanges.ordena();
            dadosCarregados = true;
            
            System.out.println("Dados reais carregados com sucesso!");
            return true;
            
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados reais: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Carrega dados de exemplo para demonstração
     */
    private void carregarDadosExemplo() {
        // Adiciona algumas localizações de exemplo
        locations.adiciona(1, new Localidade("US", "Mountain View")); // Google
        locations.adiciona(2, new Localidade("US", "San Francisco")); // Cloudflare
        locations.adiciona(3, new Localidade("US", "San Francisco")); // OpenDNS
        locations.adiciona(4, new Localidade("US", "New York")); // Quad9
        locations.adiciona(5, new Localidade("BR", "São Paulo"));
        locations.adiciona(6, new Localidade("BR", "Rio de Janeiro"));
        locations.adiciona(7, new Localidade("DE", "Frankfurt"));
        locations.adiciona(8, new Localidade("JP", "Tokyo"));
        locations.adiciona(9, new Localidade("GB", "London"));
        locations.adiciona(10, new Localidade("FR", "Paris"));
        
        // Adiciona ranges de IP de exemplo
        // Google DNS (8.8.8.8)
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("8.8.8.0"), IPUtils.ipToLong("8.8.8.255"), 1));
        
        // Cloudflare (1.1.1.1)
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("1.1.1.0"), IPUtils.ipToLong("1.1.1.255"), 2));
        
        // OpenDNS (208.67.222.222)
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("208.67.222.0"), IPUtils.ipToLong("208.67.222.255"), 3));
        
        // Quad9 (9.9.9.9)
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("9.9.9.0"), IPUtils.ipToLong("9.9.9.255"), 4));
        
        // Alguns ranges brasileiros fictícios
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("200.160.0.0"), IPUtils.ipToLong("200.160.255.255"), 5));
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("201.20.0.0"), IPUtils.ipToLong("201.20.255.255"), 6));
        
        // Ranges internacionais fictícios
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("85.114.0.0"), IPUtils.ipToLong("85.114.255.255"), 7));
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("210.188.0.0"), IPUtils.ipToLong("210.188.255.255"), 8));
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("81.2.0.0"), IPUtils.ipToLong("81.2.255.255"), 9));
        ipRanges.adiciona(new IPRange(IPUtils.ipToLong("82.64.0.0"), IPUtils.ipToLong("82.64.255.255"), 10));
        
        // Ordena os ranges para busca eficiente
        ipRanges.ordena();
        dadosCarregados = true;
        
        System.out.println("Dados de exemplo carregados!");
    }
    
    /**
     * Busca a localidade geográfica de um endereço IP
     * @param ip Endereço IP em formato string (ex: "192.168.1.1")
     * @return Localidade encontrada ou null se não encontrada
     */
    public Localidade busca_localidade(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            System.out.println("IP inválido: " + ip);
            return null;
        }
        
        try {
            // Converte o IP para formato numérico
            long ipNumerico = IPUtils.ipToLong(ip.trim());
            System.out.println("Buscando localização para IP: " + ip + " (" + ipNumerico + ")");
            
            // Busca o range que contém este IP
            IPRange rangeEncontrado = null;
            for (int i = 0; i < ipRanges.comprimento(); i++) {
                IPRange range = ipRanges.obtem(i);
                if (range.contains(ipNumerico)) {
                    rangeEncontrado = range;
                    break;
                }
            }
            
            if (rangeEncontrado == null) {
                System.out.println("Nenhum range encontrado para o IP: " + ip);
                return null;
            }
            
            // Busca a localização correspondente
            Localidade localidade = locations.obtem_ou_default(rangeEncontrado.getGeonameId(), null);
            
            if (localidade != null) {
                System.out.println("Localização encontrada: " + localidade.pais() + ", " + localidade.local());
            } else {
                System.out.println("Localização não encontrada para geoname_id: " + rangeEncontrado.getGeonameId());
            }
            
            return localidade;
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar localização para IP " + ip + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Retorna estatísticas sobre os dados carregados
     */
    public String getStats() {
        return String.format("Dados carregados: %s\nRanges de IP: %d\nLocalizações: %d", 
                           dadosCarregados ? "Sim" : "Não",
                           ipRanges.comprimento(),
                           locations.comprimento());
    }
}