package uk.codingbadgers.SurvivalPlus.module.loader;

import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.module.loader.exception.LoadException;

import java.io.File;
import java.util.List;

/**
 * A module loader takes a list of directories and loads Modules to execute
 * custom code into the server.
 */
public interface ModuleLoader {

    /**
     * Add a directory for modules to be loaded from.
     * <p />
     * <b>Note - this is not to be used by modules as it will have no effect
     * after the modules have started to be loaded. It is for internal use
     * only.</b>
     *
     * @param file
     * @throws IllegalStateException if called after modules have started to
     *          be loaded
     */
    public void addModuleDirectory(File file);

    /**
     * Gets all the directories that modules can be loaded from.
     *
     * @return a list of all the directories modules can be loaded from
     */
    public List<File> getModuleDirs();

    /**
     * Gets the current {@link LoadState} this module loader is in
     *
     * @return the current {@link LoadState} of the loader
     */
    public LoadState getLoadState();

    /**
     * Loads all modules from all directories specified in
     * {@link #getModuleDirs()}
     *
     * @throws LoadException if there is a severe unexpected error whilst
     *          loading the modules.
     */
    public void load() throws LoadException;

    /**
     * Loads a module from the file specified.
     *
     * @param file the jar file to load the module from
     */
    public void load(File file);

    /**
     * Unloads all modules safely from memory.
     */
    public void unload();

    /**
     * Unloads a specific module safely from memory.
     *
     * @param module the module to unload from memory
     */
    public void unload(Module module);

    /**
     * Get the specific module instance for a given module name.
     *
     * @param name the module name to lookup
     * @return the module instance requested
     */
    public Module getModule(String name);

    /**
     * Get a list of all currently loaded modules in this loader.
     *
     * @return a list of all currently loaded modules in this loader
     */
    public List<Module> getModules();

}
