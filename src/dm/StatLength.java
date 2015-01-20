package dm;
/*
 * Current not used
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class StatLength {

	public double mean(Vector<Double> vals) {
		double sum = 0;
		Iterator<Double> it = vals.iterator();
		while (it.hasNext()) {
			sum += it.next();
		}
		return sum / vals.size();
	}

	public double var(Vector<Double> vals) {
		if (vals.size() <= 1)
			return 0;
		double meanVal = mean(vals);
		double sum = 0;
		Iterator<Double> it = vals.iterator();
		while (it.hasNext()) {
			double next = it.next();
			sum += (next - meanVal) * (next - meanVal);
		}
		return sum / vals.size();
	}

	public void stat(String file) {
		BufferedReader br = null;
		Map<Integer, Vector<Double>> ancLength;
		ancLength = new HashMap<Integer, Vector<Double>>();
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] tmp = line.split("\t");
				int key = Integer.parseInt(tmp[2]) / 10000;
				if (!ancLength.containsKey(key)) {
					Vector<Double> ancL = new Vector<Double>();
					ancLength.put(key, ancL);
				}
				double len;
				len = Double.parseDouble(tmp[1]) - Double.parseDouble(tmp[0]);
				ancLength.get(key).add(len);
			}
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int key : ancLength.keySet()) {
			Iterator<Double> it = ancLength.get(key).iterator();
			double m = mean(ancLength.get(key));
			double v = var(ancLength.get(key));
			while (it.hasNext())
				System.out.printf("%.8f\t%d\n", it.next(), key);
			System.err.printf("mean = %.6f; var = %.6f\n", m, v);
		}
	}

//	public static void main(String[] args) {
//		if (args.length > 0) {
//			StatLength sl = new StatLength();
//			sl.stat(args[0]);
//		}
//	}
}
