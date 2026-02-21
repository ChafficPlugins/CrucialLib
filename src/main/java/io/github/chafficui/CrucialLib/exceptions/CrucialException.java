package io.github.chafficui.CrucialLib.exceptions;

/**
 * Exception thrown by CrucialLib when an operation fails.
 *
 * <p>Each instance carries a numeric error code that maps to a specific failure reason.
 * The supported error codes are:</p>
 * <ul>
 *   <li><b>001</b> -- Invalid server version</li>
 *   <li><b>002</b> -- Could not create custom item</li>
 *   <li><b>004</b> -- Create a metrics before submitting stats</li>
 *   <li><b>007</b> -- A custom item with this ID already exists</li>
 *   <li><b>008</b> -- Updater download failed</li>
 *   <li><b>009</b> -- Update check failed</li>
 *   <li><b>010</b> -- Could not save options.yml</li>
 *   <li><b>011</b> -- No owner was set for CrucialHead</li>
 *   <li><b>028</b> -- Failed to download legacy version</li>
 *   <li><b>029</b> -- Could not register custom item</li>
 *   <li><b>999</b> -- Unknown error</li>
 * </ul>
 */
public class CrucialException extends Exception{

    /**
     * Constructs a new {@code CrucialException} with a message derived from the given error code.
     *
     * @param errorCode the numeric error code identifying the failure reason
     */
    public CrucialException(int errorCode){
        super(pickErrorMessage(errorCode));
    }

    private static String pickErrorMessage(int errorCode){
        String errorMessage;
        switch (errorCode){
            case 1:
                errorMessage = "Error 001: Invalid server version.";
                break;
            case 2:
                errorMessage = "Error 002: Could not create custom item.";
                break;
            case 4:
                errorMessage = "Error 004: Create a metrics before submitting stats.";
                break;
            case 7:
                errorMessage = "Error 007: A custom item with this id already exists!";
                break;
            case 8:
                errorMessage = "Error 008: Updater tried to download the update, but was unsuccessful.";
                break;
            case 9:
                errorMessage = "Error 009: Update check failed.";
                break;
            case 10:
                errorMessage = "Error 010: Could not save options.yml";
                break;
            case 11:
                errorMessage = "Error 011: No owner was set for crucialhead.";
                break;
            case 28:
                errorMessage = "Error 028: Failed to download legacy version.";
                break;
            case 29:
                errorMessage = "Error 029: Could not register custom item.";
                break;
            default:
                errorMessage = "Error 999: An unknown error occurred.";
                break;
        }
        return errorMessage;
    }
}
