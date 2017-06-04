"use strict";

console.log("test")

var renderer = new THREE.WebGLRenderer()
renderer.setSize(window.innerWidth, window.innerHeight)
var camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.01, 2000)
    camera.up.x = 0; camera.up.y = 0; camera.up.z = 0;
camera.position.set(0, 0, 0)
camera.lookAt({x:0, y:0, z:0 });

var controls = new THREE.DeviceOrientationControls( camera );

var light = new THREE.PointLight(0xffffff, 2.0)
light.position.set(0, 0, 0)
light.castShadow = false;


var scene = new THREE.Scene()
scene.add(camera)
scene.add(light)

document.body.appendChild(renderer.domElement)

var PI = 3.1415926535;
let initialize = function () {
  var y = 0;
  var z = 0;
  for( var x=0; x<360; x+=10 ){
  for( var y =0; y<360; y+=10 ){
      var size = Math.random();
      var geometry = new THREE.CubeGeometry(0.06+(size*0.1),0.06+(size*0.1),0.06+(size*0.1));
      var material = new THREE.MeshPhongMaterial( { color: 0xffffff } );
      var mesh = new THREE.Mesh( geometry, material );
      var rx = Math.floor( Math.random() * 360 ), ry = Math.floor( Math.random() * 360 )
      mesh.position.set(30 * (( Math.sin( rx * PI/180))), 30 * ( (Math.sin(ry *  PI/180))), (30 * ((Math.cos(rx * PI /180) * (Math.cos( ry * PI/180))) )))
      scene.add(mesh);
  }
  }
}

var render = function () {
    renderer.clear();
    renderer.render(scene, camera)

        controls.update();
    requestAnimationFrame(render)
}

var animate = function(){
      window.requestAnimationFrame( animate );
      controls.update();
      renderer.render(scene, camera);
  };


initialize();
//console.log(dataTransfer.getAccSensorData())
//console.log(dataTransfer.getMagSensorData())

render()
