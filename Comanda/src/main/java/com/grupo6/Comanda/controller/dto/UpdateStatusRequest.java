package com.grupo6.Comanda.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cuerpo para actualizar el estado de una reserva")
public class UpdateStatusRequest {

    @Schema(
        description = "Nuevo estado de la reserva",
        example = "confirmada",
        allowableValues = {"pendiente", "confirmada", "cancelada", "cancelada_cliente"},
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String estado;

    @Schema(
        description = "Motivo de cancelación (requerido cuando el estado es 'cancelada')",
        example = "El restaurante cerró por mantenimiento de emergencia"
    )
    private String motivoCancelacion;

    public UpdateStatusRequest() {}

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }
}