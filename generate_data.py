from __future__ import division
import time
import sys
import random
import numpy as np

'''
This python script is used to generate the synthetic data set.
See the ppt slides for details on how the data is modeled.
Use this data to train an ML classification model.
Usage: python -u generate_data.py number_of_data_points /path/to/save/data/file
Example: python -u generate_data.py 100000 /Users/adityamakkar/Desktop/input_data
'''

def fun(n):
	return n**2

if __name__ == '__main__':
	# N data points.
	N = int(sys.argv[1])
	# path where data will be saved
	output_path = sys.argv[2]
	# C[i] is the class for ith data point.
	# C[i] = 0 if healthy, 1 if ill.
	C = []
	# features[i] is a feature vector of length 11 for ith data point.
	# features[i][0] is age
	# features[i][1] is BMI
	# features[i][2] is systolic BP
	# features[i][3] is diastolic BP
	# features[i][4] is 1 if heart rate inactive, 0 otherwise
	# features[i][5] is 1 if heart rate normal, 0 otherwise
	# features[i][6] is 1 if heart rate active, 0 otherwise
	# features[i][7] is body temperature
	# features[i][8] is 1 if hypoactive muscle activity, 0 otherwise
	# features[i][9] is 1 if normal muscle activity, 0 otherwise
	# features[i][10] is 1 if hyperactive muscle activity, 0 otherwise
	features = []
	for i in range(N):
		state = random.random()
		# 0.9 probability of being healthy
		healthy = state > 0.9
		f = [0.0] * 11
		# Age
		f[0] = random.gauss(22, 10) if healthy else random.gauss(35, 10)
		f[0] = max(13, f[0])
		f[0] = min(f[0], 70)
		# BMI
		f[1] = random.gauss(22, 3) if healthy else random.gauss(27, 3)
		f[1] = max(15, f[1])
		f[1] = min(35, f[1])
		# Systolic BP
		f[2] = random.gauss(120, 10) if healthy else random.gauss(140, 10)
		f[2] = max(60, f[2])
		f[2] = min(180, f[2])
		# Diastolic BP
		f[3] = random.gauss(80, 5) if healthy else random.gauss(90, 5)
		f[3] = max(40, f[3])
		f[3] = min(120, f[3])
		# Heart rate
		heart_state = random.random()
		f[4] = 1 if (healthy and heart_state < 0.2) or ((not healthy) and heart_state < 0.4) else 0
		f[5] = 1 if (healthy and 0.2 < heart_state < 0.8) or ((not healthy) and 0.4 < heart_state < 0.6) else 0
		f[6] = 1 if (healthy and heart_state > 0.8) or ((not healthy) and heart_state > 0.6) else 0
		# Body temperature
		f[7] = random.gauss(98.13, 0.6) if healthy else random.gauss(98.38, 0.6)
		f[7] = max(95, f[7])
		f[7] = min(106, f[7])
		# Muscle activity
		muscle_state = random.random()
		f[8] = 1 if (healthy and muscle_state < 0.2) or ((not healthy) and muscle_state < 0.4) else 0
		f[9] = 1 if (healthy and 0.2 < muscle_state < 0.8) or ((not healthy) and 0.4 < muscle_state < 0.6) else 0
		f[10] = 1 if (healthy and muscle_state > 0.8) or ((not healthy) and muscle_state > 0.6) else 0
		# Appending health and features
		C.append(0) if healthy else C.append(1)
		features.append(f)
	# Printing to file
	with open(output_path, 'w') as fout:
		for i in range(N):
			fout.write(str(C[i]))
			for f in features[i]:
				fout.write(' ' + str(f))
			fout.write('\n')
