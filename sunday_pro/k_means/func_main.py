#this is a script to implement the k-means algorithm
"""
to implement that algorithm, the following steps are needed:
	1.generate a series of nodes inside a specific area randomly;
	2.pick K temporary central points at random;
	3.calculate the distances from each node to those centers;
	4.cluster all the nodes according to the shortest distance and 		  thus get K clusters;
	5.calculate the average position of each cluster, and make them 
	  new centers;
	6.if new centers are identical to the old ones, stop, go back to 	   step 3 otherwise. 
"""

from numpy import *
#import matplotlib.pyplot as plt

def createRanData(num):
	"""randomly create a data set with num points in it"""
	return random.uniform(0, 2, (num, 2))

def distEclud(vectorA, vectorB):
	"""to calculate the distance between two vectors"""
	return sqrt(sum(power(vectorA - vectorB, 2)))

def initCentroid(dataset, k):
	"""to generate k point as the original centroids"""
	n = shape(dataset)[1]		#get the dimensions
	centroids = mat(zeros((k, n)))
	for j in range(n):
		minJ = min(dataset[:, j])
		rangeJ = float(max(dataset[:, j]) - minJ)
		centroids[:, j] = minJ + rangeJ * random.rand(k, 1)
	return centroids

def averCent(dataset):
	"""calculate the average centroid of one cluster."""
	n = shape(dataset)[1]
	center = []
	for j in range(n):
		center.append(average(dataset[:,j]))
	return array(center)

class Cluster(object):
	"""a cluster has attributes of centroid and points"""
	def __init__(self, label, centroid, pointSet):
		"""label is the sequence number
			pointSet is a array of points belong to this cluster. 
		"""
		self.label = label
		self.centroid = centroid
		self.pointSet = pointSet

	def pointReset(self):
		"""reset the points set"""
		self.pointSet = []

	def pointAdd(self, point):
		"""add a point to the pointSet"""
		self.pointSet.append(point)

	@property
	def newCenter(self):
		"""calculate the average position of all the points within
		the pointSet		
		"""
		return averCent(array(pointSet))

"""
what to do next is to figure out how to arrange the processes,
including update centroid and pointSet of each cluster
"""






