package org.szernex.folderflattener.test;

import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileCollectorTest
{
	private File temprootdir;

	@Before
	public void setUp() throws Exception
	{
		File file = File.createTempFile("temp", null);

		temprootdir = new File(file.getParentFile(), "testing");
		temprootdir.mkdir();
		temprootdir.deleteOnExit();
		file.deleteOnExit();
	}

	@After
	public void tearDown() throws Exception
	{

	}

	private Set<File> buildTempDir(File parentdir, String fileprefix, int filecount) throws IOException
	{
		Set<File> output = new HashSet<File>();

		if (parentdir != null && !parentdir.isDirectory())
		{
			parentdir.mkdir();
		}

		for (int i = 0; i < filecount; i++)
		{
			File file = null;

			if (parentdir == null)
			{
				file = File.createTempFile(fileprefix + "_" + i, null);
			}
			else
			{
				file = File.createTempFile(fileprefix + "_" + i, null, parentdir);
			}

			file.deleteOnExit();
			output.add(file);
		}

		return output;
	}
}