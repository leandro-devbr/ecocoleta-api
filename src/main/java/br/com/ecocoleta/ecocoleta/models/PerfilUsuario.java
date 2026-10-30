package br.com.ecocoleta.ecocoleta.models;

public enum PerfilUsuario {
    ADMIN,
    USUARIO;


    public String getDescricao() {
        switch (this) {
            case ADMIN:
                return "Administrador";
            case USUARIO:
                return "Usuário";
            default:
                return "";
        }
    }
}
