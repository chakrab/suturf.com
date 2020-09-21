import cv2
import numpy as np


class DemoRun:
    def __init__(self, lbf, cfg, wgt):
        self.label_file = lbf
        self.config_file = cfg
        self.weights = wgt
        self.color = (0, 0, 255)

    def start_test(self, arrimg):
        # Labels
        labels = open(self.label_file).read().strip().split("\n")
        print("Labels: {}".format(labels))

        # Darknet
        net = cv2.dnn.readNetFromDarknet(self.config_file, self.weights)
        ln = net.getLayerNames()
        print("Layers: {}".format(ln))
        ln = [ln[i[0] - 1] for i in net.getUnconnectedOutLayers()]
        print("Unconnected Layers: {}".format(ln))

        # Now identify images
        for imgt in arrimg:
            print("Processing Image: {}".format(imgt))
            img = cv2.imread(imgt)
            imgh, imgw, _ = img.shape
            blob = cv2.dnn.blobFromImage(img, 1 / 255.0, (608, 608), swapRB=True, crop=False)
            net.setInput(blob)
            layers = net.forward(ln)

            allclz = {}
            for output in layers:
                for detection in output:
                    scores = detection[5:]
                    classID = np.argmax(scores)
                    confidence = round(100*scores[classID], 2)
                    if confidence > 60:
                        if classID in allclz:
                            if allclz[classID]['confidence'] < confidence:
                                allclz[classID] = {'confidence': confidence, 'center': detection[0:2]}
                        else:
                            allclz[classID] = {'confidence': confidence, 'center': detection[0:2]}

            for key in allclz:
                print("\tClassID: {}, confidence: {}%, center: {}, {}"
                      .format(labels[key], allclz[key]['confidence'],
                              allclz[key]['center'][0]*imgw, allclz[key]['center'][1]*imgh))
                ccoord = (int(allclz[key]['center'][0]*imgw),
                          int(allclz[key]['center'][1]*imgh))
                cv2.putText(img, labels[key], ccoord, cv2.FONT_HERSHEY_SIMPLEX,
                            0.5, self.color, 2)
            cv2.imshow("Image", img)
            cv2.waitKey(0)

        cv2.destroyAllWindows()


if __name__ == "__main__":
    dr = DemoRun(
        "../resources/yolofiles/classes.txt",
        "../resources/yolofiles/myyolov3.cfg",
        "../resources/yolofiles/myyolov3_last4300.weights"
    )
    dr.start_test(
        ["../resources/testimg/test01.jpg",
         "../resources/testimg/test02.jpg",
         "../resources/testimg/test03.jpg",
         "../resources/testimg/test04.jpg"
         ]
    )
