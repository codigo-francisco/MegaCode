from flask import Flask
from flask import request
from keras.models import load_model
import cv2
import numpy as np

emotions = ["enojado","feliz","miedo","sorpresa","triste","neutral"]
classifier_face = cv2.CascadeClassifier(r"C:\opencv\build\etc\lbpcascades\lbpcascade_frontalface.xml")
model = load_model("model.h5")
app = Flask(__name__)

@app.route('/')
def index():
    return "Raiz"

#@app.route("/emocion", methods=["POST"])
#def get_emotion():
    #fotografia = request.form["fotografia"]
    #print(fotografia)
    #encoded_data = fotografia
    #nparr = np.fromstring(encoded_data.decode("base64"), np.uint8)
    #img = cv2.imdecode(nparr, cv2.IMREAD_GRAYSCALE)

    #predict = emotions[model.predict_classes(np.reshape(img, (1,150,150)))[0]]

    #print(predict)

    #return predict

contador = 0

@app.route("/emocion", methods=["POST"])
def get_emotion():
    fotografia = request.form["fotografia"]
    encoded_data = fotografia #.split(",")[1]
    nparr = np.fromstring(encoded_data.decode("base64"), np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_GRAYSCALE)

    global contador
    cv2.imwrite("D:\\EmotionService\\imagenes\\%s.jpg" % contador)
    contador+=1

    faces = classifier_face.detectMultiScale(img)

    predict = "no_encontrado"

    if len(faces) > 0:
        face = faces[0]
        img_face = img[face[1]:face[1] + face[3], face[0]:face[0] + face[2]]
        img_face = cv2.resize(img_face, (150, 150))

        predict = emotions[model.predict_classes(np.reshape(img_face, (1,150,150)))[0]]

        print(predict)

    return "feliz"


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=80, debug=True)