import numpy as np

A = np.array([[2, 2, 1, 0, 1],
              [2, 0, 1, 0, 0],
              [1, 1, 0, 1, 2],
              [0, 0, 1, 0, 2],
              [1, 0, 2, 2, 0]])

print(A@A@A@A)