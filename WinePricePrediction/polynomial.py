import pandas as pd 
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import PolynomialFeatures
from sklearn.linear_model import LinearRegression

import warnings
warnings.filterwarnings('ignore')

import sys

# Debugging
# sys.stdout = open("tmp/output.txt",'w')

## Inputs
wineToBePriced = 'Cabernet Sauvignon'
ratingGiven = 84
origin = "France"
df = pd.read_csv('dataset/winemag-data-130k-v2.csv', encoding='utf-8', usecols=['price', 'points', 'variety','country']).dropna()

# Input Validation
if wineToBePriced not in df.variety.unique():
    print("Data is not available for : ", wineToBePriced)
    sys.exit()
elif origin not in df.country.unique():
    print("Data is not available for : ", origin)
    sys.exit()

# Filtering relevant data
currDf = df.loc[ (df['variety'] == wineToBePriced) & (df['country'] == origin) ].sort_values(['points','price'])

print("Description of available data\n",currDf.describe())
choice = input("Continue? Y/n : ")
if choice != "Y":
    sys.exit()

# Training the dataset
points_x = currDf[["points"]]
prices_y = currDf["price"]

X_train, X_test, y_train, y_test = train_test_split(points_x, prices_y, test_size=0.25, random_state=0)

poly_reg = PolynomialFeatures(degree=3)
X_poly = poly_reg.fit_transform(points_x)
pol_reg = LinearRegression()
pol_reg.fit(X_poly, prices_y)

# Prediction
predicted_y = pol_reg.predict(poly_reg.fit_transform([[ratingGiven]]))
print('Recommended price: ', predicted_y)

# Viz
plt.scatter(points_x, prices_y, color='red')
plt.plot(points_x, pol_reg.predict(poly_reg.fit_transform(points_x)), color='blue')
plt.title('Polynomial model')
plt.xlabel('Points/Ratings from Taster')
plt.ylabel('Price')
plt.show()
