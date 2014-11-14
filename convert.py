import os,sys
def conv(msoutput,prefix,nhap):
    os.system('tail -{} {} > {}.hap'.format(nhap, msoutput, prefix))
    mapfile='{}.map'.format(prefix)
    with open(msoutput) as f, open(mapfile, 'w') as out:
        for i in range(5):
            f.readline()#skip first five line
        for pos in f.readline().split()[1:]:
            out.write('{}\n'.format(pos))
    print('please check file in {0}.hap and {0}.map'.format(prefix))

if __name__=='__main__':
    if len(sys.argv)>3:
        conv(sys.argv[1], sys.argv[2], int(sys.argv[3]))
    else:
        print('Usage: python {} input prefix nhap'.format(sys.argv[0]))

    
