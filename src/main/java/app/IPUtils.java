package app;

/**
 * Utilitários para conversão e manipulação de endereços IP
 */
public class IPUtils {
    
    /**
     * Converte um endereço IP em formato string para long
     * @param ip Endereço IP no formato "192.168.1.1"
     * @return Representação numérica do IP
     */
    public static long ipToLong(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP não pode ser nulo ou vazio");
        }
        
        String[] parts = ip.trim().split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Formato de IP inválido: " + ip);
        }
        
        long result = 0;
        for (int i = 0; i < 4; i++) {
            try {
                int octet = Integer.parseInt(parts[i]);
                if (octet < 0 || octet > 255) {
                    throw new IllegalArgumentException("Octeto inválido no IP: " + octet);
                }
                result = (result << 8) + octet;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Formato de IP inválido: " + ip);
            }
        }
        
        return result;
    }
    
    /**
     * Converte um long para endereço IP em formato string
     * @param ip Representação numérica do IP
     * @return Endereço IP no formato "192.168.1.1"
     */
    public static String longToIp(long ip) {
        return String.format("%d.%d.%d.%d",
            (ip >> 24) & 0xFF,
            (ip >> 16) & 0xFF,
            (ip >> 8) & 0xFF,
            ip & 0xFF);
    }
    
    /**
     * Valida se uma string representa um endereço IP válido
     * @param ip String a ser validada
     * @return true se for um IP válido
     */
    public static boolean isValidIP(String ip) {
        try {
            ipToLong(ip);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}