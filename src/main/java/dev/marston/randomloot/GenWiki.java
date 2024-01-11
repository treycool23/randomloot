package dev.marston.randomloot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;

public class GenWiki {

	private static void write(String s, FileWriter f) throws IOException {
		f.write(s + "\n");
	}

	private static void writeMod(Modifier m, FileWriter f) throws IOException {
		write("### " + m.name(), f);
		write("id: `" + m.tagName() + "`", f);
		write(m.description(), f);
	}

	private static void writeMods(Set<Modifier> mods, FileWriter f) throws IOException {
		List<Modifier> sortedList = new ArrayList<>(mods);
		sortedList.sort((o1, o2) -> {
			return o1.tagName().compareTo(o2.name());
		});

		for (Iterator<Modifier> iterator = sortedList.iterator(); iterator.hasNext();) {
			Modifier modifier = iterator.next();
			writeMod(modifier, f);
		}
	}

	private static void writeModifiers(FileWriter f) throws IOException {

		write("# Modifiers", f);
		write("This is a full list of modifiers in the game and a description of what they do.", f);

		write("## Breakers", f);
		write("These effects are applied when breaking blocks.", f);
		writeMods(ModifierRegistry.BREAKERS, f);

		write("## Holders", f);
		write("These effects are applied when holding the tool.", f);
		writeMods(ModifierRegistry.HOLDERS, f);

		write("## Users", f);
		write("These effects are applied when right clicking.", f);
		writeMods(ModifierRegistry.USERS, f);

		write("## Hurters", f);
		write("These effects are applied when hurting enemies.", f);
		writeMods(ModifierRegistry.HURTERS, f);

	}

	public static void genWiki() {

		String isProd = System.getenv("RL_PROD").strip();
		RandomLootMod.LOGGER.info("RL_PROD: " + isProd);

		if (isProd.contains("false")) {
			RandomLootMod.LOGGER.info("Creating wiki...");

			try {
				FileWriter wikiWriter = new FileWriter("../MODIFIERS.md");
				writeModifiers(wikiWriter);
				wikiWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
