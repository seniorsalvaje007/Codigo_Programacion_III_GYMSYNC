package com.gymsync.service;

import com.gymsync.model.Usuario;
import com.gymsync.model.Rol;
import com.gymsync.model.Atleta;
import com.gymsync.repository.AtletaRepositorio;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthService {

    private final Map<String, Usuario> tablaCredenciales;
    private final AtletaRepositorio atletaRepositorio;
    private Usuario usuarioAutenticado;

    public AuthService (AtletaRepositorio atletaRepositorio) {
        this.tablaCredenciales = new HashMap<>();
        this.atletaRepositorio = atletaRepositorio;
        this.usuarioAutenticado = null;
    }

    public void registrarUsuario (String correo, String contraseña, Rol rol) {
        Usuario nuevo = new Usuario (correo, contraseña, rol);
        tablaCredenciales.put(correo, nuevo);
    }

    public boolean iniciarSesion (String correo, String contraseña) {
        Usuario usuario = tablaCredenciales.get(correo);

        if (usuario == null) {
            System.out.println("No existe ninguna cuenta registrada con ese correo electronico");
            return false;
        }

        if (usuario.getContraseña().equals(contraseña)) {
            this.usuarioAutenticado = usuario;
            System.out.println("Sesion iniciada con " + correo + "[" + usuario.getRol() + "]\n");
            return true;
        }
        System.out.println("Contraseña incorrecta");
        return false;
    }

    public Optional<Atleta> obtenerAtletaLogueado () {
        if (usuarioAutenticado != null || usuarioAutenticado.getRol() == Rol.ATLETA) {return atletaRepositorio.buscarPorCorreo(usuarioAutenticado.getCorreo());}
        return Optional.empty();
    }

    public boolean tienePermiso (Rol rolRequerido) {
        if (usuarioAutenticado == null) {
            System.out.println("ACCESO DENEGADO; Por favor inicie sesion");
            return false;
        }
        return usuarioAutenticado.getRol() == rolRequerido;
    }

    public void cerrarSesion() {
        this.usuarioAutenticado = null;
    }

}
