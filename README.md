Generalized Admixture Models Simulator
========

### Descriptions

1. Population model assumptions

    The population in each generation evolves following standard Wright Fisher model without mutation and selection. That is, randomly sample two individuals from the population, and randomly choose one of the chromosomes in each individual, pair them and recombine to form a new chromosome pair for next generation. Repeat this process until sampled N chromosome pair. Here N denotes population size in specific generation.

    Recombination is modeled as a Poisson Process along the chromosome with rate 1.

2. What the admixture model simulator can do.

   Here implemented a very flexible admixture model simulator, in which:

    1). Can take arbitrary number of parental populations;

    2). Can take arbitrary wave of population admixture;

    3). Population size can be changed generation by generation;

    4). And admixture proportions can also be changed generation by generation.

### Get started

1. Requirements

    To run the simulator, java 1.6 or upper is required.
   
2. Obtain AdmixSim

    AdmixSim can be either downloaded directly from the release from the URL: https://github.com/Shuhua-Group/AdmixSim/releases or cloned from github repository by

         git clone https://github.com/Shuhua-Group/AdmixSim.git


2. Get command line help

        java -jar AdmixSim.jar -h  or java -jar AdmixSim.jar --help

2. Run it with toy data

        java -jar AdmixSim.jar --gen 10 --nanc 2 --leng 1.0 --file toy.par --samp 20 --input test --output test_out

### Input and output

1. Input files

    1). model description file

    firstly, in model description file, set up initial number of haplotypes to be sampled for parental populations

    secondly, set up the population size and parental proportions for each generation in one line

    Note: anything follow "#" was treated as comments

    Here is a complete example: 

        #set up number of haplotypes in each parental population to be sampled from
        10	 10
        // #indicate start of population size and parental proportions for each generation
        100	0.5	0.5 #init population, two parental population, each contribute 50%
        100	0	0
        100	0.1	0 #second wave admixture, with 10% gene flow from parental population 1
        100	0	0
        100	0	0
        100	0	0.2 #third wave admixture, with 20% gene flow from parental population 2
        100	0	0
        150	0	0 #population increase to 150
        100	0	0 #population size decrease to 100
        100	0	0

    It's quite simple to implemented HI, GA or CGFR or CGFD models as described in Jin Wenfei et al (2012) AJHG.

    HI model:

        10 10
        //
        100	0.7	0.3 #init population, two parental population, contribute 70% and 30%
        100	0	0
        ......
        100	0	0

    GA model:

        10 10
        //
        100	0.7	0.3 #init population, two parental population, contribute 70% and 30%
        100	0.07	0.01
        ......
        100	0.07	0.01

    CGFD(First parental population as gene flow donor) model:

        10 10
        //
        100	0.1	0.9 #init population, two parental population, contribute 10% and 90%
        100	0.1	0
        ......
        100	0.1	0

    CGFR(First parental population as gene flow recipient) model:

        10 10
        //
        100	0.9	0.1 #init population, two parental population, contribute 90% and 10%
        100	0	0.1
        ......
        100	0	0.1

    2). Map file

    The genetic positions for each marker are given in Morgan, one line per marker.

    Here is an example:

        0.00097100
        0.00238066
        0.00367538
        ......

    3). Haplotype file

    The haplotypes of parental populations to be sampled from are combined in one file, one haplotype per line. The first n1 lines correspond to haplotypes for first parental population, second n2 lines correspond to haplotypes for second parental population and so on. In which the number of parental populations and the number of haplotypes for each parental population ((n1, n2 and so on) are given in model description file.

    Here is an example:

        1011000000100100001000000010110100010101000011010111000000000010000000011101100000000101000010010010
        0010000010000100001000000010110101010011000001011111001000000010000000011001100000010101000010110010
        0010000010000100001000000010110101010001010001010111000000100011000000011001110010000101000010010010
        0010000010000100001010000010110101010001000001010111000000100010000000011001110010000101000010010010
        ......
        0100000000000110100000010000000100010000001000000001010001000010000101001011000100001100000111000100

2. Output file

    1). Admixed haplotypes

    Record the haplotypes from the individuals sampled from the admixed population, format is the same as input haplotype file

    2). Segment file

    In which record the start, end and which parental that segment comes from. Each line corresponds to one chromosomal segment.
 
    Here is an example:

         0.00000000      0.07785695      2
         0.07785695      0.30178126      1
         ......
         0.30178126      0.41594482      2

### Complete arguments list

        -h/--help       print help message [optional]
        -f/--file       model description file [required]
        -i/--input      prefix of input files [required]
        -g/--gen        generations since admixture [optional, default: 1]
        -k/--nanc       number of parental populations [optional, default: 2]
        -l/--leng       length of chromosome to be simulated [optional, default: 1.0]
        -n/--samp       number of individual(s) to be sampled [optional, default: 10]
        -o/--output     prefix of output files [optional, default: output]
        -s/--seed       seed of random generator [optional, default: current time]

### Couple with MS
  It's very easy to couple with a coalescent simulator MS. For example, use MS to simulate two parental populations, whose Ne remains constant, i.e. Ne=5000, split 4000 generations ago, command as below:

        ms 200 1 -t 2000 -r 2000 10000000 -I 2 100 100 -ej 0.2 2 1 -p 8 > simAnc.txt

  Then just simply convert into the files needed in the admixture model simulator:

        python convert.py simAnc.txt simAnc 200
  
  It will produce the map file and the parental haplotype file:

        simAnc.map simAnc.hap

  Afterwards, the simulated parental haplotypes could be used in the admixture model simulator, for example:

        java -Xmx2g -jar AdmixSim.jar -g 20 -k 2 -l 2 -f sim1.par -n 100 -i simAnc -o sim1

Notes: the software is free and open source, users are at their own risk and without any guarantee. 

Details can be also found in wiki page : https://github.com/Shuhua-Group/AdmixSim/wiki

### Citation
When using ```AdmixSim```, please cite

Yang X, Yuan K, Ni X, Zhou Y, Guo W, Xu S. AdmixSim: A Forward-Time Simulator for Various Complex Scenarios of Population Admixture. Front Genet. 2020 Dec 3;11:601439. doi: 10.3389/fgene.2020.601439. PMID: 33343638; PMCID: PMC7744625.

(https://www.frontiersin.org/articles/10.3389/fgene.2020.601439/full)
