package com.grupo6.Comanda.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cuerpo para rechazar una solicitud de restaurante")
public class RejectRequestBody {

    @Schema(
        description = "Motivo del rechazo (opcional, se envía por email al solicitante)",
        example = "La documentación presentada está incompleta"
    )
    private String motivo;

    public RejectRequestBody() {}

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}