Blockly.Blocks['caminarderecha'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Caminar Derecha");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(230);
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
    this.setColour(230);
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
    this.setColour(230);
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
    this.setColour(230);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};