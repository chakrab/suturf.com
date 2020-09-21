import cv2
from os import listdir
from shutil import copy2


class ImageResizer:
    def __init__(self, sdir, odir="", mdim=500.0):
        self.srcdir = sdir
        self.outdir = odir
        self.mindim = mdim

    def start_resize(self):
        for fln in listdir(self.srcdir):
            # Get File Size
            img = cv2.imread(self.srcdir + fln, cv2.IMREAD_UNCHANGED)
            wd, ht, _ = img.shape
            print("{} -> ({}, {})".format(fln, wd, ht))
            if wd > self.mindim and ht > self.mindim:
                # Need Resize
                scl_x = self.mindim/wd
                scl_y = self.mindim/ht
                scale = scl_x if scl_x > scl_y else scl_y
                print("\tWill resize, scale {}".format(scale))
                imgr = cv2.resize(img, (0, 0), fx=scale, fy=scale,
                                  interpolation=cv2.INTER_NEAREST)
                if self.outdir != "":
                    cv2.imwrite(self.outdir + fln, imgr)
            else:
                print("\tWill not resize...")
                if self.outdir != "":
                    copy2(self.srcdir + fln, self.outdir + fln)


if __name__ == "__main__":
    irs = ImageResizer("../resources/trainimg/", "../resources/scaleimg/")
    irs.start_resize()
