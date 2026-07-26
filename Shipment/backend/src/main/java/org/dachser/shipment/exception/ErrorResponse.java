package org.dachser.shipment.exception;

import java.time.LocalDateTime;

/**
 * Standard error payload returned by the API whenever a request fails,
 * built by {@link GlobalExceptionHandler}.
 *
 * @param status    the HTTP status code
 * @param message   a human-readable description of the error
 * @param timestamp when the error occurred
 */
public record ErrorResponse(
        int status,
                             String message,
                             LocalDateTime timestamp
) {
    /**
     * Convenience factory that stamps the current time.
     *
     * @param status  the HTTP status code
     * @param message a human-readable description of the error
     * @return a new {@link ErrorResponse}
     */
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now());
    }
}
