package adt;

import java.util.Random;
import java.util.Vector;

public class Population {
	private int Ne;
	private int label;
	private Vector<Chromosome> haplotypes;

	public Population(int label, Vector<Chromosome> haplotypes) {
		super();
		this.label = label;
		this.haplotypes = haplotypes;
		Ne = haplotypes.size() / 2;
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
		Ne = haplotypes.size() / 2;
	}

	public Vector<Chromosome> getHaplotypes() {
		return haplotypes;
	}

	public void addHaplotype(Chromosome chrom) {
		haplotypes.addElement(chrom);
	}

	public Vector<Chromosome> sample(int nsamp) {
		Vector<Chromosome> samples = new Vector<Chromosome>();
		int total = 2 * getNe();
		Random random = new Random();
		for (int i = 0; i < nsamp; i++) {
			samples.add(haplotypes.elementAt(random.nextInt(total)));
		}
		return samples;
	}

	public Population[] split(double prop) {
		Population[] pops = new Population[2];
		int npop1 = (int) (haplotypes.size() * prop);
		int[] index1 = new int[npop1];
		Vector<Integer> index = new Vector<Integer>();
		for (int i = 0; i < haplotypes.size(); i++)
			index.add(i);
		int tmpSize, tmp;
		Random random = new Random();
		for (int i = 0; i < npop1; i++) {
			tmpSize = index.size();
			tmp = random.nextInt(tmpSize);
			index1[i] = index.elementAt(tmp);
			index.remove(tmp);
		}
		Vector<Chromosome> haplos = new Vector<Chromosome>();
		for (int i = 0; i < npop1; i++) {
			haplos.add(haplotypes.elementAt(index1[i]));
		}
		pops[0] = new Population(getLabel(), haplos);
		haplos = new Vector<Chromosome>();
		for (int i : index) {
			haplos.add(haplotypes.elementAt(i));
		}
		pops[1] = new Population(getLabel() + 1, haplos);
		return pops;
	}

	public Population evolve(int Ne) {
		Vector<Chromosome> haplos = new Vector<Chromosome>();
		Random random = new Random();
		int curNumHaplos = getNe() * 2;
		int ind1, ind2;
		for (int i = 0; i < Ne; i++) {
			ind1 = random.nextInt(curNumHaplos);
			ind2 = random.nextInt(curNumHaplos);
			while (ind1 == ind2) {
				ind2 = random.nextInt(curNumHaplos);
			}
			ChromPair cp = new ChromPair(haplotypes.elementAt(ind1),
					haplotypes.elementAt(ind2));
			cp = cp.recombine();
			haplos.add(cp.getChromosome(1));
			haplos.add(cp.getChromosome(2));
		}
		return new Population(getLabel(), haplos);
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
