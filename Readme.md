The script file is the /script.py. In the command line, type: `python script.py <filename of TS or TJ>`, or `python3 script.py <filename of TS or TJ>`.
E.g. `python3 script.py WebContent/single-instance-https-10-full/TS` will give the average of numbers in `WebContent/single-instance-https-10-full/TS`
For scaled cases, there are two TS/TJ files, one for master and the other for slave. Just set both of the two files as arguments to calculate the total average:
`python3 script.py WebContent/scaled-servers-http-10-no-pooling/slave/TJ_NP WebContent/scaled-servers-http-10-no-pooling/master/TJ_NP`