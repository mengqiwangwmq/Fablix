import csv
import sys

if len(sys.argv) < 2:
    print("Please input the file name.")

f = open(sys.argv[1])
nums = csv.reader(f)
nums = list(map(lambda x: int(x), nums))
avg = sum(nums) / len(nums)
print("The average number in " + sys.argv[1] + " is: " + str(avg))
