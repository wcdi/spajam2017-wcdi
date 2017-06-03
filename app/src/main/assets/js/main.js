console.log("test")

var camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 1000)
camera.position.set(0, 6, 0)

var renderer = new THREE.WebGLRenderer()
renderer.setSize(window.innerWidth, window.innerHeight)

var light = new THREE.PointLight(0xffffff)
light.position.set(0, 0, 0)

var scene = new THREE.Scene()
scene.add(camera)
scene.add(light)

document.body.appendChild(renderer.domElement)

var stars = [
    [0, 0, 0]
]

var render = function () {
    requestAnimationFrame(render)

    renderer.render(scene, camera)
}

render()
