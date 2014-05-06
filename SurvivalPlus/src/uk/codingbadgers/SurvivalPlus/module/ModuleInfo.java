package uk.codingbadgers.SurvivalPlus.module;

import com.google.common.base.Preconditions;
import uk.codingbadgers.SurvivalPlus.module.loader.LoadableDescriptionFile;

import java.io.File;
import java.util.jar.JarFile;

/**
 * Stub module class, used to place the module in the correct place in the load
 * queue so dependencies can be handled nicely at load time.
 */
public class ModuleInfo implements Comparable<ModuleInfo> {

    private final LoadableDescriptionFile description;
    private final JarFile jar;
    private final File file;

    public ModuleInfo(File file, JarFile jar, LoadableDescriptionFile ldf) {
        this.file = file;
        this.jar = jar;
        this.description = ldf;
    }

    public String getName() {
        return description.getName();
    }

    public JarFile getJar() {
        return jar;
    }

    public File getFile() {
        return file;
    }

    public LoadableDescriptionFile getDescription() {
        return description;
    }

    public boolean hasDependency(ModuleInfo other) {
        return this.getDescription().getDependencies().contains(other.getName());
    }

    @Override
    public int compareTo(ModuleInfo o) {
        Preconditions.checkNotNull(o);

        if (this.hasDependency(o)) {
            return 1;
        } else if(o.hasDependency(this)) {
            return -1;
        }

        return (int) Math.signum(this.getDescription().getLoadPriority().ordinal() - o.getDescription().getLoadPriority().ordinal());
    }
}
