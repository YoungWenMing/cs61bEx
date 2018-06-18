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
import matplotlib.pyplot as plt

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
	return array(centroids)

def averCent(dataset):
	"""calculate the average centroid of one cluster."""
	n = shape(dataset)[1]
	center = []
	for j in range(n):
		center.append(average(dataset[:,j]))
	return array(center)

def cluster_decide(clusters, point):
	"""
	to determine to which cluster the point belong by distance
	"""
	A = clusters[0]
	d = A.distance2center(point)
	for i in range(1, len(clusters)):
		l = clusters[i].distance2center(point)
		if l < d:
			A = clusters[i]
			d = l
	return A




class Cluster(object):
	"""
	a cluster has attributes of centroid and points
	>>> A = Cluster(1, array([1,1]))
	>>> A.label
	1
	>>> A.
	"""
	def __init__(self, label, centroid, pointSet=[]):
		"""label is the sequence number
			centroid is an array
			pointSet is a array of points belong to this cluster. 
		"""
		self.label = label
		self.centroid = centroid
		self.pointSet = pointSet

	def __repr__(self):
		return "Cluster, labeled " + str(self.label) \
				+ "with centroid " + str(self.centroid)

	def pointReset(self):
		"""reset the points set"""
		self.pointSet = []

	def pointAdd(self, point):
		"""add a point to the pointSet"""
		self.pointSet.append(point)

	def distance2center(self, point):
		"""to calculate the distance from the point to this center"""
		return distEclud(self.centroid, point)

	
	@property
	def setwithLabel(self):
		"""
		add the label to the end of point set
		"""
		sets = array(self.pointSet)
		pos = shape(sets)[1]
		return insert(sets, pos, self.label, axis = 1)



	@property
	def newCenter(self):
		"""calculate the average position of all the points within
		the pointSet		
		"""
		return averCent(array(self.pointSet))

#-----to visualize the result

def linkcluster(clusters):
	"""
	concatenate all the clusters' point sets together
	in convenience of ploting
	"""
	firstMat = mat(array(clusters[0].setwithLabel))
	for i in range(1, len(clusters)):
		firstMat = concatenate([firstMat, \
				mat(array(clusters[i].setwithLabel))], axis = 0)
	return array(firstMat)



def to_plot(pointset):
	"""
	plot a scatter figure for the result
	"""
	
	plt.figure(figsize=(8,5))
	plt.scatter(pointset[:,0], pointset[:,1], c = pointset[:,2], marker = 'o')
	plt.grid(True)
	plt.xlabel('1st')
	plt.ylabel('2nd')
	plt.title('ttt')
	#plt.show()

def get_centers(clusters):
	"""
	draw the centroids of those clusters
	"""
	centersM = mat(clusters[0].centroid)
	for i in range(1, len(clusters)):
		centersM = concatenate([centersM, mat(clusters[i].centroid)],
				axis = 0)			
	return array(centersM)


#"""
if __name__ == '__main__':
	k = 4
	raw_set = createRanData(1000)
	centroids = initCentroid(raw_set, k)
	clusters = []
	for i in range(4):
		clusters.append(Cluster(i, centroids[i]))

	change= True
	while change:
		change = False
		for cluster in clusters:
			#reset the set to get new point in 
			cluster.pointReset()

		for point in raw_set:
			cluster_decide(clusters, point).pointAdd(point)

		for cluster in clusters:
			if not cluster.pointSet:
				continue
			if not (cluster.newCenter != cluster.centroid).all():   #check every element pair
				cluster.centroid = cluster.newCenter
				change = True
	#for cluster in clusters:
	#	print(cluster.pointSet)
	#	print("\n next cluster \n")

	dataplot = linkcluster(clusters)
	to_plot(dataplot)
	cen = get_centers(clusters)
	plt.scatter(cen[:,0], cen[:,1],marker = "*")
	plt.show()

#"""

"""
what to do next is to figure out how to arrange the processes,
including update centroid and pointSet of each cluster

basic functions are completed, what are supposed to be done
include visualization and completing some details
"""






