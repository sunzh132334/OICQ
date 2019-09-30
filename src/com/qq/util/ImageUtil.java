package com.qq.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageUtil {
	/**
	 * ͼƬ�ļ��Ķ�ȡ
	 * @param srcImgPath
	 * @return
	 */
	private static BufferedImage InputImage(String srcImgPath) {
		BufferedImage srcImage = null;
		try {
			FileInputStream in = new FileInputStream(srcImgPath);
			srcImage = ImageIO.read(in);
		} catch (IOException e) {
			System.out.println("��ȡͼƬ�ļ�����" + e.getMessage());
			e.printStackTrace();
		}
		return srcImage;
	}

	
	
	/**
	 * ���ļ�ת�� Ϊ�ֽ�����
	 * @param file
	 * @return
	 */
	 public static byte[] getBytesFromFile(File file) {  
	        byte[] ret = null;  
	        try {  
	            if (file == null) {  
	                return null;  
	            }  
	            FileInputStream in = new FileInputStream(file);  
	            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);  
	            byte[] b = new byte[4096];  
	            int n;  
	            while ((n = in.read(b)) != -1) {  
	                out.write(b, 0, n);  
	            }  
	            in.close();  
	            out.close();  
	            ret = out.toByteArray();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return ret;  
	    }  
	
	
	
	/**
	 * ��ͼƬ����ָ���Ĵ�С��ѹ��
	 * @param srcImgPath
	 * @param outImgPath
	 * @param new_w
	 * @param new_h
	 */
	public static void compressImage(String srcImgPath,
			int new_w, int new_h) {
		BufferedImage src = InputImage(srcImgPath);
		disposeImage(src, new_w, new_h);
	}

	/**
	 * ������ͼƬ����һ���Ĵ�Сѹ����Ŀ�ĵ�
	 * @param file
	 * @param outImgPath
	 * @param new_w
	 * @param new_h
	 */
	public static BufferedImage compressImage(File file,
			int new_w, int new_h) {
		BufferedImage src = null;
		try {
			src = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return disposeImage(src, new_w, new_h);
	}
	
	
	
	/**
	 * ����ͼƬ
	 * @param src
	 * @param outImgPath
	 * @param new_w
	 * @param new_h
	 */
	private synchronized static BufferedImage disposeImage(BufferedImage src,
			 int new_w, int new_h) {
		// �õ�ͼƬ
		int old_w = src.getWidth();
		// �õ�Դͼ��
		int old_h = src.getHeight();
		// �õ�Դͼ��
		BufferedImage newImg = null;
		// �ж�����ͼƬ������
		switch (src.getType()) {
		case 13:
			// png,gifnewImg = new BufferedImage(new_w, new_h,
			// BufferedImage.TYPE_4BYTE_ABGR);
			break;
		default:
			newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			break;
		}
		Graphics2D g = newImg.createGraphics();
		// ��ԭͼ��ȡ��ɫ������ͼ
		g.drawImage(src, 0, 0, old_w, old_h, null);
		g.dispose();
		// ����ͼƬ�ߴ�ѹ���ȵõ���ͼ�ĳߴ�
		newImg.getGraphics().drawImage(
				src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0,
				null);
		// ���÷������ͼƬ�ļ�
		return newImg;
		//OutImage(outImgPath, newImg);
	}

	
	/**
	 * ��ͼƬ�����ָ����·��
	 * @param outImgPath
	 * @param newImg
	 */
	private static void OutImage(String outImgPath, BufferedImage newImg) {
		// �ж�������ļ���·���Ƿ���ڣ��������򴴽�
		File file = new File(outImgPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}// ������ļ���
		try {
			ImageIO.write(newImg,
					outImgPath.substring(outImgPath.lastIndexOf(".") + 1),
					new File(outImgPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<Integer, String> readfile(String filepath,
			Map<Integer, String> pathMap) throws Exception {
		if (pathMap == null) {
			pathMap = new HashMap<Integer, String>();
		}

		File file = new File(filepath);
		// �ļ�
		if (!file.isDirectory()) {
			pathMap.put(pathMap.size(), file.getPath());

		} else if (file.isDirectory()) { // �����Ŀ¼�� ����������Ŀ¼ȡ�������ļ���
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "/" + filelist[i]);
				if (!readfile.isDirectory()) {
					pathMap.put(pathMap.size(), readfile.getPath());

				} else if (readfile.isDirectory()) { // ��Ŀ¼��Ŀ¼
					readfile(filepath + "/" + filelist[i], pathMap);
				}
			}
		}
		return pathMap;
	}

	/**
	 * ����ɫͼƬת��Ϊ��ɫ
	 * @param originalPic
	 * @return
	 */
	public static BufferedImage convert2GrayPicture(BufferedImage originalPic) {  
        int imageWidth = originalPic.getWidth();  
        int imageHeight = originalPic.getHeight();  
  
        BufferedImage newPic = new BufferedImage(imageWidth, imageHeight,  
                BufferedImage.TYPE_3BYTE_BGR);  
  
        ColorConvertOp cco = new ColorConvertOp(ColorSpace  
                .getInstance(ColorSpace.CS_GRAY), null);  
        cco.filter(originalPic, newPic);  
       return newPic;
    }  

}
