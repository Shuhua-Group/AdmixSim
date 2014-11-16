'''
Name: mssim2eigen.py
Date: 2014-8-25
Version: 1.01
Author: Young
Description:
    Convert the output of MS simulation into eigenstrat format, only deal with
    1 repeat siutation
Input file: the output of MS simulation
Output files: prefix.ind prefix.snp prefix.geno
Arguments:
    -h --help               print help
    -f --file filename      name of MS output file [string]
    -n --npops n,n1,n2 ...  #subpopulations and #samples [integer]
    -l --length L           length of simulated sequence [integer]
    -p --prefix prefix      prefix of output files [string]
'''
import sys,random, getopt

def gen_allele():
    return random.sample('AGCT',2)

def gen_ind(npop,ninds,prefix):
    indfile='{}.ind'.format(prefix)
    with open(indfile, 'w') as out:
        i=1
        for nind in ninds:
            for k in range(nind):
                out.write('SAM{}_{}\tU\tPOP{}\n'.format(i,k+1,i))
            i+=1
    print('Write indfile into {}'.format(indfile))

def gen_snp(posline,L,prefix):
    snpfile='{}.snp'.format(prefix)
    with open(snpfile, 'w') as out:
        gp=posline.split()[1:]
        i=1
        scale = L*1.0e-8    #Assume 1 Morgan = 100Mb
        for g in gp:
            pp=int(float(g)*L)
            a1,a2=gen_allele()
            gp=float(g)*scale
            out.write('rs{}\t1\t{:.8f}\t{}\t{}\t{}\n'.format(i,gp,pp,a1,a2))
            i+=1
    print('Write snpfile into {}'.format(snpfile))    

def gen(simfile, npop=1, ninds=None, L=1e7, prefix='sim'):
    gen_ind(npop, ninds, prefix)
    genofile='{}.geno'.format(prefix)
    with open(simfile) as f,open(genofile, 'w') as gf:
        for i in range(5):
            f.readline()    #skip 5 line
        posline=f.readline()    #posline
        gen_snp(posline, L, prefix)
        tm=[]   #temp matrix
        line=f.readline()
        while line:
            h1=[]
            for c in line[:-1]:
                h1.append(int(c))
            i=0
            line2=f.readline()
            for c in line2[:-1]:
                h1[i]+=int(c)
                i+=1
            tm.append(h1)
            line=f.readline()
        for k in range(len(tm[0])):
            ost=''
            for m in range(len(tm)):
                ost+=repr(tm[m][k])
            ost+='\n'
            gf.write(ost)
    print('write genofile into {}'.format(genofile))

def usage():
    print('''Description:
    Convert the output of MS simulation into eigenstrat format
    Note: currently only deal with 1 repeat siutation
Input file: the output of MS simulation
Output files: prefix.ind prefix.snp prefix.geno
Arguments:
    -h --help               print help
    -f --file filename      name of MS output file [string]
    -n --npops n,n1,n2 ...  #subpopulations and #samples [integer]
    -l --length L           length of simulated sequence [integer]
    -p --prefix prefix      prefix of output files [string]
''')

def main():
    f=''
    n=1
    ns=[]
    l=1e7
    p='sim'
    try:
        opts, args = getopt.getopt(sys.argv[1:], 'hf:n:l:p:', 
        ['help','file','npops','length','prefix'])
    except getopt.GetoptError as err:
        print(err)
        usage()
        sys.exit(2)
    for o, a in opts:
        if o in ('-h', '--help'):
            usage()
            sys.exit(1)
        elif o in ('-f', '--file'):
            f=a
        elif o in ('-n', '--npops'):
            lst=a.split(',')
            n=int(lst[0])
            for k in lst[1:]:
                ns.append(int(k))
        elif o in ('-l', '--length'):
            l=int(a)
        elif o in ('-p', '--prefix'):
            p=a
    print(f,n,ns,l,p)
    assert (len(ns) == n), 'the number of populations are not equal'
    assert (len(f) > 0), 'Input file is empty'
    gen(f, n, ns, l, p)    

if __name__=='__main__':
    main()
                   
