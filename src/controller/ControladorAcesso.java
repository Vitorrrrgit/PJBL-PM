package controller;

import models.Usuario;

public class ControladorAcesso {
    private final Usuario usuarioLogado;

    public ControladorAcesso(Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("O usuário logado não pode ser nulo.");
        }
        this.usuarioLogado = usuarioLogado;
    }

    public boolean podeListarTodosUsuarios() {
        return usuarioLogado.podeGerenciarUsuarios();
    }

    public boolean podeListarTodasFrequencias() {
        return usuarioLogado.podeVerRelatoriosCompletos();
    }

    public boolean podeExportarDados() {
        return usuarioLogado.podeExportarDados();
    }

    public boolean podeRegistrarFrequencia() {
        return usuarioLogado.podeEditarFrequencia();
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}