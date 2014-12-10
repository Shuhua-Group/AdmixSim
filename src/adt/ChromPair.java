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

	// double waitTime(double lambda) {
	// /*
	// * Poisson Process waiting time T follow distribution below: P(T<t) = 1
	// * - exp(-lambda*t)
	// */
	// // Random random = new Random();
	// double probability = random.nextDouble();
	// return -Math.log(1 - probability) / lambda;
	// }

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
		//System.out.println(breakNumber);
		double[] breakpoints = new double[breakNumber + 2];
		breakpoints[0] = 0.0;
		for (int i = 1; i <= breakNumber; i++) {
			breakpoints[i] = random.nextDouble() * length;
		}
		breakpoints[breakNumber + 1] = length;
		//breakpoints = selectSort(breakpoints);
		selectSort(breakpoints);
		return breakpoints;
	}

	public void selectSort(double[] data) {
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
		//return data;
	}

	public ChromPair recombine() {
		if (chrom1.getLength() != chrom2.getLength()) {
			System.err.println("Chromosome length differ, please check again");
			return this;
		}
		double[] bps = breakPoints(chrom1.getLength());
		//double start = bps.firstElement();
		double start=bps[0];
		double end;
		Vector<Segment> segs1, segs2;
		segs1 = new Vector<Segment>();
		segs2 = new Vector<Segment>();
		//for (int i = 1; i < bps.size(); i++) {
		for(int i=1;i<bps.length;i++){
			//end = bps.elementAt(i);
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

	// public static void main(String[] args) {
	// Segment s1, s2;
	// s1 = new Segment(0, 2, 1);
	// s2 = new Segment(0, 2, 2);
	// Chromosome chr1, chr2;
	// chr1 = new Chromosome(new Vector<Segment>());
	// chr1.addSegment(s1);
	// chr2 = new Chromosome(new Vector<Segment>());
	// chr2.addSegment(s2);
	// ChromPair cp = new ChromPair(chr1, chr2);
	// System.out.println("Original ChromPair");
	// System.out.print("Chrom1:");
	// for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// System.out.print("Chrom2:");
	// for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// System.out.println("After 1 recombination");
	// cp = cp.recombine();
	// System.out.print("Chrom1:");
	// for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// System.out.print("Chrom2:");
	// for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// System.out.println("After 2 recombination");
	// cp = cp.recombine();
	// System.out.print("Chrom1:");
	// for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// System.out.print("Chrom2:");
	// for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// System.out.println("After 3 recombination");
	// cp = cp.recombine();
	// System.out.print("Chrom1:");
	// for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// System.out.print("Chrom2:");
	// for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
	// System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
	// + s.getLabel() + ")");
	// }
	// System.out.println();
	// }

}
