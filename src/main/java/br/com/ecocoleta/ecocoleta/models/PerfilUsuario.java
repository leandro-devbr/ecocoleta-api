package br.com.ecocoleta.ecocoleta.models;

public enum PerfilUsuario {
    ADMIN,
    USER;


    public String getDescricao() {
        switch (this) {
            case ADMIN:
                return "Administrador";
            case USER:
                return "Usuário";
            default:
                return "";
        }
    }
}
