import sys,os
def write_par(prefix):
    parf='{}.par'.format(prefix)
    with open(parf, 'w') as out:
        out.write('genotypename: {}.geno\n'.format(prefix))
        out.write('snpname: {}.snp\n'.format(prefix))
        out.write('indivname: {}.ind\n'.format(prefix))
        out.write('evecoutname: {}.evec\n'.format(prefix))
        out.write('evaloutname: {}.eval\n'.format(prefix))
        out.write('altnormstyle: NO\n')
        out.write('numoutlieriter: 0\n')
    print('write PCA parameter file into {}'.format(parf))

def write_plot(prefix):
    rscf='{}.r'.format(prefix)
    with open(rscf, 'w') as out:
        out.write('library(ggplot2)\n')
        out.write('eval<-read.table("{}.eval")\n'.format(prefix))
        out.write('xl<-paste("PC1(",round(eval$V1[1],digits=2),"%)",sep="")\n')
        out.write('yl<-paste("PC2(",round(eval$V1[2],digits=2),"%)",sep="")\n')
        out.write('evec<-read.table("{}.evec",skip=1)\n'.format(prefix))
        out.write('pdf("{}_pca.pdf")\n'.format(prefix))
        out.write('qplot(V2,V3,xlab=xl,ylab=yl,data=evec,lwd=4,color=V12)\n')
        out.write('dev.off()\n')
    print('write plot PCA script to {}.r\n'.format(prefix))
    
def pca(prefix):
    write_par(prefix)
    os.system('smartpca -p {}.par > {}_pca.log'.format(prefix,prefix))
    write_plot(prefix)
    os.system('Rscript {}.r'.format(prefix))
    print('Finished the PCA pipeline, please check results in {}_pca.pdf'.format(prefix))

if __name__=='__main__':
    if len(sys.argv)>1:
        pca(sys.argv[1])
    else:
        print('Usage: python {} prefix'.format(sys.argv[0]))
