/*
 * AdmixModel Implemented in java
 * Version: 1.2.0
 * Author: Young
 */

package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import adt.Chromosome;
import adt.Population;
import adt.Segment;
import dm.CopyAnc;

public class AdmSimulator {
	public static void main(String[] args) {
		int gen = 1;
		int nsample = 1;
		int nanc = 2;
		double length = 1.0;
		String parFile = "";
		String prefix = "";
		String outprefix = "";
		// System.out.println(args.length);
		if (args.length < 1	|| (args.length < 14 && (!args[0].equals("-h") || !args[0].equals("--help")))) {
			System.err.println("Need more arguments than provided");
			help();
			System.exit(1);
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-h") || args[i].equals("--help")) {
				help();
			} else if (args[i].equals("-g") || args[i].equals("--gen")) {
				gen = Integer.parseInt(args[++i]);
			} else if (args[i].equals("-k") || args[i].equals("--nanc")) {
				nanc = Integer.parseInt(args[++i]);
			} else if (args[i].equals("-n") || args[i].equals("--samp")) {
				nsample = Integer.parseInt(args[++i]);
			} else if (args[i].equals("-l") || args[i].equals("--len")) {
				length = Double.parseDouble(args[++i]);
			} else if (args[i].equals("-f") || args[i].equals("--file")) {
				parFile = args[++i];
			} else if (args[i].equals("-p") || args[i].equals("--prefix")) {
				prefix = args[++i];
			} else if (args[i].equals("-o") || args[i].equals("--output")) {
				outprefix = args[++i];
			}
		}
		GeneralModel gm = new GeneralModel(parFile, gen, nanc);
		gm.evolve(length);
		output(prefix, outprefix, gm.getPop(), gm.getInitAnc(), nsample);
	}

	public static void output(String prefix, String outprefix, Population admp, int[] initAnc,
			int nsample) {
		CopyAnc ca = new CopyAnc();
		String hapfile = prefix + ".hap";
		String mapfile = prefix + ".map";
		String mixhapfile = outprefix + "_mix.hap";
		String segsfile = outprefix + ".seg";
		Map<Integer, Vector<String>> anchaps = ca.readHaplo(hapfile, initAnc);
		Vector<Double> map = ca.readMap(mapfile);
		BufferedWriter mpbw = null;
		BufferedWriter segbw = null;
		try {
			mpbw = new BufferedWriter(new FileWriter(mixhapfile));
			segbw = new BufferedWriter(new FileWriter(segsfile));
			for (Chromosome chr : admp.sample(nsample)) {
				mpbw.write(ca.copy(anchaps, map, chr));
				mpbw.newLine();
				chr.smooth();
				for (Segment seg : chr.getSegments()) {
					segbw.write(String.format("%.8f\t%.8f\t%d",
							seg.getStart(), seg.getEnd(),
							seg.getLabel() / 10000));
					segbw.newLine();
				}
			}
			mpbw.flush();
			segbw.flush();
			if (mpbw != null) {
				mpbw.close();
			}
			if (segbw != null) {
				mpbw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void help() {
		System.out.println("Description: Admixture Model Simulator");
		System.out.println("Arguments:");
		System.out.println("	-h/--help	print help message");
		System.out.println("	-g/--gen	generation since admixture");
		System.out.println("	-k/--nanc	number of ancestral population");
		System.out.println("	-l/--len	length of chromosome to be simulated");
		System.out.println("	-f/--file	file name of model description parameters");
		System.out.println("	-n/--samp	number of haplotypes sampled from admixed population");
		System.out.println("	-p/--prefix	prefix of input file");
		System.out.println("	-o/--output	prefix of output file");
	}

}
