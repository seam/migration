package org.open18;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(Long id) {
        super(id == null ? "No profile was requested" : "The requested profile does not exist: " + id);
    }
}
