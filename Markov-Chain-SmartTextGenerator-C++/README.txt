Name: Kavin Jeyasankar

I did not get any help from anyone during this project.

The biggest challenge I had with this assignment was purely understanding what was being asked. I spent maybe 2 
hours just watching videos and trying to understand what the markov chain actually was and how this could be
used to generate random text. However, after this initial hurdle, it was fairly straightforward for the
implimentation of markov chains. Another problem I have was getting the MAP MODEL to initialize in a reasonable
time. Right now it takes about 30-45 seconds for initialization (however after initialization, the generation
process takes barely any time). But, my WORD MODEL initializes very quickly even though it seems like both
of them are implimented in a similar manner. I could not fix the problem with the long initialization time in
my MAP MODEL but I suspect it is string.find() that is causing it to be as slow as it is because I did not use
that in my WORD MODEL. My MAP MODEL used to take 50-65 seconds to initialize but I cut it down to about 30 seconds
by using a set instead of a vector to hold everything.

I liked the concept behind this project and how markov chains are used for machine learning. I also liked
how it was fairly easy to implement (after getting over the initial hurdle of understanding the assignment).

I spend about 18 hours completing this project.

