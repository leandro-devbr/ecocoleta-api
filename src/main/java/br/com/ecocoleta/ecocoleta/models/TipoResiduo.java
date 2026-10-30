package br.com.ecocoleta.ecocoleta.models;

public enum TipoResiduo {
    PLASTICO,
    PAPEL,
    METAL,
    VIDRO,
    ORGANICO,
    ELETRONICO;

    public static TipoResiduo fromString(String tipo) {
        for (TipoResiduo tr : TipoResiduo.values()) {
            if (tr.name().equalsIgnoreCase(tipo)) {
                return tr;
            }
        }
        throw new IllegalArgumentException("Tipo de resíduo inválido: " + tipo);
    }
}
