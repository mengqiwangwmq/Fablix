import csv
import sys

if len(sys.argv) < 2:
    print("Please input the file name.")
else:
    f=[]
    Nums=[]
    for i in range(1,len(sys.argv)):
        f.append(open(sys.argv[i]))
        nums = csv.reader(f[i-1])
        Nums += list(map(lambda x: int(x[0]), nums))
    avg = sum(Nums) / len(Nums)
    print("The average number in " + sys.argv[1] + " is: " + str(avg))
