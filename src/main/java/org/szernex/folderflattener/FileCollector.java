package org.szernex.folderflattener;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

	public Set<File> collect(File rootdir)
	{
		Set<File> output = new HashSet<File>();

		if (rootdir.exists() && rootdir.isDirectory())
		{
			output.addAll(Arrays.asList(rootdir.listFiles(this.filefilter)));

			File[] dirs = rootdir.listFiles(this.dirfilter);
			Set<File> temp = new HashSet<File>();

			for (int i = 0; i < dirs.length; i++)
			{
				int counter = 0;

				temp.clear();
				temp.addAll(collect(dirs[i]));


				for (File file : temp)
				{
					File newfile = new File(rootdir, file.getName());

					while (output.contains(newfile))
					{
						counter++;
						newfile = new File(rootdir, String.format("%s_%d%s", getFileName(file), counter, getFileExtension(file)));
					}

					output.add(newfile);
				}
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
