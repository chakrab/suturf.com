import os
import random
from PIL import Image

from pxldict import PixelDictionary
from spclchars import SpecialChars


class HideText:
    encode_key = 1456

    def __init__(self) -> None:
        # Let's create the dictionary
        random.seed(self.encode_key)
        px = PixelDictionary(self.encode_key)
        self.pxlencdict, self.pxldecdict = px.create_dict()

    def point1d2d(self, pnt, width):
        row_num = int(pnt / width)
        col_num = int(pnt - row_num*width)
        return col_num, row_num

    def encode_file(self, txt, infile, outfile):
        im = Image.open(infile)
        im_width, im_height = im.size
        px_count = im_height * im_width
        # Get a Random point to start out our text
        rjmp = int(random.randrange(0, px_count - len(txt) - 2))

        # Now just change the text
        px = im.load()
        ltxt = list(txt)
        px[self.point1d2d(rjmp, im_width)] = self.pxlencdict.get(SpecialChars.TEXT_START.value)
        inc = rjmp + 1
        for x in range(len(txt)):
            ccode = ord(ltxt[inc-rjmp-1])
            if ccode in self.pxlencdict: 
                px[self.point1d2d(inc, im_width)] = self.pxlencdict.get(ccode)
            elif ccode == 32:
                px[self.point1d2d(inc, im_width)] = self.pxlencdict.get(SpecialChars.SPACE.value)
            elif ccode == 13 or ccode == 10:
                px[self.point1d2d(inc, im_width)] = self.pxlencdict.get(SpecialChars.NEWLINE.value)
            else:
                print('Character %c not found...' % chr(ccode))
            inc += 1
        px[self.point1d2d(inc, im_width)] = self.pxlencdict.get(SpecialChars.TEXT_END.value)
        im.save(outfile)

if __name__ == '__main__':
    ht = HideText()
    ht.encode_file('Hello from https://www.suturf.com/', '../resource/pexels.jpg', '../resource/pexels.png')
