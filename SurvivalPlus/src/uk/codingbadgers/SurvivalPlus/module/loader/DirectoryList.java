package uk.codingbadgers.SurvivalPlus.module.loader;

import com.google.common.base.Preconditions;

import java.io.File;
import java.util.ArrayList;

/**
 * A special list for holding the directories that modules are loaded from by
 * the module loaded.
 * <p />
 * If this list is set to read only ({@link #isReadonly()} == {@code true})
 * then new entries cannot be added to the list until it is set back to read
 * write mode by the loader.
 * This is so directories cannot be added to the loader after the loader has
 * computed the list of modules to be loaded.
 * <p />
 * As well as this only directories can be added to the list, if you try and
 * add any other type of file a {@link IllegalArgumentException} will be thrown
 * by the add method.
 */
public class DirectoryList extends ArrayList<File> {

    private boolean readonly = true;

    @Override
    public boolean add(File element) {
        Preconditions.checkState(readonly, "Directory list currently read only.");
        Preconditions.checkArgument(element.isDirectory(), "Can only add directories to the directory list.");

        return super.add(element);
    }

    @Override
    public void add(int index, File element) {
        Preconditions.checkState(readonly, "Directory list currently read only.");
        Preconditions.checkArgument(element.isDirectory(), "Can only add directories to the directory list.");

        super.add(index, element);
    }

    @Override
    public File set(int index, File element) {
        Preconditions.checkState(readonly, "Directory list currently read only.");
        Preconditions.checkArgument(element.isDirectory(), "Can only add directories to the directory list.");

        return super.set(index, element);
    }

    void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public boolean isReadonly() {
        return this.readonly;
    }
}
