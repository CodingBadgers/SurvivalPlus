package uk.codingbadgers.SurvivalPlus.module.loader;

/**
 * The priority that each module will be loaded with, {@code HIGHEST} priority
 * modules will be loaded first, going down to {@code LOWEST} being loaded last
 * <p />
 * Note - that dependencies take precedence over priority so if a module
 * depends on another this will be taken into account before its load priority
 * is used. The LoadPriority is only used as a fallback.
 */
public enum LoadPriority {

    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST;

}
