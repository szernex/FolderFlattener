package org.szernex.folderflattener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class Main
{
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args)
	{
		FileCollector collector = new FileCollector();
		Set<File> input = collector.collect(new File("d://testing"));
		Map<File, File> output = collector.flatten(new File("d://testing"), input);

		for (Map.Entry<File, File> entry : output.entrySet())
		{
			System.out.print(entry.getKey() + " -> " + entry.getValue() + " : ");
			System.out.println(entry.getKey().renameTo(entry.getValue()));
		}
	}
}
