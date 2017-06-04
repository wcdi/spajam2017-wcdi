console.log("test")

var camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 2000)
camera.position.set(0, 0, 0)
camera.lookAt({x:0, y:0, z:0 });

var renderer = new THREE.WebGLRenderer()
renderer.setSize(window.innerWidth, window.innerHeight)

var light = new THREE.PointLight(0xffffff)
light.position.set(0, 0, 0)

var scene = new THREE.Scene()
scene.add(camera)
scene.add(light)

document.body.appendChild(renderer.domElement)

var stars = [
    [1, 0, 0],
                 [0, 1, 0],
                              [0, 0, 1]
]

var render = function () {
    requestAnimationFrame(render)

    renderer.render(scene, camera)
}

console.log(dataTransfer.getAccSensorData())
console.log(dataTransfer.getMagSensorData())

render()
