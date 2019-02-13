var blocklyArea = document.getElementById('blocklyArea');
var blocklyDiv = document.getElementById('blocklyDiv');

var workspace = Blockly.inject(blocklyDiv,
    {
        toolbox: document.getElementById('toolbox'),
        zoom: {
                controls: true,
                wheel: true,
                startScale: 1.0,
                maxScale: 2,
                minScale: .2,
                scaleSpeed: .1
        },
        horizontalLayout: true
    }
);
var onresize = function(e) {
    // Compute the absolute coordinates and dimensions of blocklyArea.
    blocklyArea.style.height = window.innerHeight+"px";
    var element = blocklyArea;
    var x = 0;
    var y = 0;
    do {
      x += element.offsetLeft;
      y += element.offsetTop;
      element = element.offsetParent;
    } while (element);
    // Position blocklyDiv over blocklyArea.
    blocklyDiv.style.left = x + 'px';
    blocklyDiv.style.top = y + 'px';
    blocklyDiv.style.width = blocklyArea.offsetWidth + 'px';
    blocklyDiv.style.height = blocklyArea.offsetHeight + 'px';
    Blockly.svgResize(workspace);
 };
window.addEventListener('load', onresize);
//onresize();
//Blockly.svgResize(workspace);
Blockly.JavaScript.addReservedWords('code');
Blockly.JavaScript.STATEMENT_PREFIX = 'highlightBlock(%1);\n';
Blockly.JavaScript.addReservedWords('highlightBlock');

function highlightBlock(id) {
  workspace.highlightBlock(id);
}

var highlightPause = false;

function initApi(interpreter, scope) {
  // Add an API function for highlighting blocks.
  var wrapper = function(id) {
    highlightPause = true;
    return workspace.highlightBlock(id);
  };

  interpreter.setProperty(scope, 'highlightBlock',
      interpreter.createNativeFunction(wrapper));

  wrapper = function(){
     megacode.caminarDerecha()
     while(!megacode.recibirComandos());
  };

  interpreter.setProperty(scope, 'caminarDerecha',
    interpreter.createNativeFunction(wrapper));

   wrapper = function(){
        megacode.caminarIzquierda();
        while(!megacode.recibirComandos());
   };

  interpreter.setProperty(scope, 'caminarIzquierda',
     interpreter.createNativeFunction(wrapper));

  wrapper = function(){
    megacode.saltar();
    while(!megacode.recibirComandos());
  };

  interpreter.setProperty(scope, 'saltar',
    interpreter.createNativeFunction(wrapper));

  wrapper = function(){
     megacode.disparar();
     while(!megacode.recibirComandos());
  };

  interpreter.setProperty(scope, 'disparar',
    interpreter.createNativeFunction(wrapper));

  wrapper = function(){
    return megacode.enemigoDeFrente();
  }

  interpreter.setProperty(scope, 'enemigoDeFrente', interpreter.createNativeFunction(wrapper));

  interpreter.setProperty(scope, 'juegoTerminado', interpreter.createNativeFunction(function(){ return megacode.juegoTerminado() }));
}

var myInterpreter;

function doStep(){
    do {
        try {
          var hasMoreCode = myInterpreter.step();
          if (megacode.respawn()){
            break;
          }
          if (hasMoreCode && highlightPause){
            window.setTimeout(doStep, 1);
          }
        } finally {
        }
     } while (hasMoreCode && !highlightPause);
}

function getCodeBlockly(){
    megacode.codigoBlocklyGenerado(Blockly.JavaScript.workspaceToCode(workspace));
}

function runBlockly(){
    var parent = this;
    megacode.prepararNivel();
    code = Blockly.JavaScript.workspaceToCode(workspace);
    myInterpreter = new Interpreter(code, initApi);

    doStep();
}