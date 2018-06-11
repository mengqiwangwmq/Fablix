import csv
import sys

if len(sys.argv) < 2:
    print("Please input the file name.")
else:
    f = open(sys.argv[1])
    nums = csv.reader(f)
    nums = list(map(lambda x: int(x[0]), nums))
    avg = sum(nums) / len(nums)
    print("The average number in " + sys.argv[1] + " is: " + str(avg))
