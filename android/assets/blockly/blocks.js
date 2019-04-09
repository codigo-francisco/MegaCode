Blockly.Blocks['caminarderecha'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Caminar Derecha");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(5);
 this.setTooltip("Hace que el personaje avance hacia la derecha");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['caminarizquierda'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Caminar Izquierda");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(5);
 this.setTooltip("Comando que hace que el personaje camine hacia la izquierda");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['saltar'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Saltar");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(5);
 this.setTooltip("Comando que hace que el personaje realice un salto");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['disparar'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Disparar");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(5);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['enemigo_defrente'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("enemigo de frente");
    this.setOutput(true, "Boolean");
    this.setColour(210);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['fin_deljuego'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("juego terminado");
    this.setOutput(true, "Boolean");
    this.setColour(210);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['nofin_deljuego'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("juego no terminado");
    this.setOutput(true, "Boolean");
    this.setColour(225);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};