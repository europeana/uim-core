/* EngineStatus.java - created on May 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

/**
 * Status of the engine (starting, etc.)
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public enum EngineStatus {
    REGISTERED,
    BOOTING,
    RUNNING,
    STOPPED,
    FAILURE
}
