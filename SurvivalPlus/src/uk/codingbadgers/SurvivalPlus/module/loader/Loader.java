/**
 * SurvivalPlus 1.0
 * Copyright (C) 2014 CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.SurvivalPlus.module.loader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import uk.codingbadgers.SurvivalPlus.error.ExceptionHandler;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.module.ModuleLoadEvent;

/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Loader - Loader base for loading Loadables.
 * 
 * @author NodinChan
 */
public class Loader {

	private final Plugin plugin;
	private final List<File> files;
	private final List<Module> loadables;
	private ClassLoader loader;

	public enum LoadResult {
		Success,
		Fail,
		WaitingForDepends,
		AlreadyLoaded
	}
	
	/**
	 * 
	 * @return 
	 */
	public List<Module> getLoadables() {
		return this.loadables;
	}
	
	/**
	 * Instantiates a new loader.
	 * 
	 * @param plugin
	 *            the plugin
	 * @param dir
	 *            the directory to load modules from
	 */
	public Loader(Plugin plugin, File[] dirs) {
		this.plugin = plugin;
		this.files = new ArrayList<File>();
		
		FileExtensionFilter filter = new FileExtensionFilter(".jar");
		for (File dir : dirs)
		{
			this.files.addAll(Arrays.asList(dir.listFiles(filter)));
		}
		this.loadables = new ArrayList<Module>();

		generateClassLoader();
	}

	private void generateClassLoader() {
		this.loadables.clear();

		List<URL> urls = new ArrayList<URL>();

		for (File file : files) {
			try {
				urls.add(file.toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	
		this.loader = URLClassLoader.newInstance(urls.toArray(new URL[0]), plugin.getClass().getClassLoader());
	}

	/**
	 * Gets the Logger.
	 * 
	 * @return The Logger
	 */
	public Logger getLogger() {
		return plugin.getLogger();
	}

	/**
	 * Loads the all loadables in the directory specified.
	 * 
	 * @return List of loaded loadables
	 */
	public final List<Module> load() {
		return this.load(this.files);
	}
	
	public final List<Module> load(List<File> filesToLoad) {
		
		int noofToLoad = filesToLoad.size();
		
		List<File> waitingForDepends = new ArrayList<File>();
		for (File file : filesToLoad) {
			LoadResult result = load(file);
			if (result == LoadResult.WaitingForDepends) {
				getLogger().log(Level.INFO, "The loadable " + file.getName() + " is waiting for dependancies!");
				waitingForDepends.add(file);
			}
		}
		
		if (!waitingForDepends.isEmpty()) {
			if (waitingForDepends.size() != noofToLoad) {
				return load(waitingForDepends);
			}	
		}
				
		return loadables;
	}

	/**
	 * Load file into the loader.
	 * 
	 * @param file
	 *            the file
	 * @return the list
	 * @TODO support module dependencies
	 */
	public LoadResult load(File file) {
		try {
			JarFile jarFile = new JarFile(file);
			String mainClass = null;
			LoadableDescriptionFile ldf = null;
			Collection<String> depends = null;
			
			if (jarFile.getEntry("path.yml") != null) {
				JarEntry element = jarFile.getJarEntry("path.yml");
				ldf = new LoadableDescriptionFile(jarFile.getInputStream(element));
				mainClass = ldf.getMainClass();
				depends = ldf.getDependencies();
			}

			if (depends != null && !depends.isEmpty()) {
				for (String depend : depends) {
					boolean dependLoaded = false;
					for (Module loadable : loadables) {
						if (loadable.getName().equalsIgnoreCase(depend)) {
							dependLoaded = true;
							getLogger().log(Level.INFO, "The loadable " + file.getName() + " found dependancy '" + depend + "'");
							break;
						}
					}			
					if (dependLoaded == false) {
						return LoadResult.WaitingForDepends;
					}
				}			
			}
			
			if (mainClass != null) {				
				Class<?> clazz = Class.forName(mainClass, true, loader);

				if (clazz != null) {
					Class<? extends Module> loadableClass = clazz.asSubclass(Module.class);
					Constructor<? extends Module> constructor = loadableClass.getConstructor();
					Module loadable = constructor.newInstance();

					if (loadables.contains(loadable)) {
						getLogger().log(Level.WARNING, "The loadable " + file.getName() + " is already loaded, make sure to disable the module first");
						getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load");
						jarFile.close();
						return LoadResult.AlreadyLoaded;
					}

					loadable.setFile(file);
					loadable.setDesciption(ldf);
					loadable.setJarFile(jarFile);
					loadable.setDatafolder(new File(file.getParentFile(), loadable.getName()));
					loadable.init();

					loadables.add(loadable);

					ModuleLoadEvent event = new ModuleLoadEvent(plugin, loadable, jarFile);
					plugin.getServer().getPluginManager().callEvent(event);
					
					return LoadResult.Success;

				} else {
					jarFile.close();
					throw new ClassNotFoundException("Class " + mainClass + " could not be found.");
				}

			} else {
				jarFile.close();
				throw new ClassNotFoundException("Could not find main class in the path.yml.");
			}

		} catch (ClassCastException e) {
			e.printStackTrace();
			getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " is in the wrong directory.");
			getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load.");
		} catch (ClassNotFoundException e) {
            getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load.");
			getLogger().log(Level.WARNING, "Invalid path.yml.");
			getLogger().log(Level.WARNING, e.getMessage());
		} catch (NoClassDefFoundError e) {
            getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load.");
            getLogger().log(Level.WARNING, "The class " + e.getMessage() + " cannot be found.");
            getLogger().log(Level.WARNING, "Is this module missing a required dependency?");
		} catch (Throwable e) {
			ExceptionHandler.handleException(e);
			getLogger().log(Level.WARNING, "Unknown cause.");
			getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load.");
		}
		
		return LoadResult.Fail;
	}

	/**
	 * Reloads the Loader.
	 * 
	 * @return the list of modules loaded
	 */
	public List<Module> reload() {
		unload();
		generateClassLoader();
		return load();
	}

	/**
	 * Sorts a list of Loadables by name in alphabetical order.
	 * 
	 * @param loadables
	 *            The list of Loadables to sort
	 * @return The sorted list of Loadables
	 */
	public List<Module> sort(List<Module> loadables) {
		List<Module> sortedLoadables = new ArrayList<Module>();
		List<String> names = new ArrayList<String>();

		for (Loadable t : loadables) {
			names.add(t.getName());
		}
		
		Collections.sort(names);

		for (String name : names) {
			for (Module t : loadables) {
				if (t.getName().equals(name)) {
					sortedLoadables.add(t);
				}
			}
		}

		return sortedLoadables;
	}

	/**
	 * Sorts a map of Loadables by name in alphabetical order.
	 * 
	 * @param loadables
	 *            The map of Loadables to sort
	 * @return The sorted map of Loadables
	 */
	public Map<String, Module> sort(Map<String, Module> loadables) {
		Map<String, Module> sortedLoadables = new HashMap<String, Module>();
		List<String> names = new ArrayList<String>(loadables.keySet());

		Collections.sort(names);

		for (String name : names) {
			sortedLoadables.put(name, loadables.get(name));
		}

		return sortedLoadables;
	}

	/**
	 * Unloads the Loader.
	 */
	public void unload() {
		loadables.clear();
	}
}