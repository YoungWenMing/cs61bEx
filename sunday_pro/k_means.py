#apply k-means algorithm and visualize the outcome

import numpy as np
import matplotlib.pyplot as plt
import sklearn.datasets as ds
import matplotlib.colors
from sklearn.cluster import KMeans

#generate random data

point_number = 3000
centers = 20
data, laber = ds.make_blobs(points_number, centers = centers, 			random_state = 0)

#construct figures, an empty figure will be generated and functions as
#the black board to draw
fig = plt.figure()

#to display labels in Chinese
matplotlib.rcParams["fon.sans-serif"] = [u'SimHei']

#to display minus symbol
matplotlib.rcParams["axes.unicode_minus"] = False

#visualize the raw data
ax1 = fig.add_subplot(211) 		#what does 211 do?
plt.scatter(data[:, 0], data[:, 1], c = laber)
plt.title(u'原始数据分布')
plt.sca(ax1)

#visualize the data after classification
N = 5  				#divide the data into 5 groups
model = KMeans(n_clusters = N, init = "k-means++")			#build a #model of k-means algorithm
y_pre = model.fit_predict(data)
ax2 = fig.add_subplot(212)
plt.scatter(data[:, 0], data[:, 1], c = y_pre)
plt.title(u'K-Means聚类')
plt.sca(ax2)

plt.show()
