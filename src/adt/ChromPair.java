/*
 * AdmSimulator
 * ChromPair.java
 * Two chromosomes paired as a chromosome pair, and the recombination occur follow a Poisson
 * Process, with rate 1 per generation, unit in Morgan.
 * The processes to generate break points are: 
 * 1) Generate the number of break points follow Poisson Process;
 * 2) Randomly assign the break points along the chromosome
 */
package adt;

import java.util.Random;
import java.util.Vector;

public class ChromPair {
	
	private Chromosome chrom1;
	private Chromosome chrom2;
	//Random random;

	public ChromPair(Chromosome chrom1, Chromosome chrom2) {
		this.chrom1 = chrom1;
		this.chrom2 = chrom2;
		//this.random = random;
	}

	public Chromosome getChromosome(int index) {
		if (index == 1) {
			return chrom1;
		} else {
			return chrom2;
		}
	}

	int getPoisonNumber(double lambda, Random random) {
		/*
		 * A simple algorithm to randomly generate Poison distributed numbers
		 * by Donald Knuth http://en.wikipedia.org/wiki/Donald_Knuth
		 */
		double length = Math.exp(-lambda);
		double prob = 1.0;
		int number = 0;
		
		do {
			number++;
			prob *= random.nextDouble();
		} while (prob > length);
		return number - 1;
	}

	public double[] breakPoints(double length, Random random) {
		/* 
		 * generate break points along the chromosome
		 * firstly, generate the number of break points following Poison distribution
		 * secondly, randomly add break points to the chromosome
		 */
		int breakNumber = getPoisonNumber(length, random);
		double[] breakpoints = new double[breakNumber + 2];
		breakpoints[0] = 0.0;
		
		for (int i = 1; i <= breakNumber; i++) {
			breakpoints[i] = random.nextDouble() * length;
		}
		breakpoints[breakNumber + 1] = length;
		insertSort(breakpoints);
		return breakpoints;
	}

	public void insertSort(double[] data) {
		//sorting data by insertSort
		for (int i = 1; i < data.length; i++) {
			double key =data[i];
			int j = i;
			while (j > 0 && data[j-1] > key){
				data[j] = data[j-1];
				j--;
			}
			data[j] = key;
		}
	}

	public ChromPair recombine(Random random) {
		/*
		 * mimic recombination of diploids
		 */
		if (chrom1.getLength() != chrom2.getLength()) {
			System.err.println("Chromosome length differs, please check again");
			return this;
		}
		double[] bps = breakPoints(chrom1.getLength(), random);
		double start=bps[0];
		double end;
		Vector<Segment> segs1, segs2;
		segs1 = new Vector<Segment>();
		segs2 = new Vector<Segment>();
		
		for(int i=1;i<bps.length;i++){
			end=bps[i];
			if (i % 2 == 1) {
				for (Segment sg : chrom1.extractSegment(start, end))
					segs1.add(sg);
				for (Segment sg : chrom2.extractSegment(start, end))
					segs2.add(sg);
			} else {
				for (Segment sg : chrom1.extractSegment(start, end))
					segs2.add(sg);
				for (Segment sg : chrom2.extractSegment(start, end))
					segs1.add(sg);
			}
			start = end;
		}
		return new ChromPair(new Chromosome(segs1), new Chromosome(segs2));
	}
}
