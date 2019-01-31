Blockly.JavaScript['caminarderecha'] = function(block) {
   var code = "caminarDerecha();\n";
   return code;
};

Blockly.JavaScript['caminarizquierda'] = function(block) {
  var code = "caminarIzquierda();\n";
  return code;
};

Blockly.JavaScript['saltar'] = function(block) {
  var code = "saltar();\n";
  return code;
};

Blockly.JavaScript['disparar'] = function(block) {
  var code = "disparar();\n";
  return code;
};

Blockly.JavaScript['enemigo_defrente'] = function(block) {
  var code = 'enemigoDeFrente()'
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.JavaScript['fin_deljuego'] = function(block) {
  var code = 'finDelJuego()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.JavaScript['nofin_deljuego'] = function(block) {
  var code = '!finDelJuego()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};