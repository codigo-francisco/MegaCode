var blocklyArea = document.getElementById('blocklyArea');
var blocklyDiv = document.getElementById('blocklyDiv');

//var body = document.getElementsByTagName("body");
//body.addEventListener("load", injectBlockly, false);
var workspace = Blockly.inject(blocklyDiv,
    {
        toolbox: document.getElementById('toolbox'),
        zoom: {
                controls: true,
                wheel: true,
                startScale: 1.0,
                maxScale: 2,
                minScale: 1,
                scaleSpeed: .2
        }
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

function initApi(interpreter, scope) {
  // Add an API function for highlighting blocks.
  var wrapper = function(id) {
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
}

function runBlockly(){
    var parent = this;
    megacode.prepararNivel();
    code = Blockly.JavaScript.workspaceToCode(workspace);
    //eval(code);
    var myInterpreter = new Interpreter(code, initApi);

    do {
        try {
          var hasMoreCode = myInterpreter.step();
        } finally {
        }
     } while (hasMoreCode);

    //myInterpreter.run();
}