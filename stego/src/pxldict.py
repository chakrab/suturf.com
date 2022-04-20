import random
from enum import Enum

from spclchars import SpecialChars


class PixelDictionary:
    debug_mode = False

    def __init__(self, secret) -> None:
        random.seed(secret)
        self.enc_dict = {}
        self.dec_dict = {}

    def create_hash(self, r, g, b):
        r_str = str(r)
        g_str = str(g)
        b_str = str(b)
        rgb_str = r_str.rjust(3, '0') + g_str.rjust(3, '0') + b_str.rjust(3, '0')
        return rgb_str

    def create_hash_tuple(self, clr):
        r, g, b = clr
        r_str = str(r)
        g_str = str(g)
        b_str = str(b)
        rgb_str = r_str.rjust(3, '0') + g_str.rjust(3, '0') + b_str.rjust(3, '0')
        return rgb_str

    def is_duplicate(self, thishash):
        if thishash in self.dec_dict:
            return True
        return False

    def print_enc_dict(self):
        for key, value in self.enc_dict.items():
            print(key, '->', value)

    def print_dec_dict(self):
        for key, value in self.dec_dict.items():
            print(key, '->', value)

    def create_dict(self):
        # Start character
        r = int(random.randrange(0, 255))
        g = int(random.randrange(0, 255))
        b = int(random.randrange(0, 255))
        h = self.create_hash(r, g, b)
        self.enc_dict[SpecialChars.TEXT_START.value] = (r, g, b)
        self.dec_dict[h] = SpecialChars.TEXT_START.value

        # End character
        r = int(random.randrange(0, 255))
        g = int(random.randrange(0, 255))
        b = int(random.randrange(0, 255))
        h = self.create_hash(r, g, b)
        self.enc_dict[SpecialChars.TEXT_END.value] = (r, g, b)
        self.dec_dict[h] = SpecialChars.TEXT_END.value

        # New Line
        r = int(random.randrange(0, 255))
        g = int(random.randrange(0, 255))
        b = int(random.randrange(0, 255))
        h = self.create_hash(r, g, b)
        self.enc_dict[SpecialChars.NEWLINE.value] = (r, g, b)
        self.dec_dict[h] = SpecialChars.NEWLINE.value

        # Space
        r = int(random.randrange(0, 255))
        g = int(random.randrange(0, 255))
        b = int(random.randrange(0, 255))
        h = self.create_hash(r, g, b)
        self.enc_dict[SpecialChars.SPACE.value] = (r, g, b)
        self.dec_dict[h] = SpecialChars.SPACE.value

        # ASCII range
        for x in range(33, 127):
            while True:
                r = int(random.randrange(0, 255))
                g = int(random.randrange(0, 255))
                b = int(random.randrange(0, 255))
                h = self.create_hash(r, g, b)
                if not self.is_duplicate(h):
                    break
            
            self.enc_dict[x] = (r, g, b)
            self.dec_dict[h] = x

        if self.debug_mode:
            with open('../debug/enc_dict.txt', 'w', encoding = 'utf-8') as fp:
                for key, value in self.enc_dict.items():
                    fp.write('%10s -> %10s\n' % (key, value))

            with open('../debug/dec_dict.txt', 'w', encoding = 'utf-8') as fp:
                for key, value in self.dec_dict.items():
                    fp.write('%10s -> %10s\n' % (key, value))

        return self.enc_dict, self.dec_dict


if __name__ == '__main__':
    px = PixelDictionary(20)
    px.create_dict()
