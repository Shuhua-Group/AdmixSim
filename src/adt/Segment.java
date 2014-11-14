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

	// public static void main(String[] args) {
	// System.out.println("Java is different from C++");
	//
	// }
}
