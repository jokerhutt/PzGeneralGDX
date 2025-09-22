package jokerhut.main.systems.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;

import jokerhut.main.listeners.DragListener;
import jokerhut.main.model.selection.DragEvent;

public class CameraPanSystem implements DragListener {

	private static final float PAN_SENS = 1f;
	private static final float PAN_DAMP = 12f;
	private int lastX, lastY;
	private boolean dragging;
	private CameraController cameraController;
	private float vx = 0f, vy = 0f;

	public CameraPanSystem(CameraController cameraController) {
		this.cameraController = cameraController;
	}

	@Override
	public boolean onTouchDragged(DragEvent dragEvent) {

		if (!dragging) {
			return false;
		}

		OrthographicCamera camera = cameraController.getCamera();

		float deltaX = dragEvent.x() - lastX;
		float deltaY = dragEvent.y() - lastY;

		float tx = -deltaX * camera.zoom * PAN_SENS;
		float ty = deltaY * camera.zoom * PAN_SENS;

		vx += tx;
		vy += ty;

		lastX = dragEvent.x();
		lastY = dragEvent.y();

		return true;

	}

	public void updatePan(float dt) {
		if (Math.abs(vx) > 0.001f || Math.abs(vy) > 0.001f) {
			OrthographicCamera cam = cameraController.getCamera();
			cam.position.add(vx * dt * PAN_DAMP, vy * dt * PAN_DAMP, 0f);
			cameraController.clampCamera();
			cam.update();

			// decay velocity
			float decay = (float) Math.exp(-PAN_DAMP * dt);
			vx *= decay;
			vy *= decay;
		}
	}

	@Override
	public boolean onTouchUp(DragEvent dragEvent) {
		dragging = false;
		return true;
	}

	@Override
	public boolean onTouchDown(DragEvent dragEvent) {
		dragging = true;
		lastX = dragEvent.x();
		lastY = dragEvent.y();
		return true;
	}

}
