This program compresses text files and images using the Huffman compression algorithm, created by David A. Huffman.

***INSTRUCTIONS***
Run HuffMain.java
Under the 'Compress' tab, press on the red 'no file chosen' text to choose a file to compress.
You can compress any file, but .txt files and .tif files are the orignal uncompressed files.
Press 'Compress' to compress.

Under the 'Decompress' tab, you can decompress .hf files, in the same way as above.

Under the compare tab, you can compare the sameness of two files. .compressed and .hf files should be the same
bit for bit, and .dehf files and .txt or .tif files should be the same.

Do not use the 'Test' tab, it compresses entire directories, but it screws up at the very last bit. I didn't write
this function.
***

Name: Alex Boldt
NetID: apb34
Hours Spent: 3
Consulted With: NONE
Resources Used: NONE
Impressions: Pretty easy (the easiest of the year for me) yet very interesting because we are coding the first iteration
of a very important algorithm that is used constantly in today's world, and I appreciate knowing more about it.
----------------------------------------------------------------------
Problem 1: Benchmark and analyze your code

Calgary: 14.945s 
Waterloo color: 74.503s
Waterloo grayscale1: 3.957s
Waterloo grayscale2: 19.126s

Compression rate Hypothesis:
I believe that compression rate in independent of size of the file. If C is the size of the alphabet of the file,
then the tree header of the compressed file will be 11C-1 bits long (9+1 bits per character and 1 bit per internal
node, of which there are C-1 nodes, 10C + (C - 1) = 11C-1). 

I believe that compression rate will be faster as the alphabet of the file is smaller. 

I think that compression rate will become slower as frequency of characters become more equivalent, while compression
rate is faster for files with more varied frequency. To test these hypotheses, I created three test text files, 
test1.txt, test2.txt, and test3.txt. 

test1.txt is the characters [a-z] and \n. Repeated for 5000 lines, all characters with the exact same frequency.
test2.txt is the characters [a-z] with 'a' having the highest frequency and going down to 'z' with the lowest
frequency. 
test3.txt is just the character 'a' and the newline repeated over and over again. All 3 files have the same 
size (530kB). 

Average run times over 10 runs:
test1: 2.6863s   39.50%  compression 
test2: 2.5003s   46.28%  compression
test3: 0.6775s   87.14%  compression

It seems that test1, which will have a balanced Huffman tree due to the equivalence in frequencies, runs the
slowest due to the longer codes of each character. It also compresses less compared to test2.txt as a consequence.
Because of test2.txt's unbalanced tree, the most common characters have shorter codes which will allow for quicker
compression of test1.txt's equivalent characters. Test3.txt's tiny tree has a short codes which compress quickly
and with high efficiency. 

So in conclusion, smaller, more varied alphabets lead to faster compression rates. 

Problem 2: Text vs. Binary 

Calgary: 43.76% 
Waterloo color: 18.18%
Waterloo grayscale1: 39.83%
Waterloo grayscale2: 18.54%

Text files seem to compress much more. Files compress less if they have longer Huffman codes on average, or 
larger Huffman trees. Images are going to have larger trees and larger 'alphabets' because they use more of the
256 8-bit numbers available to them (in order to obtain all of the shades of colors), while most text files don't 
use many of the ASCII text encoding characters afforded to them. So because text files have smaller alphabets
and trees, they will compress more efficiently because they have shorter Huffman codes on average.


Problem 3: Compressing compressed files

First Compression of kjv10: 42.72%
Second:                     1.51%

First Melville: 43.62%
Second:          0.65%              

First lena: 2.68%
Second:    -0.07%  wow!



