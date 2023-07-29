# Discriminative-feedback-Complete
the code is in branch master

This project is based on the problem of learning with explanations that was first presented in the paper “Learning from
discriminative feature feedback” which was published at NeurIPS 2018. You can get it here: 
https://papers.nips.cc/paper/2018/file/36ac8e558ac7690b6f44e2cb5ef93322-Paper.pdf
in the first part of the project we implemented the algorithm in the above paper which takes as input a legal data set and a teacher type and runs the learning
algorithm on the data set while interacting with the teacher, also Implemented teacher types that the algorithm can be used with. The teacher first gets the data set
as input, and it can use it for preprocessing. Then, whenever the algorithm makes a prediction yˆt on an example xt with an example xˆ as an explanation, the teacher returns the correct prediction yt based on the data set, and if the algorithm’s prediction was wrong, the teacher provides a simple
discriminative feature ϕ for (xt, yt, xˆt, yˆt).  we have two teacher types:
1. A teacher that returns a simple discriminative feature which is selected uniformly at random out of all
the simple discriminative feature for (xt, yt, xˆt, yˆt).
2. A teacher that returns the “most discriminative feature” out of all the simple discriminative features
for (xt, yt, xˆt, yˆt). The “most discriminative feature” is calculated as follows: For every simple discriminative feature ϕ for (xt, yt, xˆt, yˆt), calculate the percentage pˆϕ of examples with label yˆt that
satisfy this feature and the percentage pϕ of examples with label yt that satisfy this feature. Select the
feature ϕ such that the difference |pˆϕ − pϕ| is the largest.

For the second part of this project, we improved the algorithm 
There were several ways we tried to improve the algorithm with 
The first way is Rule choosing mechanism : 
- First Rule method
- Last Rule method
- Most complicated Rule Method
- Simplest Rule method 
- Random Rule method

The second way is list ordering :
- Lazy furthest data
- shuffle list

 we saw the difference between Rule choosing mechanisms is not that significant but the real difference is in the list ordering between lazy furthest and shuffle
According to the various options we have tried the best variation is :
- Rule choosing mechanism: First rule  and List Ordering: Lazy furthest data Combined with the most discriminative teacher

  
