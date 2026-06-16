package com.cumpleanos.importramite.persistence.api;

public record MedianetRequesDTO(
        double subtotal,
        double subtotal0,
        double iva,
        double total,
        String tipoTransaccion,
        String codigoDiferido,
        String plazo,
        String mid,
        String tid,
        String cid,
        String pVenta,
        String hora,
        //Campos anulacion
        String referencia,
        String lote,
        String numeroAutorizacion
) {
}
