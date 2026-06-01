package com.grupo6.Comanda.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Campos que el usuario puede actualizar en su perfil")
    public class UpdateMeRequestDto {

        @Schema(description = "Nuevo nombre del usuario (opcional)", example = "Diego García")
        public String nombre;

        @Schema(description = "URL del nuevo avatar (opcional)", example = "https://storage.example.com/avatars/diego.jpg")
        public String avatar;

        @Schema(description = "Nuevo teléfono (exactamente 9 dígitos, opcional)", example = "987654321")
        public Long telefono;

        public UpdateMeRequestDto() {}
        public String getNombre()   { return nombre; }
        public void   setNombre(String nombre)     { this.nombre = nombre; }
        public String getAvatar()   { return avatar; }
        public void   setAvatar(String avatar)     { this.avatar = avatar; }
        public Long   getTelefono() { return telefono; }
        public void   setTelefono(Long telefono)   { this.telefono = telefono; }
    }