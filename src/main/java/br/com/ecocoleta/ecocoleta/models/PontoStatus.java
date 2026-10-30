package br.com.ecocoleta.ecocoleta.models;

public enum PontoStatus {
    NORMAL,
    CHEIO;

    public static PontoStatus fromString(String status) {
        for (PontoStatus ps : PontoStatus.values()) {
            if (ps.name().equalsIgnoreCase(status)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("Status de ponto inválido: " + status);
    }
}
