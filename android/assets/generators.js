Blockly.JavaScript['acciones'] = function(block) {
  var value_caminar = Blockly.JavaScript.valueToCode(block, 'caminar', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  return value_caminar;
};
Blockly.JavaScript['caminarderecha'] = function(block) {
  return "derecha,";
};
Blockly.JavaScript['caminarizquierda'] = function(block) {
  return "izquierda,";
};
Blockly.JavaScript['saltar'] = function(block) {
  return "saltar,";
};