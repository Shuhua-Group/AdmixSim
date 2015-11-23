/*
 * AdmSimulator
 * Chromosome.java
 * A Chromosome consists a serial segment, and the break points slice chromosome into segments
 * Notes: segments are continuous, in which the end of previous segment is identical the start
 * of current segment.
 */
package adt;

import java.util.Vector;

public class Chromosome {
	
	private Vector<Segment> segments;
	private Vector<Double> breaks;

	public Chromosome(Vector<Segment> segments) {
		this.segments = segments;
		breaks = new Vector<Double>();
		for (Segment seg : segments) {
			breaks.add(seg.getEnd());
		}
		if (breaks.size() > 0)
			breaks.remove(breaks.size() - 1);
	}

	public double getLength() {
		return segments.lastElement().getEnd();
	}

	public Vector<Segment> getSegments() {
		return segments;
	}

	public void addSegment(Segment segment) {
		if (null == segments) {
			segments = new Vector<Segment>();
		} else if (segments.size() > 0 && segment.getStart() != segments.lastElement().getEnd()) {
			System.err.println("There is a gap between the newly added segment and the last segment, positions of newly add segment are shifted");
			double prevEnd = segments.lastElement().getEnd();
			segment.setEnd(prevEnd + segment.getLength());
			segment.setStart(prevEnd);
		}
		if (segments.size() > 0) {
			breaks.addElement(segment.getStart());
		}
		segments.add(segment);
	}

	public Segment getSegment(int index) {
		return segments.elementAt(index);
	}

	public int indexOf(double pos) {
		//search the index of segment in which the position located
		//by binary search
		if (breaks.size() == 0)
			return 0;
		else {
			if (pos > breaks.lastElement()) {
				return breaks.size();
			}
			if (pos <= breaks.firstElement()) {
				return 0;
			}
			int left = 0;
			int right = breaks.size();
			int mid = (left + right + 1) / 2;
			while (left < right) {
				if (pos > breaks.elementAt(mid))
					left = mid;
				else
					right = mid - 1;
				mid = (left + right + 1) / 2;
			}
			return left + 1;
		}
	}

	public Vector<Segment> extractSegment(double start, double end) {
		Vector<Segment> extSegs = new Vector<Segment>();
		int startIndex = indexOf(start);
		int endIndex = indexOf(end);
		// if the start position is on the end of a segment, then move to next
		if (start == segments.elementAt(startIndex).getEnd())
			startIndex++;
		Segment ss = segments.elementAt(startIndex);
		// if the start and end positions are on the same segment, then cut part
		// of it
		if (startIndex == endIndex) {
			Segment seg = new Segment(start, end, ss.getLabel());
			extSegs.add(seg);
		} else {
			Segment seg = new Segment(start, ss.getEnd(), ss.getLabel());
			extSegs.add(seg);
			for (int i = startIndex + 1; i < endIndex; i++) {
				extSegs.add(segments.elementAt(i));
			}
			Segment se = segments.elementAt(endIndex);
			seg = new Segment(se.getStart(), end, se.getLabel());
			extSegs.add(seg);
		}
		return extSegs;
	}

	public Chromosome duplicate() {
		return new Chromosome(segments);
	}

	public void smooth() {
		//combine adjacent segments originated from the same ancestry
		Vector<Segment> newSegs = new Vector<Segment>();
		Segment seg1, seg2;
		seg1 = segments.firstElement();
		int size = segments.size();
		for (int i = 1; i < size; i++) {
			seg2 = segments.elementAt(i);
			if ((seg1.getLabel() / 1000000) == (seg2.getLabel() / 1000000)) {
				seg1.setEnd(seg2.getEnd());
			} else {
				newSegs.add(seg1);
				seg1 = seg2;
			}
		}
		newSegs.add(seg1);
		segments = newSegs;
		// Below is very important to maintain the consistency between the
		// number of breaks and segments
		updateBreaks();
	}

	public void print() {
		for (Segment s : segments) {
			System.out.printf("%.8f\t%.8f\t%d\n", s.getStart(), s.getEnd(), s.getLabel());
		}
	}

	public void updateBreaks() {
		//maintain the consistency of break points
		Vector<Double> tmp = new Vector<Double>();
		for (Segment seg : segments) {
			tmp.add(seg.getEnd());
		}
		if (tmp.size() > 0) {
			tmp.remove(tmp.size() - 1);
		}
		breaks = tmp;
	}
}
