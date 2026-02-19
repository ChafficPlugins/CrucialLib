package io.github.chafficui.CrucialLib.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrucialExceptionTest {

    @Test
    void errorCode1ReturnsInvalidServerVersion() {
        CrucialException ex = new CrucialException(1);
        assertEquals("Error 001: Invalid server version.", ex.getMessage());
    }

    @Test
    void errorCode2ReturnsCouldNotCreateCustomItem() {
        CrucialException ex = new CrucialException(2);
        assertEquals("Error 002: Could not create custom item.", ex.getMessage());
    }

    @Test
    void errorCode4ReturnsCreateMetrics() {
        CrucialException ex = new CrucialException(4);
        assertEquals("Error 004: Create a metrics before submitting stats.", ex.getMessage());
    }

    @Test
    void errorCode7ReturnsDuplicateItem() {
        CrucialException ex = new CrucialException(7);
        assertEquals("Error 007: A custom item with this id already exists!", ex.getMessage());
    }

    @Test
    void errorCode8ReturnsUpdaterFailed() {
        CrucialException ex = new CrucialException(8);
        assertEquals("Error 008: Updater tried to download the update, but was unsuccessful.", ex.getMessage());
    }

    @Test
    void errorCode9ReturnsUpdateCheckFailed() {
        CrucialException ex = new CrucialException(9);
        assertEquals("Error 009: Update check failed.", ex.getMessage());
    }

    @Test
    void errorCode10ReturnsOptionsSaveFailed() {
        CrucialException ex = new CrucialException(10);
        assertEquals("Error 010: Could not save options.yml", ex.getMessage());
    }

    @Test
    void errorCode11ReturnsNoOwnerSet() {
        CrucialException ex = new CrucialException(11);
        assertEquals("Error 011: No owner was set for crucialhead.", ex.getMessage());
    }

    @Test
    void errorCode28ReturnsLegacyDownloadFailed() {
        CrucialException ex = new CrucialException(28);
        assertEquals("Error 028: Failed to download legacy version.", ex.getMessage());
    }

    @Test
    void errorCode29ReturnsRegisterFailed() {
        CrucialException ex = new CrucialException(29);
        assertEquals("Error 029: Could not register custom item.", ex.getMessage());
    }

    @Test
    void unknownErrorCodeReturnsDefault() {
        CrucialException ex = new CrucialException(999);
        assertEquals("Error 999: An unknown error occurred.", ex.getMessage());
    }

    @Test
    void negativeErrorCodeReturnsDefault() {
        CrucialException ex = new CrucialException(-1);
        assertEquals("Error 999: An unknown error occurred.", ex.getMessage());
    }

    @Test
    void isException() {
        CrucialException ex = new CrucialException(1);
        assertInstanceOf(Exception.class, ex);
    }
}
