var scene, camera, renderer, mesh;

function init() {
	scene = new THREE.Scene();
	camera = new THREE.PerspectiveCamera(90, 640/480, 0.1, 1000);

	mesh = new THREE.Mesh(
		new THREE.BoxGeometry(1, 1, 1),
		new THREE.MeshBasicMaterial({color:0xff9999, wireframe: true})
	);

	scene.add(mesh);

	camera.position.set(0, 0, -4);
	camera.lookAt(0, 0, 0);

	renderer = new THREE.WebGLRenderer();
	renderer.setSize(1280, 720);
	document.body.appendChild(renderer.domElement);

	animate();
}

function animate() {
	requestAnimationFrame(animate);

	mesh.rotation.x += 0.01;
	mesh.rotation.y += 0.01;
	mesh.rotation.z += 0.01;

	renderer.render(scene, camera);
}

window.onload = init;