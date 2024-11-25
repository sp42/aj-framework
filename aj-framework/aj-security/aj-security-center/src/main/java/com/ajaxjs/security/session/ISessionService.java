package com.ajaxjs.security.session;

/**
 * Session Service
 */
public interface ISessionService {
    /**
     * Get value from session.
     *
     * @param key The key
     * @return Value
     */
    String get(String key);

    /**
     * Set value to session
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * Delete a session by the key.
     *
     * @param key
     */
    void delete(String key);
}
