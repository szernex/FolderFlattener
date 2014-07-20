package org.szernex.folderflattener.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.szernex.folderflattener.FileCollector;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

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

	@Test
	public void testCollectFilesInFlatDirectory() throws IOException
	{
		FileCollector collector = new FileCollector();
		Set<File> expected = new HashSet<File>();
		Set<File> result;

		expected.addAll(buildTempDir(temprootdir, "temp", 10));
		result = collector.collect(temprootdir);

		assertTrue(result.containsAll(expected));
	}

	@Test
	public void testCollectFilesWithSubDirectory() throws IOException
	{
		FileCollector collector = new FileCollector();
		Set<File> expected = new HashSet<File>();
		Set<File> temp = new HashSet<File>();
		Set<File> result;

		expected.addAll(buildTempDir(temprootdir, "temp", 10));
		temp.addAll(buildTempDir(new File(temprootdir, "subdir1"), "temp_sub1", 10));

		for (File file : temp)
		{
			expected.add(new File(temprootdir, file.getName()));
		}

		result = collector.collect(temprootdir);

		assertTrue(result.containsAll(expected));
	}

	@Test
	public void testCollectFilesWithSubDirsAndIdenticalFilenames() throws IOException
	{
		FileCollector collector = new FileCollector();
		Set<File> expected = new HashSet<File>();
		Set<File> temp = new HashSet<File>();
		Set<File> result;

		expected.addAll(buildTempDir(temprootdir, "temp", 10));
		temp.addAll(buildTempDir(new File(temprootdir, "subdir1"), "temp_sub1", 10));

		File duplicate = expected.iterator().next();
		duplicate = new File(temp.iterator().next().getParentFile(), duplicate.getName());

		duplicate.createNewFile();
		duplicate.deleteOnExit();

		for (File file : temp)
		{
			expected.add(new File(temprootdir, file.getName()));
		}

		expected.add(new File(temprootdir, getFileName(duplicate) + "_1" + getFileExtension(duplicate)));

		result = collector.collect(temprootdir);

		assertTrue(result.containsAll(expected));
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

	private String getFileName(File file)
	{
		return file.getName().substring(0, file.getName().indexOf("."));
	}

	private String getFileExtension(File file)
	{
		return file.getName().substring(file.getName().indexOf("."));
	}
}