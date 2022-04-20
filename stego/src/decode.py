import os
from PIL import Image

from pxldict import PixelDictionary
from spclchars import SpecialChars


class DecodeText:
    encode_key = 1456

    def __init__(self) -> None:
        # Let's create the dictionary
        self.px = PixelDictionary(self.encode_key)
        self.pxlencdict, self.pxldecdict = self.px.create_dict()

    def decode_file(self, infname, outfname):
        is_writing = False
        im = Image.open(infname)
        im_width, im_height = im.size
        with open(outfname, 'w', encoding = 'utf-8') as fp:
            for y in range(im_height):
                for x in range(im_width):
                    px_val = im.getpixel((x, y))
                    clr_hash = self.px.create_hash_tuple(px_val)
                    if clr_hash in self.pxldecdict:
                        pxl_val = self.pxldecdict.get(clr_hash)
                        if pxl_val == SpecialChars.TEXT_START.value:
                            is_writing = True
                        elif pxl_val == SpecialChars.TEXT_END.value:
                            is_writing = False
                        else:
                            if is_writing:
                                if pxl_val == SpecialChars.NEWLINE.value:
                                    fp.write('\n')
                                elif pxl_val == SpecialChars.SPACE.value:
                                    fp.write(' ')
                                else:
                                    fp.write(chr(pxl_val))

    

if __name__ == '__main__':
    dc = DecodeText()
    dc.decode_file('../resource/pexels.png', '../resource/pexels.txt')
