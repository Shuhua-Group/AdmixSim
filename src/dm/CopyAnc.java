package dm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import adt.Chromosome;
import adt.Segment;

public class CopyAnc {
	public Vector<Double> readMap(String mapfile) {
		Vector<Double> position = new Vector<Double>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(mapfile));
			String line;
			while ((line = br.readLine()) != null) {
				double pos = Double.parseDouble(line);
				position.add(pos);
			}
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return position;
	}

	public Map<Integer, Vector<String>> readHaplo(String haplofile,
			int[] initAnc) {
		Map<Integer, Vector<String>> anchaps = new HashMap<Integer, Vector<String>>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(haplofile));
			String line;
			int key = 1;
			Vector<String> haps = new Vector<String>();
			anchaps.put(key, haps);
			int nInd = 0;
			int nAnc = initAnc[0];
			while ((line = br.readLine()) != null) {
				if (nInd < nAnc) {
					anchaps.get(key).add(line);
					nInd++;
				} else {
					key++;
					nInd = 0;
					nAnc = initAnc[key - 1];
					haps = new Vector<String>();
					anchaps.put(key, haps);
					anchaps.get(key).add(line);
				}
			}
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return anchaps;
	}

	public int indexOf(double pos, Vector<Double> position) {
		if (position.size() == 0)
			return 0;
		if (pos <= position.firstElement())
			return 0;
		if (pos >= position.lastElement())
			return position.size();
		int left = 0;
		int right = position.size();
		int mid = (left + right + 1) / 2;
		while (left < right) {
			// System.out.printf("%d %d %d\n", left, right, mid);
			if (pos > position.elementAt(mid))
				left = mid;
			else
				right = mid - 1;
			mid = (left + right + 1) / 2;
		}
		if (Math.abs(pos - position.elementAt(mid)) > Math.abs(pos
				- position.elementAt(mid + 1)))
			return left + 1;
		else
			return left;
	}

	public String copy(Map<Integer, Vector<String>> anchaps,
			Vector<Double> pos, Chromosome chr) {
		StringBuilder sb = new StringBuilder();
		for (Segment seg : chr.getSegments()) {
			int key = seg.getLabel() / 10000;
			int ihap = seg.getLabel() % 10000;
			int start = indexOf(seg.getStart(), pos);
			int end = indexOf(seg.getEnd(), pos);
			String tmp = anchaps.get(key).elementAt(ihap).substring(start, end);
			sb.append(tmp);
		}
		return sb.toString();
	}

	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// Vector<Double> pp = new Vector<Double>();
	// pp.add(0.0);
	// Random rand = new Random();
	// while (pp.lastElement() < 3) {
	// pp.add(pp.lastElement() + rand.nextDouble());
	// }
	// Iterator<Double> it = pp.iterator();
	// while (it.hasNext()) {
	// System.out.printf("%.4f ", it.next());
	// }
	// CopyAnc ca = new CopyAnc();
	// System.out.println("\n" + ca.indexOf(0.3, pp));
	// String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	// System.out.println(str.substring(0, 7));
	// }

}
