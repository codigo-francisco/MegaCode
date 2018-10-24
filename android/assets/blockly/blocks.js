Blockly.Blocks['acciones'] = {
  init: function(){
      this.jsonInit({
          "lastDummyAlign0": "RIGHT",
          "message0": "Caminar %1 veces",
          "args0": [
            {
              "type": "input_value",
              "name": "caminar",
              "check": "Number"
            }
          ],
          "inputsInline": true,
          "previousStatement": null,
          "nextStatement": null,
          "colour": 225,
          "tooltip": "Cuantos pasos va a caminar",
          "helpUrl": ""
        });
    }
}

Blockly.Blocks['caminarderecha'] = {
  init: function(){
      this.jsonInit({
        "message0": "Caminar Derecha",
        "previousStatement": null,
        "nextStatement": null,
        "colour": 230,
        "tooltip": "Camina hacia la derecha",
        "helpUrl": ""
      });
  }
}

Blockly.Blocks['caminarizquierda'] = {
  init: function(){
      this.jsonInit({
          "message0": "Caminar Izquierda",
          "previousStatement": null,
          "nextStatement": null,
          "colour": 230,
          "tooltip": "Camina hacia la izquierda",
          "helpUrl": ""
      });
  }
}

Blockly.Blocks['saltar'] = {
  init: function(){
      this.jsonInit({
          "message0": "Saltar",
          "previousStatement": null,
          "nextStatement": null,
          "colour": 230,
          "tooltip": "Saltar",
          "helpUrl": ""
      });
  }
}

Blockly.Blocks['disparar'] = {
  init: function(){
      this.jsonInit({
          "message0": "Disparar",
          "previousStatement": null,
          "nextStatement": null,
          "colour": 230,
          "tooltip": "Saltar",
          "helpUrl": ""
      });
  }
}