package org.szernex.folderflattener;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

public class FileCollector
{
	private FileFilter filefilter;
	private FileFilter dirfilter;

	public FileCollector()
	{
		this(".*");
	}

	public FileCollector(final String filterpattern)
	{
		this.filefilter = new FileFilter()
		{
			@Override
			public boolean accept(File file)
			{
				return (file.isFile() && file.getName().matches(filterpattern));
			}
		};

		this.dirfilter = new FileFilter()
		{
			@Override
			public boolean accept(File file)
			{
				return (file.isDirectory());
			}
		};
	}

	public Map<File, File> flatten(File rootdir, Set<File> files)
	{
		Map<File, File> output = new HashMap<File, File>();

		for (File file : files)
		{
			File newfile = new File(rootdir, file.getName());
			int counter = 0;

			while (output.containsValue(newfile))
			{
				counter++;
				newfile = new File(rootdir, String.format("%s_%d%s", getFileName(file), counter, getFileExtension(file)));
			}

			output.put(file, newfile);
		}

		return output;
	}

	public Set<File> collect(File rootdir)
	{
		Set<File> output = new HashSet<File>();

		if (rootdir.exists() && rootdir.isDirectory())
		{
			output.addAll(Arrays.asList(rootdir.listFiles(this.filefilter)));

			File[] dirs = rootdir.listFiles(this.dirfilter);

			for (int i = 0; i < dirs.length; i++)
			{
				output.addAll(collect(dirs[i]));
			}
		}

		return output;
	}

	private String getFileName(File file)
	{
		return file.getName().substring(0, file.getName().indexOf("."));
	}

	private String getFileExtension(File file)
	{
		return file.getName().substring(file.getName().indexOf("."));
	}
}
