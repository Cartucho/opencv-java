import numpy as np
import cv2

print ''' Simple Linear Blender
------------------------------------------
Enter value of alpha [0:1] :'''

alpha = float(input())                 # Ask the value of alpha

if 0<=alpha<=1:                        # Check if 0<= alpha <=1
    beta = 1.0 - alpha                 # Calculate beta = 1 - alpha
    gamma = 0.0                        # parameter gamma = 0

    src1 = cv2.imread('../../images/LinuxLogo.jpg')
    src2 = cv2.imread('../../images/WindowsLogo.jpg')

    if src1==None:
        print "src1 not ready"
    elif src2==None:
        print "src2 not ready"
    else:
        dst = cv2.addWeighted(src1,alpha,src2,beta,gamma)  # Get weighted sum of img1 and img2
        # Numpy version of above line. But cv2 function is around 2x faster:
        #dst = np.uint8(alpha*(img1)+beta*(img2))

        cv2.imshow('dst',dst)
        cv2.waitKey(0)
        cv2.destroyAllWindows()
else:
    print "value of alpha should be 0 and 1"
