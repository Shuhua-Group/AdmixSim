/*
 * AdmSimulator
 * Segment.java
 * A Segment has a start point, an end point and a label indicates which ancestral population
 * and which haplotype originated.
 * The label is combined information, with the last four digits indicate the serial of haplotype
 * and the rest indicate ancestral. For example, label=10001 denotes from ancestral population
 * 1 and the first haplotype from ancestral population.
 * Warning, the maximum ancestral haplotypes allowed is 9999 for each ancestral population
 */
package adt;

public class Segment {
	private double start;
	private double end;
	private int label;

	public Segment() {
		this.start = 0;
		this.end = 0;
		this.label = 0;
	}

	public Segment(double start, double end, int label) {
		super();
		this.start = start;
		this.end = end;
		this.label = label;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public double getLength() {
		return end - start;
	}

	public Segment duplicate() {
		return new Segment(start, end, label);
	}
}
