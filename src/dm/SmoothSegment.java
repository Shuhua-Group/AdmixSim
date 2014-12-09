package dm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SmoothSegment {
	public void smooth(String input, String output) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(input));
			bw = new BufferedWriter(new FileWriter(output));
			String line;
			String start = null;
			String end = null;
			String label = null;
			while ((line = br.readLine()) != null) {
				String[] tmp = line.split("\\s+");
				if (start == null) {
					// No segment, just create a new one
					start = tmp[0];
					end = tmp[1];
					label = tmp[2];
				} else if (label.equals(tmp[2])) {
					// with the same label, update the end position
					end = tmp[1];
				} else {
					// non-empty and with different label, output previous one
					// and create a new one
					bw.write(start + "\t" + end + "\t" + label);
					bw.newLine();
					start = tmp[0];
					end = tmp[1];
					label = tmp[2];
				}
				//System.out.println(label);
			}
			// output the last one
			bw.write(start + "\t" + end + "\t" + label);
			bw.newLine();
			if (br != null) {
				br.close();
			}
			bw.flush();
			if (bw != null) {
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length > 1) {
			SmoothSegment sm = new SmoothSegment();
			sm.smooth(args[0], args[1]);
		} else {
			System.err.println("Warning: need more arguments than provided!");
			System.err
					.println("Usage: java -jar SmooothSegment.jar <input> <output>");
		}
	}

}
