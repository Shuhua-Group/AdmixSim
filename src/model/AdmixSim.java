/*
 * AdmixSim Implemented in java
 * Version: 1.2.0
 * Author: Young
 * License: GNU GPL v3 http://www.gnu.org/licenses/gpl.html
 * The software is free software and no guarantee, users at their own risks
 */

package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import adt.ChromPair;
import adt.Chromosome;
import adt.Population;
import adt.Segment;
import dm.CopyAnc;

public class AdmixSim {
	public static void main(String[] args) {
		int gen = 1;
		int nsample = 10;
		int nanc = 2;
		long seed = 0;
		boolean setSeed = false;
		double length = 1.0;
		String parFile = "";
		String prefix = "";
		String outprefix = "output";
		if (args.length < 1 || (args.length < 4 && (!args[0].equals("-h") || !args[0].equals("--help")))) {
			System.err.println("Need more arguments than provided, use -h/--help to get help");
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
			} else if (args[i].equals("-l") || args[i].equals("--leng")) {
				length = Double.parseDouble(args[++i]);
			} else if (args[i].equals("-f") || args[i].equals("--file")) {
				parFile = args[++i];
			} else if (args[i].equals("-i") || args[i].equals("--input")) {
				prefix = args[++i];
			} else if (args[i].equals("-o") || args[i].equals("--output")) {
				outprefix = args[++i];
			} else if (args[i].equals("-s") || args[i].equals("--seed")) {
				seed = Long.parseLong(args[++i]);
				setSeed = true;
			}
		}
		if (parFile.length() == 0){
			System.err.println("Model description file required");
			System.exit(1);
		}
		if (prefix.length() == 0){
			System.err.println("Input file prefix must be specified");
			System.exit(1);
		}
		Random random = null;
		if (setSeed) {
			random = new Random(seed);
		} else {
			random = new Random();
		}
		GeneralModel gm = new GeneralModel(parFile, gen, nanc, random);
		gm.evolve(length);
		output(prefix, outprefix, gm.getPop(), gm.getInitAnc(), nsample);
	}

	public static void output(String prefix, String outprefix, Population admp,
			int[] initAnc, int nsample) {
		CopyAnc ca = new CopyAnc();
		String hapfile = prefix + ".hap";
		String mapfile = prefix + ".map";
		String mixhapfile = outprefix + ".hap";
		String segsfile = outprefix + ".seg";
		Map<Integer, Vector<String>> anchaps = ca.readHaplo(hapfile, initAnc);
		Vector<Double> map = ca.readMap(mapfile);
		BufferedWriter hapbw = null;
		BufferedWriter segbw = null;
		try {
			hapbw = new BufferedWriter(new FileWriter(mixhapfile));
			segbw = new BufferedWriter(new FileWriter(segsfile));
			for (ChromPair ind : admp.sample(nsample)) {
				for (int k = 1; k <= 2; k++) {
					//Here must duplicate a new Chromosome to avoid change of original copy
					Chromosome chr = ind.getChromosome(k).duplicate();
					chr.smooth();
					hapbw.write(ca.copy(anchaps, map, chr));
					hapbw.newLine();
					for (Segment seg : chr.getSegments()) {
						segbw.write(String.format("%.8f\t%.8f\t%d", seg.getStart(), seg.getEnd(), seg.getLabel() / 10000));
						segbw.newLine();
					}
				}
			}
			hapbw.flush();
			segbw.flush();
			if (hapbw != null) {
				hapbw.close();
			}
			if (segbw != null) {
				segbw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void help() {
		System.out.println("========================================================================================");
		System.out.println("AdmixSim.jar");
		System.out.println("Description: A forward-time simulator for generalized admixture model");
		System.out.println("Arguments:");
		System.out.println("	-h/--help	print help message [optional]");
		System.out.println("	-f/--file	model description file [required]");
		System.out.println("	-i/--input	prefix of input files [required]");
		System.out.println("	-g/--gen	generations since admixture [optional, default: 1]");
		System.out.println("	-k/--nanc	number of ancestral populations [optional, default: 2]");
		System.out.println("	-l/--leng	length of chromosome to be simulated [optional, default: 1.0]");
		System.out.println("	-n/--samp	number of individual(s) to be sampled [optional, default: 10]");
		System.out.println("	-o/--output	prefix of output files [optional, default: output]");
		System.out.println("	-s/--seed	seed of random generator [optional, default: current time]");
		System.out.println("========================================================================================");
	}

}