import os
import glob
import librosa
import numpy as np
import pandas as pd

from musicorspeech import MUSIC_FILE_LOCATION, SPEECH_FILE_LOCATION, \
    DATA_FILE_LOCATION, SPEC_FILE_LOCATION, EXPORT_FILE
from musicorspeech.utils import MiscUtils


class DataReader:
    def extract_mel_spectrogram(self, infile):
        print('Processing: %s' % infile)
        file_parts = os.path.splitext(os.path.basename(infile))
        out_path = os.path.join(SPEC_FILE_LOCATION, file_parts[0] + '.npy')
        X, sample_rate = librosa.core.load(infile)
        mel_spec = librosa.feature.melspectrogram(X, sr=sample_rate).T
        # print(mel_spec.shape)
        np.save(out_path, mel_spec)
        return out_path

    def extract_mel_spectrogram_mean(self, infile):
        print('Processing: %s' % infile)
        file_parts = os.path.splitext(os.path.basename(infile))
        out_path = os.path.join(SPEC_FILE_LOCATION, file_parts[0] + '.npy')
        X, sample_rate = librosa.core.load(infile)
        mel_spec = np.mean(librosa.feature.melspectrogram(X, sr=sample_rate).T, axis=0)
        np.save(out_path, mel_spec)
        return out_path

    def prepare_data(self):
        """
        As first step we will extract the MEL spectrograms and store them.
        We will also create a data file with all the file names and type.
        Identify: MUSIC - 1, SPEECH - 0
        """
        MiscUtils.ensure_path(DATA_FILE_LOCATION)
        MiscUtils.ensure_path(SPEC_FILE_LOCATION)

        df = pd.DataFrame(columns=['filepath', 'type'])
        # Let's start reading the Music Location Now
        music_list = glob.glob(MUSIC_FILE_LOCATION + '/*.wav')
        speech_list = glob.glob(SPEECH_FILE_LOCATION + '/*.wav')
        for music in music_list:
            save_path = self.extract_mel_spectrogram_mean(music)
            df = df.append({'filepath': save_path, 'type': 1}, ignore_index=True)

        # And speech
        for speech in speech_list:
            save_path = self.extract_mel_spectrogram_mean(speech)
            df = df.append({'filepath': save_path, 'type': 0}, ignore_index=True)

        csv_file = os.path.join(DATA_FILE_LOCATION, EXPORT_FILE)
        df.to_csv(csv_file, index=False)


if __name__ == '__main__':
    dr = DataReader()
    dr.prepare_data()
