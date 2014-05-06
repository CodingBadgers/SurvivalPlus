package uk.codingbadgers.SurvivalPlus.module.loader;

import uk.codingbadgers.SurvivalPlus.module.Module;

/**
 * Created by James on 05/05/2014.
 */
public enum LoadState {

    PRE_SETUP,

    /**
     * Setup phase
     * <p />
     * Loads all modules and their required information from disk
     */
    SETUP,

    /**
     * Load phase
     * <p />
     * Loads the modules main class into memory and calls the
     * {@link Module#onLoad()} method. As well as this, help pages for the
     * module are created and added to the bukkit help page.
     */
    LOAD,

    /**
     * Enable phase
     * <p />
     * Calls {@link Module#onEnable()} on all modules.
     */
    ENABLE,

    /**
     * Setup phase
     * <p />
     * Calls {@link Module#onPostEnable()} on all modules.
     */
    POST_ENABLE,

    /**
     * Loaded phase
     * <p />
     * Signifies that the module loader has finished loading all modules from
     * the specified directories
     */
    LOADED;


    public boolean after(LoadState state) {
        return this.ordinal() > state.ordinal();
    }
}
