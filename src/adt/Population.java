package adt;

import java.util.Random;
import java.util.Vector;

public class Population {
	private int Ne;
	private int label;
	Random random;
	private Vector<ChromPair> indivs;

	public Population(int label, Vector<ChromPair> indivs, Random random) {
		super();
		this.label = label;
		this.random = random;
		this.indivs = indivs;
		Ne = indivs.size();
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public int getNe() {
		updateNe();
		return Ne;
	}

	public void updateNe() {
		Ne = indivs.size();
	}

	public Vector<ChromPair> getIndivs() {
		return indivs;
	}

	public void addIndiv(ChromPair indiv) {
		indivs.addElement(indiv);
	}

	public Vector<ChromPair> sample(int nsamp) {
		Vector<ChromPair> samples = new Vector<ChromPair>();
		int total = getNe();
		// Random random = new Random();
		for (int i = 0; i < nsamp; i++) {
			samples.add(indivs.elementAt(random.nextInt(total)));
		}
		return samples;
	}

	public Population[] split(double prop) {
		Population[] pops = new Population[2];
		int npop1 = (int) (indivs.size() * prop);
		int[] index1 = new int[npop1];
		Vector<Integer> index = new Vector<Integer>();
		for (int i = 0; i < indivs.size(); i++)
			index.add(i);
		int tmpSize, tmp;
		// Random random = new Random();
		for (int i = 0; i < npop1; i++) {
			tmpSize = index.size();
			tmp = random.nextInt(tmpSize);
			index1[i] = index.elementAt(tmp);
			index.remove(tmp);
		}
		Vector<ChromPair> inds = new Vector<ChromPair>();
		for (int i = 0; i < npop1; i++) {
			inds.add(indivs.elementAt(index1[i]));
		}
		pops[0] = new Population(getLabel(), inds, random);
		inds = new Vector<ChromPair>();
		for (int i : index) {
			inds.add(indivs.elementAt(i));
		}
		pops[1] = new Population(getLabel() + 1, inds, random);
		return pops;
	}

	public Population evolve(int Ne) {
		Vector<ChromPair> inds = new Vector<ChromPair>();
		// Random random = new Random();
		int curNumInds = getNe();
		int index1, index2;
		for (int i = 0; i < Ne; i++) {
			// randomly choose two individuals
			index1 = random.nextInt(curNumInds);
			index2 = random.nextInt(curNumInds);
			while (index1 == index2) {
				index2 = random.nextInt(curNumInds);
			}
			// randomly choose one chromosome of first individual
			int tmp = random.nextInt() % 2;
			Chromosome hap1 = indivs.elementAt(index1).getChromosome(tmp);
			tmp = random.nextInt() % 2;
			// randomly choose one chromosome of another individual
			Chromosome hap2 = indivs.elementAt(index2).getChromosome(tmp);
			// recombination and form an offspring
			ChromPair cp = new ChromPair(hap1, hap2, random);
			cp = cp.recombine();
			inds.add(cp);
		}
		return new Population(getLabel(), inds, random);
	}

	// public static void main(String[] args) {
	// Vector<Segment> segs = new Vector<Segment>();
	// segs.add(new Segment(0, 2, 1));
	// Chromosome chr = new Chromosome(segs);
	// Vector<Chromosome> haplo = new Vector<Chromosome>();
	// haplo.add(chr);
	// Population p = new Population(1, haplo);
	// segs = new Vector<Segment>();
	// segs.add(new Segment(0, 2, 2));
	// chr = new Chromosome(segs);
	// p.addHaplotype(chr);
	// for(int i=0;i<4;i++){
	// p=p.evolve(20);
	// }
	// for(Chromosome c:p.sample(10)){
	// c.smooth();
	// c.print();
	// }
	// }
}
