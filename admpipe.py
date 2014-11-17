import os, sys, multiprocessing

'''
Name: admpipe.py
Description:
    Run ADMIXTURE analysis automatically in multiprocessing way, with 10 fold
    cross validation error,and plot the results automatically.
Input: the genofile in eigenstrat prefix.geno format
Output: prefix.k.P prefix.k.Q prefix_k.log prefix_adm.pdf
Arguments:
    prefix: the prefix for input and output file
    maxk: the max K to run ADMIXTURE
'''

def write_plot(prefix, maxk):
    outf = '{}_adm.r'.format(prefix)
    with open(outf, 'w') as out:
        out.write('pdf("{}_adm.pdf", width=8, height=4)\n'.format(prefix))
        for k in range(2,maxk+1):
            out.write('d<-read.table("{}.{}.Q")\n'.format(prefix, k))
            out.write('barplot(t(as.matrix(d)), col=1:{}, space=0, border=NA, ylab="Ancestry")\n'.format(k))
        out.write('dev.off()\n')
    print('Write plot script to {}'.format(outf))

def worker(prefix, k):
    cmd = 'admixture --cv=10 {0}.geno {1} > {0}_{1}.log'.format(prefix,k)
    print('job-{}: {}'.format(k,cmd))
    os.system(cmd)
    
def run_adm(prefix, maxk=2):
    jobs=[]
    for k in range(1, maxk+1):
        p = multiprocessing.Process(name='job-'+repr(k),target=worker, args=(prefix,k))
        jobs.append(p)
        p.start()
    for j in jobs:
        j.join()
        print('{}: Finished'.format(j.name))
    write_plot(prefix, maxk)
    os.system('Rscript {}_adm.r'.format(prefix))
    print('Find the results in {}_adm.pdf'.format(prefix))
    print('Please referrence to the cross validation error below:')
    os.system('grep CV {}_*.log'.format(prefix))
    
if __name__=='__main__':
    if len(sys.argv)>2:
        maxk = int(sys.argv[2])
        run_adm(sys.argv[1], maxk)
    elif len(sys.argv)>1:
        run_adm(sys.argv[1])
    else:
        print('Usage: python {} <prefix> [maxk]'.format(sys.argv[0]))
