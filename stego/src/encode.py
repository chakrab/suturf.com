import os
import random
from PIL import Image

from pxldict import PixelDictionary
from spclchars import SpecialChars


class EncodeText:
    encode_key = 1456

    def __init__(self) -> None:
        # Let's create the dictionary
        random.seed(self.encode_key)
        px = PixelDictionary(self.encode_key)
        self.pxlencdict, self.pxldecdict = px.create_dict()

    def encode_file(self, fname):
        pxl_value = []
        char_cnt = 0
        with open(fname, encoding = 'utf-8') as fp:
            pxl_value.append(self.pxlencdict.get(SpecialChars.TEXT_START.value))
            char_cnt += 1
            while c := fp.read(1):
                ccode = ord(c)
                if ccode in self.pxlencdict: 
                    char_cnt += 1
                    pxl_value.append(self.pxlencdict.get(ccode))
                elif ccode == 32:
                    char_cnt += 1
                    pxl_value.append(self.pxlencdict.get(SpecialChars.SPACE.value))
                elif ccode == 13 or ccode == 10:
                    char_cnt += 1
                    pxl_value.append(self.pxlencdict.get(SpecialChars.NEWLINE.value))
                else:
                    print('Character %c not found...' % c)

            pxl_value.append(self.pxlencdict.get(SpecialChars.TEXT_END.value))
            char_cnt += 1
            print('Character Count: %d' % char_cnt)

        return char_cnt, pxl_value

    def write_image(self, fname, char_cnt, pxl_data):
        # Is char count perfect square?
        rt = int(char_cnt ** (0.5))
        if char_cnt == (rt*rt):
            im_width = rt
            im_height = rt
        else:
            im_width = rt + 1
            im_height = rt + 1
        
        im = Image.new(mode='RGB', size=(im_width, im_height))
        cnt = 0
        for y in range(im_height):
            for x in range(im_width):
                if cnt >= char_cnt:
                    # Put Random Pixels
                    r = int(random.randrange(0, 255))
                    g = int(random.randrange(0, 255))
                    b = int(random.randrange(0, 255))
                    im.putpixel((x, y), (r, g, b))
                else:
                    im.putpixel((x, y), pxl_data[cnt])
                cnt += 1
        im.save(fname)


if __name__ == '__main__':
    print(os.getcwd())
    et = EncodeText()
    cnt, pxl = et.encode_file('../resource/SherlockHolmes.txt')
    et.write_image('../resource/SherlockHolmes.png', cnt, pxl)