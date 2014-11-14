package adt;

import java.util.Random;
import java.util.Vector;

public class ChromPair {
	Chromosome chrom1;
	Chromosome chrom2;

	public ChromPair(Chromosome chrom1, Chromosome chrom2) {
		this.chrom1 = chrom1;
		this.chrom2 = chrom2;
	}

	Chromosome getChromosome(int index) {
		if (index == 1) {
			return chrom1;
		} else {
			return chrom2;
		}
	}

	double waitTime(double lambda) {
		/*
		 * Poisson Process waiting time T follow distribution below: P(T<t) = 1
		 * - exp(-lambda*t)
		 */
		Random random = new Random();
		double probability = random.nextDouble();
		return -Math.log(1 - probability) / lambda;
	}

	Vector<Double> breakPoints(double length) {
		Vector<Double> breakpoints = new Vector<Double>();
		breakpoints.add(0.0);
		while (breakpoints.lastElement() < length) {
			breakpoints.add(breakpoints.lastElement() + waitTime(1));
		}
		breakpoints.setElementAt(length, breakpoints.size() - 1);
		return breakpoints;
	}

	public ChromPair recombine() {
		if (chrom1.getLength() != chrom2.getLength()) {
			System.err.println("Chromosome length differ, please check again");
			return this;
		}
		Vector<Double> bps = breakPoints(chrom1.getLength());
		double start = bps.firstElement();
		double end;
		Vector<Segment> segs1, segs2;
		segs1 = new Vector<Segment>();
		segs2 = new Vector<Segment>();
		for (int i = 1; i < bps.size(); i++) {
			end = bps.elementAt(i);
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

//	public static void main(String[] args) {
//		Segment s1, s2;
//		s1 = new Segment(0, 2, 1);
//		s2 = new Segment(0, 2, 2);
//		Chromosome chr1, chr2;
//		chr1 = new Chromosome(new Vector<Segment>());
//		chr1.addSegment(s1);
//		chr2 = new Chromosome(new Vector<Segment>());
//		chr2.addSegment(s2);
//		ChromPair cp = new ChromPair(chr1, chr2);
//		System.out.println("Original ChromPair");
//		System.out.print("Chrom1:");
//		for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//		System.out.print("Chrom2:");
//		for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//		System.out.println("After 1 recombination");
//		cp = cp.recombine();
//		System.out.print("Chrom1:");
//		for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//		System.out.print("Chrom2:");
//		for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//		System.out.println("After 2 recombination");
//		cp = cp.recombine();
//		System.out.print("Chrom1:");
//		for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//		System.out.print("Chrom2:");
//		for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//		System.out.println("After 3 recombination");
//		cp = cp.recombine();
//		System.out.print("Chrom1:");
//		for (Segment s : cp.getChromosome(1).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//		System.out.print("Chrom2:");
//		for (Segment s : cp.getChromosome(2).extractSegment(0, 2)) {
//			System.out.print("(" + s.getStart() + "," + s.getEnd() + ","
//					+ s.getLabel() + ")");
//		}
//		System.out.println();
//	}

}
