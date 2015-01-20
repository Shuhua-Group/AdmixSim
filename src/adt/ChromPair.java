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
	Random random;

	public ChromPair(Chromosome chrom1, Chromosome chrom2, Random random) {
		this.chrom1 = chrom1;
		this.chrom2 = chrom2;
		this.random = random;
	}

	public Chromosome getChromosome(int index) {
		if (index == 1) {
			return chrom1;
		} else {
			return chrom2;
		}
	}

	int getPoissonNumber(double lambda) {
		/*
		 * A simple algorithm to randomly generate Poisson distributed numbers
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

	public double[] breakPoints(double length) {
		int breakNumber = getPoissonNumber(length);
		double[] breakpoints = new double[breakNumber + 2];
		breakpoints[0] = 0.0;
		for (int i = 1; i <= breakNumber; i++) {
			breakpoints[i] = random.nextDouble() * length;
		}
		breakpoints[breakNumber + 1] = length;
		selectSort(breakpoints);
		return breakpoints;
	}

	public void selectSort(double[] data) {
		//sorting data by select sort, can be improved later
		int iMin;
		for (int i = 0; i < data.length - 1; i++) {
			iMin = i;
			for (int j = i + 1; j < data.length; j++) {
				if (data[j] < data[iMin]) {
					iMin = j;
				}
			}
			if (iMin != i) {
				double tmp = data[i];
				data[i] = data[iMin];
				data[iMin] = tmp;
			}
		}
	}

	public ChromPair recombine() {
		if (chrom1.getLength() != chrom2.getLength()) {
			System.err.println("Chromosome length differ, please check again");
			return this;
		}
		double[] bps = breakPoints(chrom1.getLength());
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
		return new ChromPair(new Chromosome(segs1), new Chromosome(segs2),
				random);
	}
}
