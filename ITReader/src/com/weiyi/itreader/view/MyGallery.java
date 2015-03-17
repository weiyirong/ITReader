package com.weiyi.itreader.view;

import com.weiyi.itreader.util.ViewUtil;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * �Զ���GalleryЧ����չʾ����С˵�б�
 * 
 * @author κ����
 * @version 1.0
 * */
public class MyGallery extends Gallery {
	private Camera camera = new Camera();
	private int maxRotationAngle = 60;// �����ת�Ƕȣ���3D�л�Ч��
	private int maxZoom = -200;// �������ֵ,�������Ŵ�ͼƬ������ͼƬ��

	public MyGallery(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);// TODO Auto-generated
													// constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);// TODO Auto-generated
													// constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);// TODO Auto-generated
													// constructor stub
	}
	/**
	 * setStaticTransformationsEnabled����������true��ʱ��
	 * ÿ��viewGroup(��Gallery��Դ��Ϳ��Կ������Ǵ�ViewGroup
	 * ��Ӽ̳й�����) �����»�����child��ʱ�򶼻�ٷ�getChildStaticTransformation���������
	 * */
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		int childCenter = ViewUtil.getCenterOfView(child);
		int ratationAngle = 0;

		// ��ʼ����ת��
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		if (childCenter == getCenterOfGallery()) {// �����View��Gallery��������ת�����л���
			transformWithCamera(child, t, 0);
		} else {
			// ����������ת�Ƕ�
			ratationAngle = ((getCenterOfGallery() - ViewUtil
					.getCenterOfView(child)) / child.getWidth())
					* maxRotationAngle;// �ؼ�֮һ
			if (Math.abs(ratationAngle) > maxRotationAngle) {
				ratationAngle = (ratationAngle < 0) ? -maxRotationAngle
						: maxRotationAngle;
			}
			transformWithCamera(child, t, ratationAngle);
		}
		return true;
	}

	/**
	 * ���ͼƬ����תЧ����ͨ��Camera��Matrix���
	 * */
	private void transformWithCamera(View child, Transformation t,
			int rotationAngle) {
		camera.save();
		Matrix matrix = t.getMatrix();
		int imgWidth = child.getLayoutParams().width;
		int imgHeight = child.getLayoutParams().height;
		int rotation = Math.abs(rotationAngle);

		camera.translate(0.0f, 0.0f, 100.0f);// �ƶ�camera���ӽ�
		if (rotation < maxRotationAngle) {
			float zoomAmount = (float) (maxZoom + (rotation * 1.5));
			camera.translate(0.0f, 0.0f, zoomAmount);
		}
		camera.rotateY(rotationAngle);
		camera.getMatrix(matrix);
		matrix.preTranslate(-(imgWidth / 2), -(imgHeight / 2));
		matrix.postTranslate((imgWidth / 2), (imgHeight / 2));
		camera.restore();
	}

	public int getCenterOfGallery() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}
	public int getMaxRotationAngle() {
		return maxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		this.maxRotationAngle = maxRotationAngle;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
	}
	
}
