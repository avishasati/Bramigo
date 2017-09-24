from __future__ import division
import sys
import numpy as np
from sklearn import linear_model

'''
Use this script to train a binary classification model.
Usage: python -u train_model.py path/to/input_data
'''

if __name__ == '__main__':
	# X will contain all the feature vectors
	X = []
	# y contains labels
	y = []
	input_data = sys.argv[1]
	with open(input_data) as fin:
		for line in fin:
			l = list(map(float, line.split()))
			y.append([l[0]])
			X.append(l[1:])
	# converting to numpy arrays for performance
	X = np.array(X)
	y = np.array(y)
	assert len(X) == len(y)
	print(X.shape)
	print(y.shape)
	# Using 90% data for training
	X_train = X[:int(0.9*len(X))]
	y_train = y[:int(0.9*len(y))]
	# Using remaining 10% data for testing
	X_test = X[int(0.9*len(X)):]
	y_test = y[int(0.9*len(X)):]
	# Using a logistic regression classifier
	logistic = linear_model.LogisticRegression()
	logistic.fit(X_train, y_train)
	print(logistic.score(X_test, y_test))