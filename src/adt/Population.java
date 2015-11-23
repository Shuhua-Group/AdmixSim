/*
 * AdmSimulator
 * Population.java
 * Here population consists a diploid individuals, in the form of Chromosome pair
 * No mutation, no selection and no generation overlap
 */
package adt;

import java.util.Random;
import java.util.Vector;

public class Population {
	private int Ne;
	private int label;
	//Random random;
	private Vector<ChromPair> indivs;

	public Population(int label, Vector<ChromPair> indivs) {
		//super();
		this.label = label;
		//this.random = random;
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

	public Vector<ChromPair> sample(int nsamp, Random random) {
		Vector<ChromPair> samples = new Vector<ChromPair>();
		int total = getNe();
		for (int i = 0; i < nsamp; i++) {
			samples.add(indivs.elementAt(random.nextInt(total)));
		}
		return samples;
	}

	public Population[] split(double prop, Random random) {
		Population[] pops = new Population[2];
		int npop1 = (int) (indivs.size() * prop);
		int[] index1 = new int[npop1];
		Vector<Integer> index = new Vector<Integer>();
		for (int i = 0; i < indivs.size(); i++)
			index.add(i);
		int tmpSize, tmp;
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
		pops[0] = new Population(getLabel(), inds);
		inds = new Vector<ChromPair>();
		for (int i : index) {
			inds.add(indivs.elementAt(i));
		}
		pops[1] = new Population(getLabel() + 1, inds);
		return pops;
	}

	public Population evolve(int Ne, Random random) {
		Vector<ChromPair> inds = new Vector<ChromPair>();
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
			//int tmp = random.nextInt() % 2;
			//Chromosome hap1 = indivs.elementAt(index1).getChromosome(tmp);
			//tmp = random.nextInt() % 2;
			// randomly choose one chromosome of another individual
			//Chromosome hap2 = indivs.elementAt(index2).getChromosome(tmp);
			// recombination and form an offspring
			//ChromPair cp = new ChromPair(hap1, hap2, random);
			//cp = cp.recombine();
			//the two chromosome of one individual crossover and recombine to form gametes
			//and randomly choose one to pair with gamete from another individual
			int tmp = random.nextInt() % 2;
			Chromosome gamete1 = indivs.elementAt(index1).recombine(random).getChromosome(tmp);
			tmp = random.nextInt() % 2;
			Chromosome gamete2 = indivs.elementAt(index2).recombine(random).getChromosome(tmp);
			inds.add(new ChromPair(gamete1, gamete2));
		}
		return new Population(getLabel(), inds);
	}
}
