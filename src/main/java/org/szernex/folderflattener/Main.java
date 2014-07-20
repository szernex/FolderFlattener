package org.szernex.folderflattener;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Main
{
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args)
	{
		OptionParser parser = new OptionParser();

		parser.accepts("s", "The source root directory.")
				.withRequiredArg()
				.describedAs("source")
				.required();
		parser.accepts("t", "The target directoy. Can be identical to source.")
				.withRequiredArg()
				.describedAs("target")
				.required();
		parser.accepts("f", "The file filter in form of a regular expression. Optional.")
				.withRequiredArg()
				.describedAs("filter")
				.defaultsTo(".*");

		OptionSet options = null;

		try
		{
			options = parser.parse(args);
		}
		catch (OptionException oex)
		{
			try
			{
				parser.printHelpOn(System.out);
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
		}


		FileCollector collector = new FileCollector(options.valueOf("f").toString());
		Set<File> input = collector.collect(new File(options.valueOf("s").toString()));
		Map<File, File> output = collector.flatten(new File(options.valueOf("t").toString()), input);
		int moved = 0;

		for (Map.Entry<File, File> entry : output.entrySet())
		{
			System.out.print(entry.getKey() + " -> " + entry.getValue() + " : ");

			if (entry.getKey().renameTo(entry.getValue()))
			{
				System.out.println("OK");
				moved++;
			}
			else
			{
				System.out.println("FAILED");
			}
		}

		System.out.println("Finished. Moved " + moved + " file(s).");
	}
}
