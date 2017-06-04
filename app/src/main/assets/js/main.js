"use strict";

console.log("test")

var renderer = new THREE.WebGLRenderer()
var camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.01, 2000)
    camera.up.x = 0; camera.up.y = 1; camera.up.z = 0;
camera.position.set(0, 0, 0)
camera.lookAt({x:0, y:3, z:0 });

controls = new THREE.DeviceOrientationControls( camera );

var light = new THREE.PointLight(0xffffff)
light.position.set(0, 0, 0)

var scene = new THREE.Scene()
scene.add(camera)
scene.add(light)

document.body.appendChild(renderer.domElement)

var PI = 3.1415926535;
let initialize = function () {
  var y = 0;
  var z = 0;
  for( var x=0; x<360; x++ ){
  for( var y =0; x<360; x++ ){
      var geometry = new THREE.CubeGeometry(0.2,0.2,0.2);
      var material = new THREE.MeshPhongMaterial( { color: 0xffffff } );
      var mesh = new THREE.Mesh( geometry, material );
      mesh.position.set(3 * ( x *  (Math.sin(PI/180))), 3 * ( y *  (Math.sin(PI/180))), 3 * ( x *  (Math.cos(PI/180))) )
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
