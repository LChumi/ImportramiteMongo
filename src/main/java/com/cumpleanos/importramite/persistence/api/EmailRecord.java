package com.cumpleanos.importramite.persistence.api;

public record EmailRecord(String[] toUser, String subject, String message) {
}
